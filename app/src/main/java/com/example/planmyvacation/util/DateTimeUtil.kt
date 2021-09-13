package com.example.planmyvacation.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    private const val dateFormat = "yyyy-MM-dd"

    fun dateFromString(date: String, format: String = dateFormat): Date? {
        return SimpleDateFormat(format, Locale.getDefault()).parse(date)
    }

    fun stringFromDate(date: Date, format: String = dateFormat): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatDateString(date: String, delimiter: String, format: String): String {
        val dateList = date.split(delimiter)
        if (format == "dmg") {
            return dateList[2].padStart(4, '0') + "-" + dateList[1].padStart(2, '0') + "-" + dateList[0].padStart(2,'0')
        }
        if (format == "mdg") {
            return dateList[2].padStart(4, '0') + "-" + dateList[0].padStart(2, '0') + "-" + dateList[1].padStart(2,'0')
        }
        if (format == "gdm") {
            return dateList[0].padStart(4, '0') + "-" + dateList[2].padStart(2, '0') + "-" + dateList[1].padStart(2,'0')
        }
        if (format == "gmd") {
            return dateList[0].padStart(4, '0') + "-" + dateList[1].padStart(2, '0') + "-" + dateList[2].padStart(2,'0')
        }
        return ""
    }

}