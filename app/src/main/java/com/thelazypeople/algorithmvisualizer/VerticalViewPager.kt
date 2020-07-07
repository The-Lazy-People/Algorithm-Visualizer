package com.thelazypeople.algorithmvisualizer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager

//Class for Vertical View Paging.
//Implemented by Google
//https://android.googlesource.com/platform/packages/apps/DeskClock/+/master/src/com/android/deskclock/VerticalViewPager.java

class VerticalViewPager(context: Context, attrs: AttributeSet?) :
    ViewPager(context, attrs) {
    constructor(context: Context) : this(context, null) {}

    /**
     * @return `false` since a vertical view pager can never be scrolled horizontally
     */
    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    /**
     * @return `true` iff a normal view pager would support horizontal scrolling at this time
     */
    override fun canScrollVertically(direction: Int): Boolean {
        return super.canScrollHorizontally(direction)
    }

    private fun init() {
        // Make page transit vertical
        setPageTransformer(true, VerticalPageTransformer())
        // Get rid of the overscroll drawing that happens on the left and right (the ripple)
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val toIntercept = super.onInterceptTouchEvent(flipXY(ev))
        // Return MotionEvent to normal
        flipXY(ev)
        return toIntercept
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val toHandle = super.onTouchEvent(flipXY(ev))
        // Return MotionEvent to normal
        flipXY(ev)
        return toHandle
    }

    private fun flipXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val x = (ev.y  / height * width )
        val y = (ev.x  / width * height )
        ev.setLocation(x-200, y+200)
        return ev
    }

    private class VerticalPageTransformer : PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val pageWidth: Int = view.width
            val pageHeight: Int = view.height
            when {
                position < -1 -> {
                    // This page is way off-screen to the left.
                    view.alpha = 0F
                }
                position <= 1 -> {
                    view.alpha = 1F
                    // Counteract the default slide transition
                    view.translationX = pageWidth * -position
                    // set Y position to swipe in from top
                    val yPosition = position * pageHeight
                    view.translationY = yPosition
                }
                else -> {
                    // This page is way off-screen to the right.
                    view.alpha = 0F
                }
            }
        }
    }

    init {
        init()
    }
}