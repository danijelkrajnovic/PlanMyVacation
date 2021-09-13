package com.example.planmyvacation.util

object NetworkUtil {

    fun createGoogleHolidaysRetaliveUrl(language: String, country: String): String {
        return "calendars/${language}.${country}%23holiday%40group.v.calendar.google.com/events?key=ADD_KEY_HERE"
    }
}