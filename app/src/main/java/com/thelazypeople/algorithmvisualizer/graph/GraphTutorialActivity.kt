package com.thelazypeople.algorithmvisualizer.graph

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.thelazypeople.algorithmvisualizer.FragmentAdapter
import com.thelazypeople.algorithmvisualizer.R
import com.thelazypeople.algorithmvisualizer.ZoomOutPageTransformer
import com.thelazypeople.algorithmvisualizer.graph.tutorial.*
import kotlinx.android.synthetic.main.activity_graph_tutorial.*

class GraphTutorialActivity : AppCompatActivity() {
    var mDots = arrayOfNulls<TextView>(5)
    var count :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_tutorial)
        val viewPagerAdapter = FragmentAdapter(supportFragmentManager)
        viewPagerAdapter.apply {
            addf(firstFragment())
            addf(SecondFragment())
            addf(ThreeFragment())
            addf(FourthFragment())
            addf(FifthFragment())
        }
        //dot initialisation
        DotStatus(0)
        viewPager.adapter = viewPagerAdapter
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                DotStatus(position)
                count = position
                if(position == 0){
                    back.visibility = View.INVISIBLE
                    back.isEnabled  = false
                    next.text = "Next"
                    skip.visibility = View.VISIBLE
                    skip.isEnabled = true
                }else if( position == mDots.size -1) {
                    back.visibility = View.VISIBLE
                    back.isEnabled  = true
                    next.text = "Finish"
                    skip.visibility = View.INVISIBLE
                    skip.isEnabled = false
                }else{
                    back.visibility = View.VISIBLE
                    back.isEnabled  = true
                    next.text = "Next"
                    skip.visibility = View.VISIBLE
                    skip.isEnabled = true
                }
            }
        })

        back.setOnClickListener {
            viewPager.currentItem = count -1
        }

        next.setOnClickListener {
            if(next.text == "Finish")
            {
                val it = Intent(this, GraphActivity::class.java)
                startActivity(it)
                finish()
            }
            else{
                viewPager.currentItem = count +1
            }
        }

        skip.setOnClickListener {
            val it = Intent(this, GraphActivity::class.java)
            startActivity(it)
            finish()
        }
    }
    private fun DotStatus(pos: Int){
        mDots = arrayOfNulls<TextView>(5)
        dotsLayout.removeAllViews()
        for(i  in mDots.indices){
            mDots[i] = TextView(this)
            mDots[i]?.text = "•"
            mDots[i]?.textSize = 35F
            mDots[i]?.setTextColor(Color.parseColor("#50000000"))

            dotsLayout.addView(mDots[i])
        }
        mDots[pos]?.setTextColor(Color.parseColor("#000000"))
    }
}