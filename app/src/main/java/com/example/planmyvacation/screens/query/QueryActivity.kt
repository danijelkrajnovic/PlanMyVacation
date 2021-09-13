package com.example.planmyvacation.screens.query

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planmyvacation.R
import com.example.planmyvacation.base.BaseActivity
import com.example.planmyvacation.models.GoogleHolidays
import com.example.planmyvacation.models.TimePeriod
import com.example.planmyvacation.models.VacationTimePeriodResult
import com.example.planmyvacation.network.HolidaysApiClient
import com.example.planmyvacation.network.HolidaysApi
import com.example.planmyvacation.screens.query.adapter.PeriodsRecyclerViewAdapter
import com.example.planmyvacation.screens.results.ResultsActivity
import com.example.planmyvacation.util.DateTimeUtil
import com.example.planmyvacation.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_query.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class QueryActivity : BaseActivity(), QueryContract.View {

    companion object {
        const val queryResult = "QUERY_RESULT"
    }

    private val queryPresenter: QueryPresenter by inject()
    private lateinit var periodsRecyclerViewAdapter: PeriodsRecyclerViewAdapter

    /**
     * Lifecycle methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)
        setScreenTitle(getString(R.string.query_screen_title))
        queryPresenter.registerActivity(this)

        setupView()

    }

    /**
     * Public methods
     */
    override fun returnGoogleHolidaysResult(result: MutableList<VacationTimePeriodResult>) {

        val intent = Intent(this, ResultsActivity::class.java)
        intent.putParcelableArrayListExtra(queryResult, result as java.util.ArrayList<VacationTimePeriodResult>)
        startActivity(intent)
    }

    /**
     * Private methods
     */
    private fun setupView() {
        country_text_view.text = getCountryName()
        language_text_view.text = getLanguageName()

        start_date_button.setOnClickListener {
            setEditTextDate(start_date_edit_text)
        }

        end_date_button.setOnClickListener {
            setEditTextDate(end_date_edit_text)
        }

        add_time_period_to_list_button.setOnClickListener {
            //Check if time period fields are empty
            if (start_date_edit_text.text.isEmpty() || end_date_edit_text.text.isEmpty() || days_of_vacation_edit_text.text.isEmpty()) {
                showWarningMessage(getString(R.string.query_screen_time_period_data_missing))
                return@setOnClickListener
            }

            //Check if time period data is correct
            val startDate = DateTimeUtil.dateFromString(DateTimeUtil.formatDateString(start_date_edit_text.text.toString(), ".", "dmg"))
            val endDate = DateTimeUtil.dateFromString(DateTimeUtil.formatDateString(end_date_edit_text.text.toString(), ".", "dmg"))
            if (!startDate!!.before(endDate) || days_of_vacation_edit_text.text.toString().toInt() <= 0) {
                showWarningMessage(getString(R.string.query_screen_incorrect_time_period_data))
                return@setOnClickListener
            }

            //Check if newly created time period is already in the list
            val newTimePeriod = TimePeriod(
                start_date_edit_text.text.toString(),
                end_date_edit_text.text.toString(),
                days_of_vacation_edit_text.text.toString().toInt()
            )
            val timePeriodsList = periodsRecyclerViewAdapter.getPeriodsItems()
            var alreadyInTheList = false
            for (timePeriod in timePeriodsList) {
                if (timePeriod == newTimePeriod) alreadyInTheList = true
            }
            if (alreadyInTheList) {
                showWarningMessage(getString(R.string.query_screen_already_in_the_list))
                return@setOnClickListener
            }

            //Add time period to the list
            periodsRecyclerViewAdapter.addPeriodItem(newTimePeriod)

            //clear input fields
            start_date_edit_text.setText("")
            end_date_edit_text.setText("")
            days_of_vacation_edit_text.setText("")
        }

        calculate_button.setOnClickListener {
            val timePeriodsList = periodsRecyclerViewAdapter.getPeriodsItems()
            if (timePeriodsList.isEmpty()) {
                showWarningMessage(getString(R.string.query_screen_empty_list))
                return@setOnClickListener
            }
            queryPresenter.getHolidaysFromGoogle(timePeriodsList, "en", "Croatian")
        }

        setupRecyclerView()
    }

    private fun showWarningMessage(message: String) {
        query_screen_info_view.setBubbleTextRightAway(message)
        query_screen_info_view.showBubble()
        query_screen_info_view.setBubbleTextNextTimeItOpens(getString(R.string.query_screen_bubble_text))
    }

    private fun setupRecyclerView() {
        periodsRecyclerViewAdapter = PeriodsRecyclerViewAdapter()
        periods_recycler_view.layoutManager = LinearLayoutManager(this)
        periods_recycler_view.adapter = periodsRecyclerViewAdapter
    }

    private fun getLanguageName(): String {
        return Locale.getDefault().displayLanguage
    }

    private fun getLanguageCode(): String {
        return Locale.getDefault().language
    }

    private fun getCountryName(): String {
        return Locale.getDefault().displayCountry
    }

    private fun getCountryCode(): String {
        return Locale.getDefault().country
    }

    private fun setEditTextDate(editText: EditText) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
                editText.setText(getString(R.string.query_screen_date, day, month + 1, year))
            }, currentYear, currentMonth, currentDay)
        datePickerDialog.show()
    }

}