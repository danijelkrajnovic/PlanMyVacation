package com.example.planmyvacation.network

import com.example.planmyvacation.models.GoogleHolidays
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface HolidaysApi {

    @GET
    fun getHolidays(@Url url: String?): Call<GoogleHolidays>
}