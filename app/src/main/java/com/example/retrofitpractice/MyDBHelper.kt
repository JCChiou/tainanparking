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

//============列舉經緯度座標對應停車場名稱 , 參照code
enum class MyLnglat(val park_name: String , val my_longitude : Double , val my_Latitude : Double ) {

    B00001("海安路地下停車場", 22.991501, 120.195621),
    A00010("安億路路外停車場", 22.997425, 120.164037),
    A00020("樹屋停車場", 23.003582, 120.159792),
    A00030("遊憩碼頭停車場", 22.996402, 120.159702),
    A00041("水景公園停車場(交工科)", 22.999508, 120.157948),
    A00051("崇善路龍山停車場", 22.982593, 120.221534),
    A00061("東光路地下停車場", 22.992491, 120.231296),
    A00101("南瀛綠都心公園地下停車場", 23.309726, 120.316173),
    B00002("大宏社區公園停車場", 23.307886, 120.317136),
    A00111("公園南路停車場", 23.002911, 120.200498),
    A00131("榮譽街停車場", 22.976591, 120.219439),
    A00141("臺南萬昌街停車場", 22.991078, 120.210567),
    A00151("臺南環河街停車場", 22.992542, 120.192008),
    A00511("民生路停車場", 22.997837, 120.188128),
    A00531("東橋八街停車場", 23.021002, 120.233932),
    A00541("體育之心路外停車場 ", 22.978296, 120.204722),
    A00561("新化廣停二停車場", 23.038083, 120.307251),
    A00571("北園公兒23路外停車場 ", 23.012557, 120.224916),
    A00581("大興公園停車場", 23.011473, 120.192454),
    A00591("大豐公園停車場", 23.008078, 120.187522),
    A00601("停2路外停車場 (南側)", 22.996565, 120.185636),
    A00611("林默娘停車場", 22.992392, 120.156916),
    A00621("港濱停車場(第一區)", 22.994967, 120.163398),
    A00631("億載停車場", 22.991197, 120.158474),
    A00641("水景停車場", 22.999480, 120.158030),
    A00840("怡平停九免費臨時停車場 ", 22.993102, 120.176987),
    A00850("育平9街公有停車場(停10) ", 22.995235, 120.171545),
    A00860("運河路(停3)", 22.997908, 120.160811),
    A00870("文平路與建平五街免費臨時停車場 ", 22.987782, 120.177894),
    A00880("龍山里臨時停車場(第一區) ", 22.984129, 120.220095),
    A00890("龍山里臨時停車場(第二區) ", 22.984527, 120.219863),
    A00900("本原街3段(停1) ", 23.053050, 120.177516),
    A00910("安順里臨時免費公有停車場 ", 23.051369, 120.215407),
    A00920("德光里活動中心停車場 ", 22.979947, 120.219334),
    A00930("東興路公有停車場 ", 23.315489, 120.312910),
    A00940("民族路停車場", 23.318598, 120.318561),
    A00950("西拉雅大道西側臨時停車場", 23.107819, 120.301554),
    A00960("西拉雅大道東側臨時停車場 ", 23.107886, 120.302675),
    A00970("南科特定區LM陽光電城 ", 23.109752, 120.306256),
    A00980("湖美街60巷臨時免費公有停車場 ", 23.001373, 120.181094),
    A00990("竹溪街公有停車場(停10) ", 22.980223, 120.209880),
    A01000("大道停6路外停車場 ", 23.009411, 120.215634),
    A01010("成德停四免費公有停車場 ", 23.008623, 120.202959),
    A01020("賢北街臨時免費公有停車場 ", 23.011481, 120.192496),
    A01030("北成路38巷口臨時免費公有停車場 ", 23.018824, 120.210739),
    A01040("大興四自辦重劃區公有停車場 ", 23.013394, 120.191746),
    B00011("成功大學大學路地下停車場", 22.999852, 120.215339),
    B00012("永康中華立體停車場", 23.000737, 120.238085),
    B00013("永康區復國里停(七)停車場", 23.005160, 120.247130),
    B00014("臺南市仁德區立體停車場", 22.972088, 120.253223),
    B00015("新市國小地下停車場", 23.078299, 120.2953306),
    A01061("港濱停車場(第二區)", 22.994967, 120.163398),
    A01121("西門路育賢停車場", 23.015194, 120.209305),
    B00016("長榮崇明停車場", 22.982578, 120.216159),
    A01131("大學路(社E2)停車場", 22.996415, 120.216435),
    A01141("關聖里停車場", 22.985299, 120.240880),
    A01221("民族路停車場", 22.994291, 120.214003),
    A01231("大學路(社E1)停車場", 22.996186, 120.215532),
    A01241("永福路停車場", 22.989641, 120.200203),
    A01251("停2路外停車場(北側)", 22.996775, 120.185680),
    A01261("崇明停E6停車場", 22.969779, 120.225508),
    A01271("和平公園停車場", 22.977825, 120.233083),
    A01281("德光公園停車場", 22.979767, 120.221511),
    A01291("崇明五街公兒E37停車場", 22.979940, 120.217013),
    A01311("泉南里停車場", 22.983160, 120.216030),
    A01321(" 裕文圖書館停車場", 22.982166, 120.245506),
    A01341("立德臨時停車場", 22.976128, 120.214834),
    D00001("林森路-3路邊停車格", 22.995708, 120.224279),
    D00002("小東路-1 前鋒路口-勝利路口", 23.001162, 120.216190),
    D00003("小東路-2  勝利路口-成大側門", 23.000798, 120.219560),
    D00004("小東路-3 成大側門-長榮路口", 23.000670, 120.220920),
    D00005("小東路-4 長榮路口-中華東路口", 23.000551, 120.223380),
    C00044("南榕大道-1", 22.990382, 120.185285),
    C00045("南榕大道-2", 22.990056, 120.184344),
    C00046("永華路-3", 22.996521, 120.186501),
    C00047("和緯路-1", 23.004300, 120.213713),
    C00048("和緯路-2", 23.012232, 120.205822),
    C00049("府前四街", 22.996996, 120.185319),
    C00050("南島路東段", 22.991104, 120.185832),
    C00051("南島路西段", 22.991315, 120.184180),
    B00009("博客停車場開發股份有限公司燦坤海佃場", 23.026084, 120.190838),
    D00006("勝利路-1(路邊停車格)", 22.990740, 120.217720),
    D00007("勝利路-2(路邊停車格)", 22.996836, 120.218154),
    D00008("勝利路-3(路邊停車格)", 23.001863, 120.218551),
    D00009("長榮路-3(路邊停車格)", 22.993639, 120.221729),
    D00010("長榮路-4(路邊停車格)", 22.998840, 120.222156),
    D00011("前鋒路-3(路邊停車格)", 23.002477, 120.214804),
    D00012("林森路-4(路邊停車格)", 23.005566, 120.224688),
    D00013("東和路(路邊停車格）", 22.998481, 120.225730),
    D00014("安平路(路邊停車格)", 22.998103, 120.172637),
    C00056("海安路-1", 22.989640, 120.195159),
    C00057("海安路-5", 23.000070, 120.198538),
    C00058("公園南路-2", 23.002430, 120.203801),
    C00059("西門路-3", 22.989052, 120.197498),
    C00060("西門路-4", 22.993060, 120.198729),
    C00061("西門路-5", 22.996525, 120.199747),
    C00062("西門路-6", 22.998932, 120.200861),
    C00063("西門路-7", 23.001481, 120.202609),
    C00064("西門路-8", 23.008816, 120.207446),
    C00065("西門圓環-1", 22.997236, 120.200488),
    C00066("西門圓環-2", 22.997297, 120.199666),
    A01351("博客停車場開發股份有限公司台南轉運站", 23.001805, 120.209197),
    D00015("府前路-9(路邊停車格)", 22.993200, 120.184280),
    D00016("大學路-2(路邊停車格)", 22.996090, 120.220482),
    D00017("東寧路-1(路邊停車格)", 22.992170, 120.220100),
    D00018("東豐路-1(路邊停車格)", 23.003900, 120.216190),
    D00019("東豐路-2(路邊停車格)", 23.003276, 120.220291),
    D00020("東豐路-3(路邊停車格)", 23.002854, 120.223324),
    D00021("東豐路-4(路邊停車格)", 23.002441, 120.226031),
    D00022("慶平路-3(路邊停車格)", 22.997191, 120.172522),
    D00023("慶平路-4(路邊停車格)", 22.997508, 120.170447),
    D00024("慶平路-5(路邊停車格)", 22.997743, 120.166683),
    D00025("怡平路(路邊停車格)", 22.993134, 120.179337),
    D00026("郡平路(路邊停車格)", 22.989001, 120.168962),
    D00027("國平路(路邊停車格)", 22.992211, 120.171202),
    D00028("建平17街(路邊停車格)", 22.997531, 120.180323),
    D00029("永華三街(路邊停車格)", 22.993331, 120.181738),
    D00030("建平路-2(路邊停車格)", 22.991522, 120.182923),
    D00031("正南五街", 23.036196, 120.237510),
    D00032("府前路-8(路邊停車格)", 22.992300, 120.188919),
    D00033("青年路-4(路邊停車格)", 22.991440, 120.213516),
    D00034("小東路-6(路邊停車格)", 22.998400, 120.236587),
    D00035("環河街(路邊停車格)", 22.992670, 120.191890),
    D00036("運河北岸C-68號(路邊停車格)", 22.995836, 120.192703),
    D00037("中華西路2段28巷(路邊停車格)", 22.989797, 120.187236),
    D00038("中華東路-1(路邊停車格)", 22.997425, 120.234469),
    D00039("育樂街-1(路邊停車格)", 22.992059, 120.214555),
    D00040("東榮街(路邊停車格)", 22.991961, 120.215456),
    D00041("崇善路(路邊停車格)", 22.979740, 120.224341),
    D00042("安億路(路邊停車格）", 22.993946, 120.159473),
    D00043("光州路(路邊停車格)", 22.989703, 120.160142),
    D00044("後甲二路(路邊停車格)", 22.992940, 120.236971),
    A01461("臺鐵南科車站東側停車場(汽車)", 23.107966, 120.302103),
    A01471("臺鐵南科車站西側停車場(汽車)", 23.107982, 120.302714),
    A01481("富中街停車場", 23.079851, 120.288075),
    A01491("富安三街停車場", 23.081723, 120.288867),
    A01501("明大停車場", 23.073274, 120.289969),
    A01511("億載公園停車場",22.991168,120.159479),
    A01521("水景停車場",22.999175,120.157886),
    A01531("港濱停車場(第一區)",22.994969,120.163416),
    A01541("港濱停車場(第二區)",22.993735,120.158557),
    A01551("林默娘停車場",22.992914,120.157155);
}
class myTransferLatLng(){
    fun toGetLatLng( code: String?) : String {
        var myCode : String  = when(code){
            "A00010" ->"${MyLnglat.A00010.my_longitude}, ${MyLnglat.A00010.my_Latitude}"
            "B00001" ->"${MyLnglat.B00001.my_longitude}, ${MyLnglat.B00001.my_Latitude}"
            "A00020" ->"${MyLnglat.A00020.my_longitude}, ${MyLnglat.A00020.my_Latitude}"
            "A00030" ->"${MyLnglat.A00030.my_longitude}, ${MyLnglat.A00030.my_Latitude}"
            "A00041" ->"${MyLnglat.A00041.my_longitude}, ${MyLnglat.A00041.my_Latitude}"
            "A00051" ->"${MyLnglat.A00051.my_longitude}, ${MyLnglat.A00051.my_Latitude}"
            "A00061" ->"${MyLnglat.A00061.my_longitude}, ${MyLnglat.A00061.my_Latitude}"
            "A00101" ->"${MyLnglat.A00101.my_longitude}, ${MyLnglat.A00101.my_Latitude}"
            "B00002" ->"${MyLnglat.B00002.my_longitude}, ${MyLnglat.B00002.my_Latitude}"
            "A00111" ->"${MyLnglat.A00111.my_longitude}, ${MyLnglat.A00111.my_Latitude}"
            "A00131" ->"${MyLnglat.A00131.my_longitude}, ${MyLnglat.A00131.my_Latitude}"
            "A00141" ->"${MyLnglat.A00141.my_longitude}, ${MyLnglat.A00141.my_Latitude}"
            "A00151" ->"${MyLnglat.A00151.my_longitude}, ${MyLnglat.A00151.my_Latitude}"
            "A00511" ->"${MyLnglat.A00511.my_longitude}, ${MyLnglat.A00511.my_Latitude}"
            "A00531" ->"${MyLnglat.A00531.my_longitude}, ${MyLnglat.A00531.my_Latitude}"
            "A00541" ->"${MyLnglat.A00541.my_longitude}, ${MyLnglat.A00541.my_Latitude}"
            "A00561" ->"${MyLnglat.A00561.my_longitude}, ${MyLnglat.A00561.my_Latitude}"
            "A00571" ->"${MyLnglat.A00571.my_longitude}, ${MyLnglat.A00571.my_Latitude}"
            "A00581" ->"${MyLnglat.A00581.my_longitude}, ${MyLnglat.A00581.my_Latitude}"
            "A00591" ->"${MyLnglat.A00591.my_longitude}, ${MyLnglat.A00591.my_Latitude}"
            "A00601" ->"${MyLnglat.A00601.my_longitude}, ${MyLnglat.A00601.my_Latitude}"
            "A00611" ->"${MyLnglat.A00611.my_longitude}, ${MyLnglat.A00611.my_Latitude}"
            "A00621" ->"${MyLnglat.A00621.my_longitude}, ${MyLnglat.A00621.my_Latitude}"
            "A00631" ->"${MyLnglat.A00631.my_longitude}, ${MyLnglat.A00631.my_Latitude}"
            "A00641" ->"${MyLnglat.A00641.my_longitude}, ${MyLnglat.A00641.my_Latitude}"
            "A00840" ->"${MyLnglat.A00840.my_longitude}, ${MyLnglat.A00840.my_Latitude}"
            "A00850" ->"${MyLnglat.A00850.my_longitude}, ${MyLnglat.A00850.my_Latitude}"
            "A00860" ->"${MyLnglat.A00860.my_longitude}, ${MyLnglat.A00860.my_Latitude}"
            "A00870" ->"${MyLnglat.A00870.my_longitude}, ${MyLnglat.A00870.my_Latitude}"
            "A00880" ->"${MyLnglat.A00880.my_longitude}, ${MyLnglat.A00880.my_Latitude}"
            "A00890" ->"${MyLnglat.A00890.my_longitude}, ${MyLnglat.A00890.my_Latitude}"
            "A00900" ->"${MyLnglat.A00900.my_longitude}, ${MyLnglat.A00900.my_Latitude}"
            "A00910" ->"${MyLnglat.A00910.my_longitude}, ${MyLnglat.A00910.my_Latitude}"
            "A00920" ->"${MyLnglat.A00920.my_longitude}, ${MyLnglat.A00920.my_Latitude}"
            "A00930" ->"${MyLnglat.A00930.my_longitude}, ${MyLnglat.A00930.my_Latitude}"
            "A00940" ->"${MyLnglat.A00940.my_longitude}, ${MyLnglat.A00940.my_Latitude}"
            "A00950" ->"${MyLnglat.A00950.my_longitude}, ${MyLnglat.A00950.my_Latitude}"
            "A00960" ->"${MyLnglat.A00960.my_longitude}, ${MyLnglat.A00960.my_Latitude}"
            "A00970" ->"${MyLnglat.A00970.my_longitude}, ${MyLnglat.A00970.my_Latitude}"
            "A00980" ->"${MyLnglat.A00980.my_longitude}, ${MyLnglat.A00980.my_Latitude}"
            "A00990" ->"${MyLnglat.A00990.my_longitude}, ${MyLnglat.A00990.my_Latitude}"
            "A01000" ->"${MyLnglat.A01000.my_longitude}, ${MyLnglat.A01000.my_Latitude}"
            "A01010" ->"${MyLnglat.A01010.my_longitude}, ${MyLnglat.A01010.my_Latitude}"
            "A01020" ->"${MyLnglat.A01020.my_longitude}, ${MyLnglat.A01020.my_Latitude}"
            "A01030" ->"${MyLnglat.A01030.my_longitude}, ${MyLnglat.A01030.my_Latitude}"
            "A01040" ->"${MyLnglat.A01040.my_longitude}, ${MyLnglat.A01040.my_Latitude}"
            "B00011" ->"${MyLnglat.B00011.my_longitude}, ${MyLnglat.B00011.my_Latitude}"
            "B00012" ->"${MyLnglat.B00012.my_longitude}, ${MyLnglat.B00012.my_Latitude}"
            "B00013" ->"${MyLnglat.B00013.my_longitude}, ${MyLnglat.B00013.my_Latitude}"
            "B00014" ->"${MyLnglat.B00014.my_longitude}, ${MyLnglat.B00014.my_Latitude}"
            "B00015" ->"${MyLnglat.B00015.my_longitude}, ${MyLnglat.B00015.my_Latitude}"
            "A01061" ->"${MyLnglat.A01061.my_longitude}, ${MyLnglat.A01061.my_Latitude}"
            "A01121" ->"${MyLnglat.A01121.my_longitude}, ${MyLnglat.A01121.my_Latitude}"
            "B00016" ->"${MyLnglat.B00016.my_longitude}, ${MyLnglat.B00016.my_Latitude}"
            "A01131" ->"${MyLnglat.A01131.my_longitude}, ${MyLnglat.A01131.my_Latitude}"
            "A01141" ->"${MyLnglat.A01141.my_longitude}, ${MyLnglat.A01141.my_Latitude}"
            "A01221" ->"${MyLnglat.A01221.my_longitude}, ${MyLnglat.A01221.my_Latitude}"
            "A01231" ->"${MyLnglat.A01231.my_longitude}, ${MyLnglat.A01231.my_Latitude}"
            "A01241" ->"${MyLnglat.A01241.my_longitude}, ${MyLnglat.A01241.my_Latitude}"
            "A01251" ->"${MyLnglat.A01251.my_longitude}, ${MyLnglat.A01251.my_Latitude}"
            "A01261" ->"${MyLnglat.A01261.my_longitude}, ${MyLnglat.A01261.my_Latitude}"
            "A01271" ->"${MyLnglat.A01271.my_longitude}, ${MyLnglat.A01271.my_Latitude}"
            "A01281" ->"${MyLnglat.A01281.my_longitude}, ${MyLnglat.A00041.my_Latitude}"
            "A01291" ->"${MyLnglat.A01291.my_longitude}, ${MyLnglat.A01291.my_Latitude}"
            "A01311" ->"${MyLnglat.A01311.my_longitude}, ${MyLnglat.A01311.my_Latitude}"
            "A01321" ->"${MyLnglat.A01321.my_longitude}, ${MyLnglat.A01321.my_Latitude}"
            "A01341" ->"${MyLnglat.A01341.my_longitude}, ${MyLnglat.A01341.my_Latitude}"
            "D00001" ->"${MyLnglat.D00001.my_longitude}, ${MyLnglat.D00001.my_Latitude}"
            "D00002" ->"${MyLnglat.D00002.my_longitude}, ${MyLnglat.D00002.my_Latitude}"
            "D00003" ->"${MyLnglat.D00003.my_longitude}, ${MyLnglat.D00003.my_Latitude}"
            "D00004" ->"${MyLnglat.D00004.my_longitude}, ${MyLnglat.D00004.my_Latitude}"
            "D00005" ->"${MyLnglat.D00005.my_longitude}, ${MyLnglat.D00005.my_Latitude}"
            "C00044" ->"${MyLnglat.C00044.my_longitude}, ${MyLnglat.C00044.my_Latitude}"
            "C00045" ->"${MyLnglat.C00045.my_longitude}, ${MyLnglat.C00045.my_Latitude}"
            "C00046" ->"${MyLnglat.C00046.my_longitude}, ${MyLnglat.C00046.my_Latitude}"
            "C00047" ->"${MyLnglat.C00047.my_longitude}, ${MyLnglat.C00047.my_Latitude}"
            "C00048" ->"${MyLnglat.C00048.my_longitude}, ${MyLnglat.C00048.my_Latitude}"
            "C00049" ->"${MyLnglat.C00049.my_longitude}, ${MyLnglat.C00049.my_Latitude}"
            "C00050" ->"${MyLnglat.C00050.my_longitude}, ${MyLnglat.C00050.my_Latitude}"
            "C00051" ->"${MyLnglat.C00051.my_longitude}, ${MyLnglat.C00051.my_Latitude}"
            "B00009" ->"${MyLnglat.B00009.my_longitude}, ${MyLnglat.B00009.my_Latitude}"
            "D00006" ->"${MyLnglat.D00006.my_longitude}, ${MyLnglat.D00006.my_Latitude}"
            "D00007" ->"${MyLnglat.D00007.my_longitude}, ${MyLnglat.D00007.my_Latitude}"
            "D00008" ->"${MyLnglat.D00008.my_longitude}, ${MyLnglat.D00008.my_Latitude}"
            "D00009" ->"${MyLnglat.D00009.my_longitude}, ${MyLnglat.D00009.my_Latitude}"
            "D00010" ->"${MyLnglat.D00010.my_longitude}, ${MyLnglat.D00010.my_Latitude}"
            "D00011" ->"${MyLnglat.D00011.my_longitude}, ${MyLnglat.D00011.my_Latitude}"
            "D00012" ->"${MyLnglat.D00012.my_longitude}, ${MyLnglat.D00012.my_Latitude}"
            "D00013" ->"${MyLnglat.D00013.my_longitude}, ${MyLnglat.D00013.my_Latitude}"
            "D00014" ->"${MyLnglat.D00014.my_longitude}, ${MyLnglat.D00014.my_Latitude}"
            "C00056" ->"${MyLnglat.C00056.my_longitude}, ${MyLnglat.C00056.my_Latitude}"
            "C00057" ->"${MyLnglat.C00057.my_longitude}, ${MyLnglat.C00057.my_Latitude}"
            "C00058" ->"${MyLnglat.C00058.my_longitude}, ${MyLnglat.C00058.my_Latitude}"
            "C00059" ->"${MyLnglat.C00059.my_longitude}, ${MyLnglat.C00059.my_Latitude}"
            "C00060" ->"${MyLnglat.C00060.my_longitude}, ${MyLnglat.C00060.my_Latitude}"
            "C00061" ->"${MyLnglat.C00061.my_longitude}, ${MyLnglat.C00061.my_Latitude}"
            "C00062" ->"${MyLnglat.C00062.my_longitude}, ${MyLnglat.C00062.my_Latitude}"
            "C00063" ->"${MyLnglat.C00063.my_longitude}, ${MyLnglat.C00063.my_Latitude}"
            "C00064" ->"${MyLnglat.C00064.my_longitude}, ${MyLnglat.C00064.my_Latitude}"
            "C00065" ->"${MyLnglat.C00065.my_longitude}, ${MyLnglat.C00065.my_Latitude}"
            "C00066" ->"${MyLnglat.C00066.my_longitude}, ${MyLnglat.C00066.my_Latitude}"
            "A01351" ->"${MyLnglat.A01351.my_longitude}, ${MyLnglat.A01351.my_Latitude}"
            "D00015" ->"${MyLnglat.D00015.my_longitude}, ${MyLnglat.D00015.my_Latitude}"
            "D00016" ->"${MyLnglat.D00016.my_longitude}, ${MyLnglat.D00016.my_Latitude}"
            "D00017" ->"${MyLnglat.D00017.my_longitude}, ${MyLnglat.D00017.my_Latitude}"
            "D00018" ->"${MyLnglat.D00018.my_longitude}, ${MyLnglat.D00018.my_Latitude}"
            "D00019" ->"${MyLnglat.D00019.my_longitude}, ${MyLnglat.D00019.my_Latitude}"
            "D00020" ->"${MyLnglat.D00020.my_longitude}, ${MyLnglat.D00020.my_Latitude}"
            "D00021" ->"${MyLnglat.D00021.my_longitude}, ${MyLnglat.D00021.my_Latitude}"
            "D00022" ->"${MyLnglat.D00022.my_longitude}, ${MyLnglat.D00022.my_Latitude}"
            "D00023" ->"${MyLnglat.D00023.my_longitude}, ${MyLnglat.D00023.my_Latitude}"
            "D00024" ->"${MyLnglat.D00024.my_longitude}, ${MyLnglat.D00024.my_Latitude}"
            "D00025" ->"${MyLnglat.D00025.my_longitude}, ${MyLnglat.D00025.my_Latitude}"
            "D00026" ->"${MyLnglat.D00026.my_longitude}, ${MyLnglat.D00026.my_Latitude}"
            "D00027" ->"${MyLnglat.D00027.my_longitude}, ${MyLnglat.D00027.my_Latitude}"
            "D00028" ->"${MyLnglat.D00028.my_longitude}, ${MyLnglat.D00028.my_Latitude}"
            "D00029" ->"${MyLnglat.D00029.my_longitude}, ${MyLnglat.D00029.my_Latitude}"
            "D00030" ->"${MyLnglat.D00030.my_longitude}, ${MyLnglat.D00030.my_Latitude}"
            "D00031" ->"${MyLnglat.D00031.my_longitude}, ${MyLnglat.D00031.my_Latitude}"
            "D00032" ->"${MyLnglat.D00032.my_longitude}, ${MyLnglat.D00032.my_Latitude}"
            "D00033" ->"${MyLnglat.D00033.my_longitude}, ${MyLnglat.D00033.my_Latitude}"
            "D00034" ->"${MyLnglat.D00034.my_longitude}, ${MyLnglat.D00034.my_Latitude}"
            "D00035" ->"${MyLnglat.D00035.my_longitude}, ${MyLnglat.D00035.my_Latitude}"
            "D00036" ->"${MyLnglat.D00036.my_longitude}, ${MyLnglat.D00036.my_Latitude}"
            "D00037" ->"${MyLnglat.D00037.my_longitude}, ${MyLnglat.D00037.my_Latitude}"
            "D00038" ->"${MyLnglat.D00038.my_longitude}, ${MyLnglat.D00038.my_Latitude}"
            "D00039" ->"${MyLnglat.D00039.my_longitude}, ${MyLnglat.D00039.my_Latitude}"
            "D00040" ->"${MyLnglat.D00040.my_longitude}, ${MyLnglat.D00040.my_Latitude}"
            "D00041" ->"${MyLnglat.D00041.my_longitude}, ${MyLnglat.D00041.my_Latitude}"
            "D00042" ->"${MyLnglat.D00042.my_longitude}, ${MyLnglat.D00042.my_Latitude}"
            "D00043" ->"${MyLnglat.D00043.my_longitude}, ${MyLnglat.D00043.my_Latitude}"
            "D00044" ->"${MyLnglat.D00044.my_longitude}, ${MyLnglat.D00044.my_Latitude}"
            "A01461" ->"${MyLnglat.A01461.my_longitude}, ${MyLnglat.A01461.my_Latitude}"
            "A01471" ->"${MyLnglat.A01471.my_longitude}, ${MyLnglat.A01471.my_Latitude}"
            "A01481" ->"${MyLnglat.A01481.my_longitude}, ${MyLnglat.A01481.my_Latitude}"
            "A01491" ->"${MyLnglat.A01491.my_longitude}, ${MyLnglat.A01491.my_Latitude}"
            "A01501" ->"${MyLnglat.A01501.my_longitude}, ${MyLnglat.A01501.my_Latitude}"
            "A01511" ->"${MyLnglat.A01511.my_longitude}, ${MyLnglat.A01511.my_Latitude}"
            "A01521" ->"${MyLnglat.A01521.my_longitude}, ${MyLnglat.A01521.my_Latitude}"
            "A01531" ->"${MyLnglat.A01531.my_longitude}, ${MyLnglat.A01531.my_Latitude}"
            "A01541" ->"${MyLnglat.A01541.my_longitude}, ${MyLnglat.A01541.my_Latitude}"
            "A01551" ->"${MyLnglat.A01551.my_longitude}, ${MyLnglat.A01551.my_Latitude}"

            else -> "no LngLat data"
        }
        return myCode
    }
}

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
        Log.d("MyDBHelper", "MyDBHelper 初始化了~~~~~~~~~")
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
        Log.d("啟用", "創建資料庫")
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