package com.example.planmyvacation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.example.planmyvacation.R
import kotlinx.android.synthetic.main.view_info.view.*

class InfoView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var showBubble: Boolean = false
    private var bubbleText: String? = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.view_info, this)

        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.InfoView)
        bubbleText = styledAttributes.getString(R.styleable.InfoView_bubbleText)

        setBubbleText(bubbleText)

        styledAttributes.recycle()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                showBubble = !showBubble
                performClick()
            }
        }

        return true
    }

    override fun performClick(): Boolean {

        if (showBubble) {
            setBubbleText(bubbleText)
            bubble_linear_layout.visibility = View.VISIBLE
            return super.performClick()
        }

        bubble_linear_layout.visibility = View.GONE
        return super.performClick()
    }

    /**
     * Private methods
     */
    private fun setBubbleText(text: String?) {
        text?.let {
            bubble_text_view.text = text
        }
    }

    /**
     * Public methods
     */
    fun setBubbleTextRightAway(text: String) {
            bubbleText = text
            setBubbleText(bubbleText)
    }

    fun setBubbleTextNextTimeItOpens(text: String) {
        bubbleText = text
    }

    fun showBubble() {
        if (!showBubble) {
            showBubble = true
            performClick()
        }
    }

}