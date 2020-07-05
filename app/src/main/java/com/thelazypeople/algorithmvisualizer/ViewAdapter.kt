package com.thelazypeople.algorithmvisualizer

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class ViewAdapter : PagerAdapter() {
    private var views = ArrayList<View>()
    override fun getItemPosition(`object`: Any): Int {
        val index: Int =views.indexOf(`object`)
        return if(index == -1)
            POSITION_NONE
        else
            index
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = views[position]
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = views.size

    //Adding Views
    public fun addView(v: View): Int {
        return addView(v,views.size)
    }
    public fun addView(v:View, position: Int) : Int{
        views.add(position, v)
        return position
    }

    //Deleting views
    public fun removeView(pager: ViewPager, v:View): Int {
        return removeView(pager, views.indexOf(v))
    }
    public fun removeView(pager: ViewPager,position: Int): Int {
        pager.adapter = null
        views.removeAt(position)
        pager.adapter = this
        return position
    }

    //getting Views
    public fun getView(position: Int): View {
        return views[position]
    }
}