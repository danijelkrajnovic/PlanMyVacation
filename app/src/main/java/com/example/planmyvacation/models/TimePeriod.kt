package com.example.planmyvacation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimePeriod(
        val startDate: String,
        val endDate: String,
        val daysOfVacation: Int
) : Parcelable