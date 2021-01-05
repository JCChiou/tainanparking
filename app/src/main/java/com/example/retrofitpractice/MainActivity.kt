package com.example.retrofitpractice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val DISPtype = 1
const val USER_DISPtype = 2
//台南市政府
/** https://opengov.tainan.gov.tw/OpenApi/api/service/Get/c3604e1d-c4e1-4224-9d41-084ce299c3bf */

class MainActivity : AppCompatActivity(), OnMapReadyCallback , GoogleMap.OnInfoWindowClickListener {
    private lateinit var mClusterManager: ClusterManager<MyItem>
    lateinit var mMap: GoogleMap
    lateinit var dbrw: SQLiteDatabase
    lateinit var userDb : SQLiteDatabase
    private val REQUEST_PERMISSIONS = 1
    private val baseUrl = "https://opengov.tainan.gov.tw:443/OpenApi/"
    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val api = retrofit.create(ApiInterface::class.java)
    private lateinit var userRecyclerView :RecyclerView
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var myadapter :RecShowDbAdapter
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        //初始化googleMap
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS)
        } else{
            val mapFragment: SupportMapFragment? =
                    supportFragmentManager.findFragmentById(R.id.myMapView) as? SupportMapFragment
            mapFragment?.getMapAsync(this)
        }

        /**create user define Location database*/
        userDb = UserDBHelper(this).writableDatabase
        /** 獲取JSON資料 */
        findViewById<Button>(R.id.btn_info).setOnClickListener() {
            if (checkNetwork()) { /** if Net connection is available */
                val myBar = findViewById<ProgressBar>(R.id.mprogressBar)
                myBar.visibility = View.VISIBLE
                /** progress Bar start */
                api.login().enqueue(object : Callback<GetData> {
                    override fun onFailure(call: Call<GetData>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "連線失敗，請確認連線後再嘗試", Toast.LENGTH_SHORT).show()
                    }

                    @SuppressLint("SdCardPath")
                    override fun onResponse(call: Call<GetData>, response: Response<GetData>) {
                        val lis = response.body()!!.data
                        /**寫入dataBase*/
                        myWriteDB(lis)
                        /**讀取database*/
                        val sh = getJsonDbData() //from JSON open data
                        val userDB = getUserDBData()   // user define location
                        addItems(sh)
                        if (userDB.isNotEmpty()) {
                            userAddItem(userDB)
                        }
                        myBar.visibility = (View.GONE)
                        /** progress Bar end */
                    }
                })
            }
        }
        /**user 點擊"記錄現地"的按鈕 */
        findViewById<Button>(R.id.btn_recordLoc).setOnClickListener {
            if(checkNetwork()) {
                showPopupWindow()
            }
        }
        /**user 點擊"編輯位置"的按鈕 */
        findViewById<Button>(R.id.btn_edit).setOnClickListener {
            showEditPopupWindow()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty()) return
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED)
                        finish()
                    else {
                        val map = supportFragmentManager.findFragmentById(R.id.myMapView) as SupportMapFragment
                        map.getMapAsync(this)
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(p0: GoogleMap) {
        //Log.d("Map ready", "ready")
        mMap = p0
        mMap.clear()
        /**判斷權限*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        p0.isMyLocationEnabled = true //顯示我的位置按鈕
        setUpClusterer()
        /** clusterer initial*/
        mMap.setOnInfoWindowClickListener(this)
        /** Cluster Item click event , in this case , trigger google map navigation*/
        mClusterManager.setOnClusterItemInfoWindowClickListener { item ->
            if (item != null) {
                //Log.d("myLoc  ", "${item.position.latitude},${item.position.longitude}")
                val gmmIntentUri =
                        Uri.parse("geo:0,0?q=${item.position.latitude},${item.position.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
        /** 自訂顯示資訊畫面*/
        mClusterManager.markerCollection.setInfoWindowAdapter(ClusterAdapter(this))
        mClusterManager.renderer = CustomClusterRenderer(this, mMap, mClusterManager)
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setUpClusterer() {
        /** locate to Tainan train Station */
        Log.d("初始化標記", "執行")
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(22.997216446206675, 120.21263744087841), 13f))
        mClusterManager = ClusterManager<MyItem>(this, mMap)
        mMap.setOnCameraIdleListener(mClusterManager)
        mMap.setOnMarkerClickListener(mClusterManager)
        mMap.setOnInfoWindowClickListener(mClusterManager)
    }

    private fun userAddItem(addArray: ArrayList<UserDisplay>){
        try {
            for(i in addArray){
                val lat = i.lnglat.split(",")[0].toDouble()
                val lng = i.lnglat.split(",")[1].toDouble()
                val title = i.pName
                val snippet = i.dispType.toString()
                val dispType = i.dispType
                val offsetItem = MyItem(lat,lng,title,snippet,dispType)
                mClusterManager.addItem(offsetItem)
            }
        }catch (e:Exception){
            Toast.makeText(this@MainActivity,"格式轉換錯誤", Toast.LENGTH_SHORT).show()
        }
        mClusterManager.cluster()
    }
    private fun addItems(addArray: ArrayList<display>) {
        mClusterManager.clearItems()
        /** try catch */
        try {
            for (j in addArray) {
                val lat = j.lnglat.toString().split(",")[0].toDouble()
                val lng = j.lnglat.toString().split(",")[1].toDouble()
                val title = j.name
                val snippet = j.car.toString() + "," + j.moto.toString()
                val dispType = j.dType
                val offsetItem = MyItem(lat, lng, title, snippet, dispType)
                mClusterManager.addItem(offsetItem)
            }
        }catch (e:Exception){
                Toast.makeText(this@MainActivity,"格式轉換錯誤", Toast.LENGTH_SHORT).show()
        }
        mClusterManager.cluster()
    }

    private fun myWriteDB(lis: List<ApiData>) {
        dbrw = MyDBHelper(this).writableDatabase
        val myLis = lis // lis from JSON
        val values = ContentValues()  //存放準備寫入DB的文字
        for (i in myLis.indices) {
            values.put(ADDRESS, myLis[i].address.toString())
            values.put(CAR, myLis[i].car)
            values.put(CARDIS, myLis[i].carDis)
            values.put(CARDIS_TOTAL, myLis[i].carDis_total)
            values.put(CARGREEN, myLis[i].carGreen)
            values.put(CARGREEN_TOTAL, myLis[i].carGreen_total)
            values.put(CARWOMAN, myLis[i].carWoman)
            values.put(CARWOMAN_TOTAL, myLis[i].carWoman_total)
            values.put(CAR_TOTAL, myLis[i].car_total)
            values.put(CHARGEFEE, myLis[i].chargeFee.toString())
            values.put(CHARGETIME, myLis[i].chargeTime.toString())
            values.put(CODE, myLis[i].code.toString())
            values.put(ID, myLis[i].id.toString())
            values.put(LARGECAR, myLis[i].largeCar)
            values.put(LARGECAR_TOTAL, myLis[i].largeCar_total)
            values.put(LNGLAT, myLis[i].lnglat)
            values.put(MOTO, myLis[i].moto)
            values.put(MOTODIS, myLis[i].motoDis)
            values.put(MOTODIS_TOTAL, myLis[i].motoDis_total)
            values.put(MOTO_TOTAL, myLis[i].moto_total)
            values.put(NAME, myLis[i].name.toString())
            values.put(TYPEID, myLis[i].typeId.toString())
            values.put(TYPENAME, myLis[i].typeName.toString())
            values.put(UPDATE_TIME, myLis[i].update_time.toString())
            values.put(ZONE, myLis[i].zone.toString())
            values.put(ZONDID, myLis[i].zoneId.toString())
            values.put(DISP_TYPE,DISPtype)
            dbrw.insert(TABLE_NAME, null, values)
        }
    }

    private fun getUserDBData(): ArrayList<UserDisplay>{
        val userShowDB = ArrayList<UserDisplay>()
        val cursor : Cursor = userDb.query(USER_TABLE_NAME,
                arrayOf("id", "pName", "latLng", "u_disptype"),
                null,
                null,
                null,
                null,
                null,
                null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val item = UserDisplay(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("latLng")),
                            cursor.getString(cursor.getColumnIndex("pName")),
                            cursor.getInt(cursor.getColumnIndex("u_disptype"))
                    )
                    userShowDB.add(item)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.d("讀取資料庫出錯了", "$e")
        } finally {
            if (!cursor.isClosed) {
                cursor.close()
            }
        }
        return userShowDB
    }

    private fun getJsonDbData(): ArrayList<display> {
        val showDb = ArrayList<display>()
        val cursor: Cursor = dbrw.query(TABLE_NAME,  //table name
                arrayOf("code", "name", "car", "moto", "zoneId", "zone", "address", "lnglat", "dispType"), //要取出的欄位名稱
                null,                           //查詢條件
                null,
                null,
                null,
                null,
                null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val item = display(
                            cursor.getString(cursor.getColumnIndex("code")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getInt(cursor.getColumnIndex("car")),
                            cursor.getInt(cursor.getColumnIndex("moto")),
                            cursor.getString(cursor.getColumnIndex("zoneId")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getString(cursor.getColumnIndex("lnglat")),
                            cursor.getString(cursor.getColumnIndex("zone")),
                            cursor.getInt(cursor.getColumnIndex("dispType"))
                    )
                    showDb.add(item)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.d("讀取資料庫出錯了", "$e")
        } finally {
            if (!cursor.isClosed) {
                cursor.close()
            }
        }
        return showDb
    }
    /** 更新修改後的名稱 */
    private fun myUpdateDB(uPosition : Int, uString : String){
        userDb.execSQL(" UPDATE " + USER_TABLE_NAME + " SET pName = " + "'" + uString + "'" + "WHERE $USER_ID = '" + uPosition + "'")
    }

    /** 刪除使用者所選取的項目 */
    private fun myDeleteDB(dPosition: Int){
        userDb.delete(USER_TABLE_NAME, " $USER_ID = $dPosition " , null)
    }


    override fun onInfoWindowClick(p0: Marker?) {
        //Log.d("近來onClusterInfoWindowCl","XXXXXXXXXXXXXXXXXXXXX")
    }

    /**show popup window */
    @SuppressLint("InflateParams")
    private fun showEditPopupWindow(){
        /**get userDB */
        val toEditRec = getUserDBData()
        val sPopupView = LayoutInflater.from(this).inflate(R.layout.edit_popup_window,null)
        userRecyclerView = sPopupView.findViewById<RecyclerView>(R.id.rec_spopup)
        linearLayoutManager = LinearLayoutManager(this)
        userRecyclerView.layoutManager = linearLayoutManager
        myadapter = RecShowDbAdapter()
        /** bind adapter */
        userRecyclerView.adapter = myadapter
        myadapter.reload(toEditRec)
        val sPopupWindow = PopupWindow(this)
        sPopupWindow.isFocusable = true
        sPopupWindow.contentView = sPopupView
        sPopupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        sPopupWindow.height = ViewGroup.LayoutParams.MATCH_PARENT
        sPopupWindow.isOutsideTouchable = true
        sPopupWindow.showAsDropDown(findViewById<Button>(R.id.btn_edit))

        /** 點選返回健 隱藏popup window */
        sPopupView.findViewById<Button>(R.id.btn_back).setOnClickListener { sPopupWindow.dismiss() }

        /** RecyclerView click event */
        userRecyclerView.addOnItemTouchListener(SingleItemClickListener(userRecyclerView,object: SingleItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                /** 取得點擊item的ID編號給後續更新與刪除的條件依據 */
                val selItemPosId =  toEditRec[position].id
                //Toast.makeText(this@MainActivity, "touch click:" + position, Toast.LENGTH_SHORT).show()
                /** 單擊顯示編輯視窗 並將單擊送資料庫查詢對應欄位資料 顯示於對話框 */
                val mview = LayoutInflater.from(this@MainActivity).inflate(R.layout.update_userdb,null)
                val edWindow = mview.findViewById<EditText>(R.id.ed_updateName)
                edWindow.setText(toEditRec[position].pName)
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("更新資料")
                        .setView(mview)
                        .setPositiveButton("確定") {
                            /** 執行資料庫更新 */
                            dialog, which ->
                            val getmyText = edWindow.text.toString()
                            myUpdateDB(selItemPosId, getmyText)
                            hideKeyboard(mview)
                            sPopupWindow.dismiss()
                            AlertDialog.Builder(this@MainActivity)
                                    .setTitle("更新資料完成")
                                    .setPositiveButton("OK"){
                                        dialog, which -> dialog?.dismiss()
                                    }
                                    .show()
                        }
                        .setNeutralButton("取消"){
                            dialog, which ->
                            hideKeyboard(mview)
                            dialog?.dismiss()
                        }
                        .setNegativeButton("刪除") {
                            dialog, which ->
                            myDeleteDB(selItemPosId)
                            hideKeyboard(mview)
                            sPopupWindow.dismiss()
                            AlertDialog.Builder(this@MainActivity)
                                    .setTitle("刪除資料完成")
                                    .setPositiveButton("OK"){
                                        dialog, which -> dialog?.dismiss()
                                    }
                                    .show()
                        }
                        .show()
            }

            override fun onItemLongClick(view: View?, position: Int) {
                /** 長按事件 */
//                Toast.makeText(this@MainActivity, "touch Long click:" + position, Toast.LENGTH_SHORT).show();
            }
        }))
    }

    @SuppressLint("Recycle")
    private fun showPopupWindow() {
        val locTosave = getRecordLocation()
        /**get current Location */
        val popupWindow = PopupWindow(this)
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_window, null)
        popupWindow.isFocusable = true
        popupWindow.contentView = popupView
        popupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.isOutsideTouchable = true
        popupWindow.showAsDropDown(findViewById<Button>(R.id.btn_recordLoc))
        popupView.findViewById<TextView>(R.id.tv_saveLoc).text = locTosave

        /**按下ok按鈕 執行的動作*/
        popupView.findViewById<Button>(R.id.popup_ok).setOnClickListener {
            val pName = popupView.findViewById<EditText>(R.id.ed_Pname)
            val nameCheck = pName.text.trim()
            val reg = Regex("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+\$")
            if (reg.matches(nameCheck)){
                //match condition
                /**執行建立資料庫*/
                val values = ContentValues()  //存放準備寫入DB的文字
                /**寫入停車場名稱&經緯度*/
                values.put(USER_PNAME,nameCheck.toString())
                values.put(USER_LATLNG,locTosave )
                values.put(USER_DISP_TYPE, USER_DISPtype)

                if (userDb.isOpen){
                    Log.d("check DB is open","inside")
                    userDb.insert(USER_TABLE_NAME, null, values)
                    popupWindow.dismiss() //隱藏popup window
                    val userDB = getUserDBData()
                    userAddItem(userDB)
                }
            } else{
                // not match
                Toast.makeText(this, "請輸入名稱(不含特殊符號以及空白)", Toast.LENGTH_SHORT).show()
            }
        }

        /**按下取消按鈕 執行的動作*/
        popupView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            popupWindow.dismiss() //隱藏popup window
        }
    }

    private fun getRecordLocation():String?{
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        val oriLocation : Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null
        }
        return if (locationManager != null) {
            oriLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            Log.d("myLoc", "不為Null")
            "${oriLocation.latitude},${oriLocation.longitude}"

        } else {
            Toast.makeText(this,"無法取得位置資訊，請確認網路與定位功能是否開啟",Toast.LENGTH_LONG).show()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkNetwork() : Boolean{
        /** check NetWork if enable */
        val connManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info : Network? = connManager.activeNetwork
        return if(info == null){
            val v = layoutInflater.inflate(R.layout.custom_toast,findViewById(R.id.custom_toast_container),false)
            v.findViewById<TextView>(R.id.toast_msg).text = "請開啟網路連線功能"
            with(Toast(this)){
                duration = Toast.LENGTH_LONG
                view = v
                setGravity(Gravity.CENTER_VERTICAL,0,0)
                show()
            }
            false
        }else true
    }



    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


open class MyItem (lat: Double, lng: Double, title: String, snippet: String, dsiptype : Int) : ClusterItem {
    private val mPosition: LatLng
    private val mTitle: String = title
    private val mSnippet: String = snippet
    private val mDispType: Int= dsiptype

    init {
        mPosition = LatLng(lat, lng)
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }

    open fun getMydispType(): Int{
        return mDispType
    }
}

