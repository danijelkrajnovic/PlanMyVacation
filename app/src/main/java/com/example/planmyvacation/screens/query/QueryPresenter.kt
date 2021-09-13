package com.example.planmyvacation.screens.query

import com.example.planmyvacation.callbacks.ApiCallbacks
import com.example.planmyvacation.models.*
import com.example.planmyvacation.services.HolidayTasks
import com.example.planmyvacation.util.DateTimeUtil
import java.text.DateFormatSymbols
import java.util.*

class QueryPresenter : QueryContract.Presenter {

    private lateinit var interfaceToQueryActivity: QueryContract.View

    /**
     * Public methods
     */
    override fun registerActivity(activityInstance: QueryContract.View) {
        interfaceToQueryActivity = activityInstance
    }

    override fun getHolidaysFromGoogle(timePeriodsList: MutableList<TimePeriod>, language: String, country: String) {

        HolidayTasks.getHolidays(language, country, object : ApiCallbacks<GoogleHolidays> {
            override fun returnResult(result: GoogleHolidays?) {
                val holidays = mutableListOf<Holiday>()
                for (item in result!!.items) {
                    holidays.add(
                        Holiday(
                            item.summary,
                            item.start.date,
                            item.end.date
                        )
                    )
                }
                val listOfAllHolidayDates = listOfAllHolidayDates(holidays)
                val bestVacationOptionsForEachPeriod = mutableListOf<VacationTimePeriodResult>()
                for (period in timePeriodsList) {
                    val startDate = DateTimeUtil.formatDateString(period.startDate, ".", "dmg")
                    val endDate = DateTimeUtil.formatDateString(period.endDate, ".", "dmg")
                    val bestVacationOptionsInCurrentPeriod = calculateBestVacationOptions(startDate, endDate, period.daysOfVacation, listOfAllHolidayDates)

                    val bestVacationOptionsInCurrentPeriodWithFreeDays = mutableListOf<VacationTimePeriod>()
                    for (bestVacationOption in bestVacationOptionsInCurrentPeriod) {
                        val holidaysInBestVacationOption = holidaysInSelectedTimePeriod(bestVacationOption.startDate, bestVacationOption.endDate, holidays)
                        val saturdaysAndSundaysInBestVacationOption = saturdaysAndSundaysInSelectedTimePeriod(bestVacationOption.startDate, bestVacationOption.endDate)
                        bestVacationOptionsInCurrentPeriodWithFreeDays.add(
                            VacationTimePeriod (
                                bestVacationOption,
                                holidaysInBestVacationOption,
                                saturdaysAndSundaysInBestVacationOption
                            )
                        )
                    }

                    bestVacationOptionsForEachPeriod.add(
                        VacationTimePeriodResult(
                            period,
                            bestVacationOptionsInCurrentPeriodWithFreeDays
                        )
                    )
                }

                interfaceToQueryActivity.returnGoogleHolidaysResult(bestVacationOptionsForEachPeriod)
            }

            override fun returnError(message: String) {

            }
        })
    }

    /**
     * Private methods
     */
    private fun calculateBestVacationOptions(startStringDate: String, endStringDate: String, daysOfVacation: Int, holidays: MutableList<String>): MutableList<TimePeriod> {
        val calendar = Calendar.getInstance()
        val startDate = DateTimeUtil.dateFromString(startStringDate)!!
        calendar.time = startDate

        //define starting date taking into account non working days (saturdays, sundays and holidays)
        //check if the day before current date is saturday, sunday or holiday and set the calendar accordingly
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && !holidays.contains(DateTimeUtil.stringFromDate(calendar.time)))
            calendar.add(Calendar.DAY_OF_YEAR, 1)

        //check if the current date is saturday, sunday or holiday and set the calendar accordingly
        var dayWasSetToPreviousDay = false
        do {
            var continueLoop = false
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || holidays.contains(DateTimeUtil.stringFromDate(calendar.time))) {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                continueLoop = true
                dayWasSetToPreviousDay = true
            }
            if (!continueLoop && dayWasSetToPreviousDay) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        } while (continueLoop)

        return searchForMaxFreeDaysInARow(daysOfVacation, calendar, DateTimeUtil.dateFromString(endStringDate)!!, holidays)

    }

    private fun searchForMaxFreeDaysInARow(daysOfVacation: Int, firstDayOfMaxVacation: Calendar, endDate: Date, holidays: MutableList<String>): MutableList<TimePeriod> {

        val bestHolidayDatesInCurrentPeriod = mutableListOf<TimePeriod>()

        var maxDaysOfVacation = 0
        val lastDayOfMaxVacation = Calendar.getInstance()
        lastDayOfMaxVacation.time = firstDayOfMaxVacation.time

        val startDateCalendar = Calendar.getInstance()
        startDateCalendar.time = firstDayOfMaxVacation.time

        val searchCalendar = Calendar.getInstance()
        var dateIsOutOfRange = false

        while (!dateIsOutOfRange) {
            var daysOfVacationLeft = daysOfVacation
            searchCalendar.time = startDateCalendar.time
            var tmpMaxDaysOfVacation = 0

            while (daysOfVacationLeft > 0) {
                if (searchCalendar.time > endDate && (searchCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && searchCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && !holidays.contains(DateTimeUtil.stringFromDate(searchCalendar.time))))
                    dateIsOutOfRange = true
                if (searchCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && searchCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && !holidays.contains(DateTimeUtil.stringFromDate(searchCalendar.time)))
                    daysOfVacationLeft--
                if (daysOfVacationLeft > 0)
                    searchCalendar.add(Calendar.DAY_OF_YEAR, 1)
                tmpMaxDaysOfVacation++
            }

            val totalFreeDaysForCurrentStartDate = checkIfNextDaysAreNonWorkingDays(startDateCalendar, searchCalendar, tmpMaxDaysOfVacation, holidays)

            if (!dateIsOutOfRange && totalFreeDaysForCurrentStartDate.daysOfVacation >= maxDaysOfVacation) {
                maxDaysOfVacation = totalFreeDaysForCurrentStartDate.daysOfVacation
                bestHolidayDatesInCurrentPeriod.add(
                    TimePeriod (
                        totalFreeDaysForCurrentStartDate.startDate,
                        totalFreeDaysForCurrentStartDate.endDate,
                        maxDaysOfVacation
                    )
                )
            }

            startDateCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return keepOnlyBestHolidayDatesInList(bestHolidayDatesInCurrentPeriod)
    }

    private fun checkIfNextDaysAreNonWorkingDays(firstDayOfMaxVacation: Calendar, lastDayOfMaxVacation: Calendar, currentMaxDaysOfVacation: Int, holidays: MutableList<String>): TimePeriod {
        var maxDaysOfVacation = currentMaxDaysOfVacation
        var isNonWorkingDay = true
        do {
            lastDayOfMaxVacation.add(Calendar.DAY_OF_YEAR, 1)
            maxDaysOfVacation++
            if (lastDayOfMaxVacation.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && lastDayOfMaxVacation.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && !holidays.contains(DateTimeUtil.stringFromDate(lastDayOfMaxVacation.time))) {
                lastDayOfMaxVacation.add(Calendar.DAY_OF_YEAR, -1)
                maxDaysOfVacation--
                isNonWorkingDay = false
            }
        } while (isNonWorkingDay)

        return TimePeriod(
            DateTimeUtil.stringFromDate(firstDayOfMaxVacation.time),
            DateTimeUtil.stringFromDate(lastDayOfMaxVacation.time),
            maxDaysOfVacation
        )
    }

    private fun keepOnlyBestHolidayDatesInList(startList: MutableList<TimePeriod>): MutableList<TimePeriod> {
        val endList = mutableListOf<TimePeriod>()

        val highestNumberOfFreeDaysInAList = determineHighestNumberOfFreeDaysInAList(startList)
        for (period in startList) {
            if (period.daysOfVacation == highestNumberOfFreeDaysInAList)
                endList.add(period)
        }

        return endList
    }

    private fun determineHighestNumberOfFreeDaysInAList(timePeriods: MutableList<TimePeriod>): Int {
        var tempHighestNumber = 0

        for (period in timePeriods) {
            if (period.daysOfVacation > tempHighestNumber)
                tempHighestNumber = period.daysOfVacation
        }

        return tempHighestNumber
    }

    private fun listOfAllHolidayDates(holidays: MutableList<Holiday>): MutableList<String> {
        val holidayDates = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        for (holiday in holidays) {
            var startDate = DateTimeUtil.dateFromString(holiday.startDate)
            val endDate = DateTimeUtil.dateFromString(holiday.endDate)
            do {
                holidayDates.add(DateTimeUtil.stringFromDate(startDate!!))
                calendar.time = startDate
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                startDate = calendar.time
            } while (startDate != endDate)
        }

        return holidayDates
    }

    private fun holidaysInSelectedTimePeriod(startDate: String, endDate: String, allHolidays: MutableList<Holiday>): MutableList<Holiday> {

        val vacationStartDate = DateTimeUtil.dateFromString(startDate)
        val vacationEndDate = DateTimeUtil.dateFromString(endDate)

        val holidaysInSelectedPeriod = mutableListOf<Holiday>()

        val holidayEndDateCalendar = Calendar.getInstance()
        for (holiday in allHolidays) {
            val holidayStartDate = DateTimeUtil.dateFromString(holiday.startDate)
            var holidayEndDate = DateTimeUtil.dateFromString(holiday.endDate)
            //Google calendar returns holidays in this fashion:
            //If holiday is only 1 day, for example 22.06.2021
            //Google will return startDate 22.06.2021, and endDate 23.06.2021
            //So I corrected this by making endDate -1
            holidayEndDateCalendar.time = holidayEndDate!!
            holidayEndDateCalendar.add(Calendar.DAY_OF_YEAR, -1)
            holidayEndDate = holidayEndDateCalendar.time

            if (holidayEndDate!! >= vacationStartDate && holidayStartDate!! <= vacationEndDate) {
                holidaysInSelectedPeriod.add(
                    Holiday(
                        holiday.name,
                        DateTimeUtil.stringFromDate(holidayStartDate),
                        DateTimeUtil.stringFromDate(holidayEndDate)
                    )
                )
            }
        }

        holidaysInSelectedPeriod.sortBy { it.startDate }

        return  holidaysInSelectedPeriod
    }

    private fun saturdaysAndSundaysInSelectedTimePeriod(startDate: String, endDate: String): MutableList<Holiday> {
        val vacationStartDate = DateTimeUtil.dateFromString(startDate)
        val vacationEndDate = DateTimeUtil.dateFromString(endDate)

        val saturdaysAndSundaysInSelectedPeriod = mutableListOf<Holiday>()

        val dateFormatSymbols = DateFormatSymbols.getInstance()
        val saturdaysAndSundaysCalendar = Calendar.getInstance()
        saturdaysAndSundaysCalendar.time = vacationStartDate!!
        while (saturdaysAndSundaysCalendar.time <= vacationEndDate) {
            if (saturdaysAndSundaysCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || saturdaysAndSundaysCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                saturdaysAndSundaysInSelectedPeriod.add(
                    Holiday(
                        dateFormatSymbols.weekdays[saturdaysAndSundaysCalendar.get(Calendar.DAY_OF_WEEK)],
                        DateTimeUtil.stringFromDate(saturdaysAndSundaysCalendar.time),
                        DateTimeUtil.stringFromDate(saturdaysAndSundaysCalendar.time)
                    )
                )
            }
            saturdaysAndSundaysCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        saturdaysAndSundaysInSelectedPeriod.sortBy { it.startDate }

        return  saturdaysAndSundaysInSelectedPeriod
    }
}