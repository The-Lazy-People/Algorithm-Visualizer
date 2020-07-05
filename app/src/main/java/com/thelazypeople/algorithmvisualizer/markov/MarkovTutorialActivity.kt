package com.thelazypeople.algorithmvisualizer.markov

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
import com.thelazypeople.algorithmvisualizer.markov.tutorial.Markov1Fragment
import com.thelazypeople.algorithmvisualizer.markov.tutorial.Markov2Fragment
import com.thelazypeople.algorithmvisualizer.markov.tutorial.Markov3Fragment
import com.thelazypeople.algorithmvisualizer.markov.tutorial.Markov4Fragment
import kotlinx.android.synthetic.main.activity_markov_tutorial.*

class MarkovTutorialActivity : AppCompatActivity() {
    var mDots = arrayOfNulls<TextView>(5)
    var count :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markov_tutorial)
        val viewPagerAdapter = FragmentAdapter(supportFragmentManager)
        viewPagerAdapter.apply {
            addf(Markov1Fragment())
            addf(Markov2Fragment())
            addf(Markov3Fragment())
            addf(Markov4Fragment())
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
                val it = Intent(this, MarkovActivity::class.java)
                startActivity(it)
                finish()
            }
            else{
                viewPager.currentItem = count +1
            }
        }

        skip.setOnClickListener {
            val it = Intent(this, MarkovActivity::class.java)
            startActivity(it)
            finish()
        }
    }
    private fun DotStatus(pos: Int){
        mDots = arrayOfNulls<TextView>(4)
        dotsLayout.removeAllViews()
        for(i  in 0 until mDots.size){
            mDots[i] = TextView(this)
            mDots[i]?.text = "•"
            mDots[i]?.textSize = 35F
            mDots[i]?.setTextColor(Color.parseColor("#50000000"))

            dotsLayout.addView(mDots[i])
        }
        mDots[pos]?.setTextColor(Color.parseColor("#000000"))
    }

}