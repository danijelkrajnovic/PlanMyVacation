package com.example.planmyvacation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VacationTimePeriod(
    val timePeriod: TimePeriod,
    val holidays: MutableList<Holiday>,
    val saturdaysAndSundays: MutableList<Holiday>
) : Parcelable

@Parcelize
data class VacationTimePeriodResult(
    val searchedTimePeriod: TimePeriod,
    val vacationTimePeriods: MutableList<VacationTimePeriod>
) : Parcelable
