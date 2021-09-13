package com.example.planmyvacation.base

import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.planmyvacation.R

private lateinit var screenTitle: TextView
private lateinit var imageBackButton: ImageButton

open class BaseActivity: AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        val coordinatorLayout: CoordinatorLayout = layoutInflater.inflate(R.layout.activity_base, null) as CoordinatorLayout
        val activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.layout_container)
        screenTitle = coordinatorLayout.findViewById(R.id.text_screen_title) as TextView
        imageBackButton = coordinatorLayout.findViewById(R.id.image_back_button)

        layoutInflater.inflate(layoutResID, activityContainer, true)
        setupView()

        super.setContentView(coordinatorLayout)
    }

    /**
     * Private methods
     */
    private fun setupView() {
        imageBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Public methods
     */
    fun setScreenTitle(title: String) {
        screenTitle.text = title
    }

    fun getBackButton(): ImageButton {
        return imageBackButton
    }

}