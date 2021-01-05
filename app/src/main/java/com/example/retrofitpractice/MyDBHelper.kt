package com.example.retrofitpractice

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File

/**定義Parking資料表&欄位名稱 */
const val TABLE_NAME = "myParkingTable"
const val ID = "id"
const val TYPEID = "typeId"  //停車場種類代號
const val ADDRESS = "address"
const val CAR = "car"  //一般小型車剩餘數量
const val CARDIS = "carDis" //殘障者專用小型車剩餘車位
const val CARDIS_TOTAL = "carDis_total"  //殘障者專用小型車總車位
const val CARGREEN = "carGreen" //綠能車位剩餘
const val CARGREEN_TOTAL = "carGreen_total" //綠能車位總數
const val CARWOMAN = "carWoman" //婦幼專用車位剩餘
const val CARWOMAN_TOTAL = "carWoman_total" //婦幼專用車位總數
const val CAR_TOTAL = "car_total"
const val CHARGEFEE = "chargeFee"  //收費方式
const val CHARGETIME = "chargeTime" //收費期間
const val CODE = "code" //停車場代號
const val LARGECAR = "largeCar"
const val LARGECAR_TOTAL = "largeCar_total"
const val LNGLAT = "lnglat"  //經緯度
const val MOTO = "moto"  //一般摩托車
const val MOTODIS = "motoDis"
const val MOTODIS_TOTAL = "motoDis_total" //殘障型摩托車
const val MOTO_TOTAL = "moto_total"
const val NAME = "name" //停車場名稱
const val TYPENAME = "typeName"  //停車場種類
const val UPDATE_TIME = "update_time"
const val ZONE = "zone" //行政區
const val ZONDID = "zoneId"
const val DISP_TYPE = "dispType"

/**使用者自行新增位置的資料表&欄位名稱*/
const val USER_TABLE_NAME = "userParkingTable"
const val USER_ID = "id"
const val USER_LATLNG = "latLng"
const val USER_PNAME = "pName"
const val USER_DISP_TYPE = "u_disptype"



class UserDBHelper(context: Context, name: String = userdatabase, factory: SQLiteDatabase.CursorFactory? = null, version: Int = user_v) : SQLiteOpenHelper(context, name, factory, version){

    companion object{
        private const val userdatabase = "userParkingTable.db"  //資料庫名稱
        private const val user_v = 1      //資料庫版本
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${USER_TABLE_NAME} (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_LATLNG + " TEXT NOT NULL, " +
                USER_PNAME + " TEXT NOT NULL, " +
                USER_DISP_TYPE + " INTEGER NOT NULL )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}

@SuppressLint("SdCardPath")
class MyDBHelper (context : Context, name: String = database, factory: SQLiteDatabase.CursorFactory? = null, version: Int = v):SQLiteOpenHelper(context,name,factory,version){
    init {
        //初始化判斷是否存在資料表,如果有檔案就執行刪除
        //Log.d("MyDBHelper", "MyDBHelper 初始化了~~~~~~~~~")
        val file = File("/data/data/com.example.retrofitpractice/databases")
        val filename :File= File("/data/data/com.example.retrofitpractice/databases","myParkingTable.db")
        //myParkingTable.db-journal
        if(file.isDirectory){
            filename.delete()
        }
    }

    companion object{
        private const val database = "myParkingTable.db"  //資料庫名稱
        private const val v = 1      //資料庫版本

    }


    override fun onCreate(db: SQLiteDatabase?) {  //新建資料庫的語法
        //Log.d("啟用", "創建資料庫")
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME}(" +
                ID + " TEXT PRIMARY KEY," +
                TYPEID + " INTEGER NOT NULL," +
                ADDRESS + " TEXT NOT NULL," +
                CAR + " INTEGER NOT NULL," +
                CARDIS + " INTEGER NOT NULL," +
                CARDIS_TOTAL + " INTEGER NOT NULL," +
                CARGREEN + " INTEGER NOT NULL," +
                CARGREEN_TOTAL + " INTEGER NOT NULL," +
                CARWOMAN + " INTEGER NOT NULL," +
                CARWOMAN_TOTAL + " INTEGER NOT NULL," +
                CAR_TOTAL + " INTEGER NOT NULL," +
                CHARGEFEE + " TEXT," +
                CHARGETIME + " TEXT," +
                CODE + " TEXT," +
                LARGECAR + " INTEGER," +
                LARGECAR_TOTAL + " INTEGER," +
                LNGLAT + " TEXT," +
                MOTO + " INTEGER NOT NULL," +
                MOTODIS + " INTEGER NOT NULL," +
                MOTODIS_TOTAL + " INTEGER NOT NULL," +
                MOTO_TOTAL + " INTEGER NOT NULL," +
                NAME + " TEXT," +
                TYPENAME + " TEXT," +
                UPDATE_TIME + " TEXT," +
                ZONE + " TEXT," +
                ZONDID + " TEXT, " +
                DISP_TYPE + " INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}