package com.example.retrofitpractice

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = RoomGetJsonDB.TABLE_NAME)
class RoomGetJsonDB {
    companion object {
        const val TABLE_NAME = "myParkingTable"
    }


    @PrimaryKey val ID :String = ""
    @NonNull
    @ColumnInfo val typeid: String = "" //停車場種類代號
    @ColumnInfo(name = "ADDRESS", typeAffinity = ColumnInfo.TEXT) val address: String = "" //地址
    @ColumnInfo(name = "CAR",typeAffinity = ColumnInfo.TEXT) val car: String = ""//一般小型車剩餘數量
    @ColumnInfo(name = "CAR_TOTAL",typeAffinity = ColumnInfo.TEXT) val car_total: String = ""//一般小型車總數
    @ColumnInfo(name = "CARDIS",typeAffinity = ColumnInfo.TEXT) val cardis: String = "" //殘障者專用小型車剩餘車位
    @ColumnInfo(name = "CARDIS_TOTAL", typeAffinity = ColumnInfo.TEXT) val cardis_total: String = "" //殘障者專用小型車總車位
    @ColumnInfo(name = "CARGREEN", typeAffinity = ColumnInfo.TEXT) val cargreen: String = "" //綠能車位剩餘
    @ColumnInfo(name = "CARGREEN_TOTAL", typeAffinity = ColumnInfo.TEXT) val cargreen_total: String = "" //綠能車位總數
    @ColumnInfo(name = "CARWOMAN", typeAffinity = ColumnInfo.TEXT) val carwoman: String = "" //婦幼專用車位剩餘
    @ColumnInfo(name = "CARWOMAN_TOTAL", typeAffinity = ColumnInfo.TEXT) val carwoman_total: String = "" //婦幼專用車位總數
    @ColumnInfo(name = "MOTO", typeAffinity = ColumnInfo.TEXT) val moto: String = "" //一般摩托車
    @ColumnInfo(name = "MOTODIS", typeAffinity = ColumnInfo.TEXT) val motodis: String = "" //殘障型摩托車
    @ColumnInfo(name = "MOTODIS_TOTAL", typeAffinity = ColumnInfo.TEXT) val motodis_total: String = "" //殘障行車位總數
    @ColumnInfo(name = "MOTO_TOTAL", typeAffinity = ColumnInfo.TEXT) val moto_total: String = "" //一般摩托車總數
    @ColumnInfo(name = "NAME", typeAffinity = ColumnInfo.TEXT) val name: String = "" //停車場名稱
    @ColumnInfo(name = "LARGECAR", typeAffinity = ColumnInfo.TEXT) val largeCar: String = "" //大型車剩餘車位
    @ColumnInfo(name = "LARGECAR_TOTAL", typeAffinity = ColumnInfo.TEXT) val largeCar_total: String = "" //大型車位總數

    @Nullable
    @ColumnInfo(name = "CHARGEFEE", typeAffinity = ColumnInfo.TEXT) val chargeFee: String = "" //收費方式
    @ColumnInfo(name = "CHARGETIME", typeAffinity = ColumnInfo.TEXT) val chargeTime: String = "" // 收費期間
    @ColumnInfo(name = "CODE", typeAffinity = ColumnInfo.TEXT) val code: String = ""



    var LNGLAT = "lnglat"  //經緯度


    @ColumnInfo(name =  "TYPENAME" ) var typename = "typeName"  //停車場種類
    @ColumnInfo(name =  "UPDATE_TIME" ) var update_time = "update_time"
    @ColumnInfo(name =  "ZONE" ) var zone = "zone" //行政區
    @ColumnInfo(name =  "ZONDID" ) var zondid = "zoneId"
    @ColumnInfo(name =  "DISP_TYPE" ) var disp_type = "dispType"
}