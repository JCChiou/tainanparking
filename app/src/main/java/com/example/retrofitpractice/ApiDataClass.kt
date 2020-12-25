package com.example.retrofitpractice

import android.os.Parcel
import android.os.Parcelable

data class GetData(
    val contentType: String,
    val `data`: List<ApiData>,
    val id: String,
    val isImage: Boolean,
    val success: Boolean
)

data class ApiData(
        val address: String?,
        val car: Int,
        val carDis: Int,
        val carDis_total: Int,
        val carGreen: Int,
        val carGreen_total: Int,
        val carWoman: Int,
        val carWoman_total: Int,
        val car_total: Int,
        val chargeFee: String?,
        val chargeTime: String?,
        val code: String?,
        val id: String?,
        val largeCar: Int,
        val largeCar_total: Int,
        val lnglat: String?,
        val moto: Int,
        val motoDis: Int,
        val motoDis_total: Int,
        val moto_total: Int,
        val name: String?,
        val typeId: String?,
        val typeName: String?,
        val update_time: String?,
        val zone: String?,
        val zoneId: String?
)

data class display(
    val code: String?,
    val name : String,
    val car : Int,
    val moto : Int,
    val zoneId : String,
    val address : String?,
    val lnglat: String?,
    val zone: String?,
    val dType : Int
)

data class UserDisplay(
        val lnglat: String,
        val pName : String,
        val dispType : Int
)