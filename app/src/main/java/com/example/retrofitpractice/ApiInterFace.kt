package com.example.retrofitpractice

import retrofit2.Call
import retrofit2.http.GET

//base URL :https://opengov.tainan.gov.tw:443/OpenApi/

interface ApiInterface {

    @GET("api/service/Get/c3604e1d-c4e1-4224-9d41-084ce299c3bf")
    fun login (): Call<GetData>
}

interface CellClickListener {
    fun my_nCellClickListener(data : String?){
        //Log.d("呼叫","$data")
    }

interface OnItemClickHandler{
    // 提供onItemClick方法作為點擊事件，括號內為接受的參數
    fun onItemClick(text: String?)

    // 提供onItemRemove做為移除項目的事件
    fun onItemRemove(position: Int, text: String?)
}
}
