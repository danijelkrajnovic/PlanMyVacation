package com.example.planmyvacation.services

import com.example.planmyvacation.callbacks.ApiCallbacks
import com.example.planmyvacation.models.GoogleHolidays
import com.example.planmyvacation.network.HolidayApiInstances
import com.example.planmyvacation.util.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HolidayTasks {

    fun getHolidays(language: String, country: String, callback: ApiCallbacks<GoogleHolidays>) {
        val call = HolidayApiInstances.getHolidayApiInstance()!!
            .getHolidays(NetworkUtil.createGoogleHolidaysRetaliveUrl(language, country))

        call.enqueue(object : Callback<GoogleHolidays> {
            override fun onFailure(call: Call<GoogleHolidays>?, t: Throwable?) {
                callback.returnError("There was an error and the request was not completed!!!")
            }

            override fun onResponse(call: Call<GoogleHolidays>, response: Response<GoogleHolidays>) {
                if (!response.isSuccessful) {
                    callback.returnError("Error code" + response.code())
                    return
                }
                callback.returnResult(response.body())
            }
        })

    }


}