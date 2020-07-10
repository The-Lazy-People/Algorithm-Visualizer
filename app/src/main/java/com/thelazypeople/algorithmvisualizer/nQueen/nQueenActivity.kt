package com.thelazypeople.algorithmvisualizer.nQueen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sdsmdg.harjot.crollerTest.Croller
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.activity_n_queen.*

class nQueenActivity : AppCompatActivity(), OnCrollerChangeListener {
    var noOfQueens=0
    var boardSize=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_n_queen)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val sharedPreferences=this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
        val prefEditor=sharedPreferences.edit()

        boardSizeSB.setOnCrollerChangeListener(this)
        startVisualizationBTN.setOnClickListener {
            prefEditor.putInt("boardSize",boardSize)
            prefEditor.apply()
            prefEditor.commit()
            val intent = Intent(this,VisualizationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onProgressChanged(croller: Croller?, progress: Int) {
        showBoardSizeTV.text = progress.toString()
        boardSize = progress
    }

    override fun onStartTrackingTouch(croller: Croller?) {}

    override fun onStopTrackingTouch(croller: Croller?) {}
}