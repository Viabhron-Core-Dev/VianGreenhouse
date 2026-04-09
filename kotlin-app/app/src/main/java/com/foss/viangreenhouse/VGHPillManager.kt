package com.foss.viangreenhouse

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat

class VGHPillManager(private val context: Context) {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var pillView: View? = null

    @SuppressLint("ClickableViewAccessibility")
    fun showPill(tabName: String, onSwipeUp: () -> Unit) {
        if (pillView != null) {
            pillView?.findViewById<TextView>(R.id.pill_text)?.text = tabName
            return
        }

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 100
        }

        val inflater = LayoutInflater.from(context)
        pillView = inflater.inflate(R.layout.vgh_pill, null)
        pillView?.findViewById<TextView>(R.id.pill_text)?.text = tabName

        val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (velocityY < -500) { // Swipe Up
                    onSwipeUp()
                    return true
                }
                return false
            }
        })

        pillView?.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        windowManager.addView(pillView, layoutParams)
    }

    fun updatePill(tabName: String) {
        pillView?.findViewById<TextView>(R.id.pill_text)?.text = tabName
    }

    fun hidePill() {
        pillView?.let {
            windowManager.removeView(it)
            pillView = null
        }
    }
}
