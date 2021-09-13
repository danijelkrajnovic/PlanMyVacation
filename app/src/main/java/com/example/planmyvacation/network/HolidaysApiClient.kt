package com.example.planmyvacation.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object HolidaysApiClient {

    private const val baseUrl = "https://www.googleapis.com/calendar/v3/"
    private var holidayApiClient: Retrofit? = null

    fun getHolidaysApiClient(): Retrofit? {
        if (holidayApiClient == null) {
            holidayApiClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return holidayApiClient
    }

}