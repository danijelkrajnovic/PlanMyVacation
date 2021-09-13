package com.example.planmyvacation.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.planmyvacation.R
import com.example.planmyvacation.screens.query.QueryActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: Activity() {

    /**
     * Lifecycle methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupView()
    }

    /**
     * Private methods
     */
    private fun setupView() {
        start_app_button.setOnClickListener {
            val intent = Intent(this, QueryActivity::class.java)
            startActivity(intent)
        }
    }

}