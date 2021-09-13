package com.example.planmyvacation.screens.results.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyvacation.R
import com.example.planmyvacation.models.VacationTimePeriodResult

class ResultsRecyclerViewAdapter(context: Context): RecyclerView.Adapter<ResultsRecyclerViewAdapter.CustomViewHolder>() {

    private val resultPeriodsList = mutableListOf<VacationTimePeriodResult>()
    private val activityContext = context

    inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val searchedPeriodStartDateTextView: TextView = view.findViewById(R.id.searched_period_start_date)
        val searchedPeriodEndDateTextView: TextView = view.findViewById(R.id.searched_period_end_date)
        val searchedPeriodDaysOfVacationTextView: TextView = view.findViewById(R.id.searched_period_vacation_days)
        val searchedPeriodVacationPeriodsLinearLayout: LinearLayout = view.findViewById(R.id.searched_period_vacation_periods_linear_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val resultRow = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_results_row_searched_period, parent, false)
        return CustomViewHolder(resultRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val searchedTimePeriod = resultPeriodsList[position]
        holder.searchedPeriodVacationPeriodsLinearLayout.removeAllViews()

        holder.searchedPeriodStartDateTextView.text = searchedTimePeriod.searchedTimePeriod.startDate
        holder.searchedPeriodEndDateTextView.text = searchedTimePeriod.searchedTimePeriod.endDate
        holder.searchedPeriodDaysOfVacationTextView.text = searchedTimePeriod.searchedTimePeriod.daysOfVacation.toString()

        for ((index, vacationPeriod) in searchedTimePeriod.vacationTimePeriods.withIndex()) {
            //Vacation View
            val vacationPeriodRow = LayoutInflater.from(activityContext).inflate(R.layout.recycler_view_results_row_vacation_period, holder.searchedPeriodVacationPeriodsLinearLayout, false)

            val vacationPeriodVacationOptionTextView: TextView = vacationPeriodRow.findViewById(R.id.textview_vacation_option)
            val vacationPeriodStartDateTextView: TextView = vacationPeriodRow.findViewById(R.id.vacation_period_start_date)
            val vacationPeriodEndDateTextView: TextView = vacationPeriodRow.findViewById(R.id.vacation_period_end_date)
            val vacationPeriodDaysOfVacationTextView: TextView = vacationPeriodRow.findViewById(R.id.vacation_period_vacation_days)
            val vacationPeriodHolidaysLinearLayout: LinearLayout = vacationPeriodRow.findViewById(R.id.vacation_period_holidays)
            val vacationPeriodSaturdaysAndSundaysLinearLayout: LinearLayout = vacationPeriodRow.findViewById(R.id.vacation_period_saturdays_and_sundays)

            vacationPeriodVacationOptionTextView.text = activityContext.resources.getString(R.string.results_screen_vacation_period_vacation_option, index + 1)
            vacationPeriodVacationOptionTextView.setTypeface(null, Typeface.BOLD_ITALIC)
            vacationPeriodVacationOptionTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            vacationPeriodStartDateTextView.text = vacationPeriod.timePeriod.startDate
            vacationPeriodEndDateTextView.text = vacationPeriod.timePeriod.endDate
            vacationPeriodDaysOfVacationTextView.text = vacationPeriod.timePeriod.daysOfVacation.toString()

            //Holidays View
            for (holiday in vacationPeriod.holidays) {
                val vacationPeriodHolidayRow = LayoutInflater.from(activityContext).inflate(R.layout.vacation_period_holiday_row, holder.searchedPeriodVacationPeriodsLinearLayout, false)

                val holidayNameTextView: TextView = vacationPeriodHolidayRow.findViewById(R.id.holiday_name)
                val holidayStartDateTextView: TextView = vacationPeriodHolidayRow.findViewById(R.id.holiday_start_date)
                val holidayEndDateTextView: TextView = vacationPeriodHolidayRow.findViewById(R.id.holiday_end_date)

                holidayNameTextView.text = holiday.name
                holidayStartDateTextView.text = holiday.startDate
                holidayEndDateTextView.text = holiday.endDate

                vacationPeriodHolidaysLinearLayout.addView(vacationPeriodHolidayRow)
            }

            //Saturdays and Sundays Views
            for (saturdayOrSunday in vacationPeriod.saturdaysAndSundays) {
                val vacationPeriodSaturdayOrSundayRow = LayoutInflater.from(activityContext).inflate(R.layout.vacation_period_saturday_or_sunday_row, holder.searchedPeriodVacationPeriodsLinearLayout, false)

                val saturdayOrSundayNameTextView: TextView = vacationPeriodSaturdayOrSundayRow.findViewById(R.id.saturday_or_sunday_name)
                val saturdayOrSundayDateTextView: TextView = vacationPeriodSaturdayOrSundayRow.findViewById(R.id.saturday_or_sunday_date)

                saturdayOrSundayNameTextView.text = saturdayOrSunday.name
                saturdayOrSundayDateTextView.text = saturdayOrSunday.startDate

                vacationPeriodSaturdaysAndSundaysLinearLayout.addView(vacationPeriodSaturdayOrSundayRow)
            }

            holder.searchedPeriodVacationPeriodsLinearLayout.addView(vacationPeriodRow)
        }
    }

    override fun getItemCount(): Int {
        return resultPeriodsList.size
    }

    /**
     * Public methods
     */
    fun addResultItems(results: MutableList<VacationTimePeriodResult>) {
        resultPeriodsList.addAll(results)
        notifyDataSetChanged()
    }

    fun getResultItems() = resultPeriodsList
}