package com.thelazypeople.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thelazypeople.algorithmvisualizer.home.ContentFragment
import com.thelazypeople.algorithmvisualizer.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPagerAdapter =ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.apply {
            add(HomeFragment())
            add(ContentFragment())
        }
        viewPager.adapter = viewPagerAdapter
        //viewPager.setPageTransformer(true, ZoomOutPageTransformer())

    }
}