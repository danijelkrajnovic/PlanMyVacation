package com.example.planmyvacation.screens.results

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyvacation.R
import com.example.planmyvacation.base.BaseActivity
import com.example.planmyvacation.models.VacationTimePeriodResult
import com.example.planmyvacation.screens.query.QueryActivity
import com.example.planmyvacation.screens.results.adapter.ResultsRecyclerViewAdapter

class ResultsActivity : BaseActivity() {

    private lateinit var resultsRecyclerViewAdapter: ResultsRecyclerViewAdapter

    /**
     * Lifecycle methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        setScreenTitle(getString(R.string.results_screen_title))

        val result = this.intent.getParcelableArrayListExtra<VacationTimePeriodResult>(QueryActivity.queryResult)

        setupView(result!!)
    }

    /**
     * Private methods
     */
    private fun setupView(results: MutableList<VacationTimePeriodResult>) {

        setupRecyclerView()
        resultsRecyclerViewAdapter.addResultItems(results)
    }

    private fun setupRecyclerView() {
        val resultsRecyclerView: RecyclerView = findViewById(R.id.results_recycler_view)

        resultsRecyclerViewAdapter = ResultsRecyclerViewAdapter(this)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = resultsRecyclerViewAdapter
    }

}