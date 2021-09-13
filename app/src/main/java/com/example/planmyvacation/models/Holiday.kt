package com.example.planmyvacation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Holiday(
    val name: String,
    val startDate: String,
    val endDate: String
) : Parcelable
