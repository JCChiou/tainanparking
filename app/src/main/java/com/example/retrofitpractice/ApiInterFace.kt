package com.example.retrofitpractice

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

//base URL :https://opengov.tainan.gov.tw:443/OpenApi/

interface ApiInterface {

    @GET("api/service/Get/c3604e1d-c4e1-4224-9d41-084ce299c3bf")
    fun login (): Call<GetData>
}

interface CellClickListener {
    fun my_nCellClickListener(data : String?){
        //Log.d("呼叫","$data")
    }
}
