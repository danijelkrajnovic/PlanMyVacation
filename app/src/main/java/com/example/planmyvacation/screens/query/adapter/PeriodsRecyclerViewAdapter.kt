package com.example.planmyvacation.screens.query.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyvacation.R
import com.example.planmyvacation.models.TimePeriod

class PeriodsRecyclerViewAdapter: RecyclerView.Adapter<PeriodsRecyclerViewAdapter.CustomViewHolder>() {

    private val timePeriodsList = mutableListOf<TimePeriod>()

    inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                deletePeriodItem(adapterPosition)
            }
        }

        val startDateTextView: TextView = view.findViewById(R.id.start_date_recycler_view_text_view)
        val endDateTextView: TextView = view.findViewById(R.id.end_date_recycler_view_text_view)
        val daysOfVacationTextView: TextView = view.findViewById(R.id.days_of_vacation_recycler_view_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val periodRow = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_period_row, parent, false)
        return CustomViewHolder(periodRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val timePeriod = timePeriodsList[position]
        holder.startDateTextView.text = timePeriod.startDate
        holder.endDateTextView.text = timePeriod.endDate
        holder.daysOfVacationTextView.text = timePeriod.daysOfVacation.toString()
    }

    override fun getItemCount(): Int {
        return timePeriodsList.size
    }

    /**
     * Public methods
     */
    fun addPeriodItem(item: TimePeriod) {
        timePeriodsList.add(item)
        notifyItemInserted(timePeriodsList.size - 1)
    }

    fun getPeriodsItems() = timePeriodsList

    /**
     * Private methods
     */
    private fun deletePeriodItem(position: Int) {
        timePeriodsList.removeAt(position)
        notifyItemRemoved(position)
    }

}