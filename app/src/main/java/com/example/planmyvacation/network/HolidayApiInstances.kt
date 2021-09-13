package com.example.planmyvacation.network

object HolidayApiInstances {

    private var holidayApiInstance: HolidaysApi? = null

    fun getHolidayApiInstance(): HolidaysApi? {
        if (holidayApiInstance == null) {
            holidayApiInstance = HolidaysApiClient.getHolidaysApiClient()!!.create(HolidaysApi::class.java)
        }
        return holidayApiInstance
    }
}