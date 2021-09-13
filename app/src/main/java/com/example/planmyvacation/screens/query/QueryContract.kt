package com.example.planmyvacation.screens.query

import com.example.planmyvacation.models.TimePeriod
import com.example.planmyvacation.models.VacationTimePeriodResult

class QueryContract {

    interface View {
        fun returnGoogleHolidaysResult(result: MutableList<VacationTimePeriodResult>)
    }

    interface Presenter {
        fun registerActivity(activityInstance: QueryContract.View)
        fun getHolidaysFromGoogle(timePeriodsList: MutableList<TimePeriod>, language: String, country: String)
    }
}