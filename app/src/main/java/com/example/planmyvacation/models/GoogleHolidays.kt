package com.example.planmyvacation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class GoogleHolidays(
    val accessRole: String,
    val defaultReminders: List<@RawValue Any>,
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextSyncToken: String,
    val summary: String,
    val timeZone: String,
    val updated: String
): Parcelable

@Parcelize
data class Item(
    val created: String,
    val creator: Creator,
    val description: String,
    val end: End,
    val etag: String,
    val eventType: String,
    val htmlLink: String,
    val iCalUID: String,
    val id: String,
    val kind: String,
    val organizer: Organizer,
    val sequence: Int,
    val start: Start,
    val status: String,
    val summary: String,
    val transparency: String,
    val updated: String,
    val visibility: String
): Parcelable

@Parcelize
data class Creator(
    val displayName: String,
    val email: String,
    val self: Boolean
): Parcelable

@Parcelize
data class End(
    val date: String
): Parcelable

@Parcelize
data class Organizer(
    val displayName: String,
    val email: String,
    val self: Boolean
): Parcelable

@Parcelize
data class Start(
    val date: String
): Parcelable
