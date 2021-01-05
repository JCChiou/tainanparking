package com.example.retrofitpractice

import retrofit2.Call
import retrofit2.http.GET

//base URL :https://opengov.tainan.gov.tw:443/OpenApi/

interface ApiInterface {

    @GET("api/service/Get/c3604e1d-c4e1-4224-9d41-084ce299c3bf")
    fun login (): Call<GetData>
}

