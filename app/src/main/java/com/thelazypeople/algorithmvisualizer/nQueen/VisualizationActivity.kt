package com.thelazypeople.algorithmvisualizer.nQueen

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.thelazypeople.algorithmvisualizer.R
import com.varunest.sparkbutton.SparkButton
import com.varunest.sparkbutton.SparkButtonBuilder
import kotlinx.android.synthetic.main.activity_visualization.*
import kotlinx.coroutines.*

class VisualizationActivity : AppCompatActivity() {
    var boardSize=0
    val buttons: MutableList<MutableList<SparkButton>> = ArrayList()
    var ld= mutableListOf<Int>()
    var rd= mutableListOf<Int>()
    var cl= mutableListOf<Int>()
    var i=1
    var dataHolder:solutionMatrix=solutionMatrix()
    var job1:Job=GlobalScope.launch {  }
    var job2:Job=GlobalScope.launch {  }
    var job3:Job=GlobalScope.launch {  }
    var activityIsCancelled=0
    var delayTimeLong:Long=2000
    var delayTimeShort:Long=500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualization)
        val sharedPreferences=this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
        boardSize=sharedPreferences.getInt("boardSize",0)
        createButtonGrid()
        job1=GlobalScope.launch(Dispatchers.Main) {
            for (i in 0..30){
                ld.add(0)
                rd.add(0)
                cl.add(0)
            }
            solveNQ()
        }
    }
    private fun createButtonGrid() {
        // new dynamically declared linear layout inside screen linearlayout so grid can be deleted at any time
        val mainScreen = LinearLayout(this)
        mainScreen.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        mainScreen.orientation = LinearLayout.VERTICAL
        var mainScreenID = resources.getIdentifier("mainScreen", "id", packageName)
        mainScreen.id=mainScreenID
        screen.addView(mainScreen)
        for (i in 0 until boardSize) {

            val arrayLinearLayout = LinearLayout(this)
            arrayLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1.0f
            )
            arrayLinearLayout.orientation = LinearLayout.HORIZONTAL
            //arrayLinearLayout.setPadding(1,1,1,1)

            val buttoncol: MutableList<SparkButton> = ArrayList()
            for (j in 0 until boardSize) {
                val sbutton: SparkButton = SparkButtonBuilder(this).setImageSizeDp(30)
                    .setActiveImage(R.drawable.ic_crown) //after creation
                    .setInactiveImage(R.drawable.ic_mathematics_empty) //before creation
                    .setPrimaryColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_blue_dark
                        )
                    )
                    .setSecondaryColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_green_dark
                        )
                    )
                    .build()
                sbutton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f)
                sbutton.pressOnTouch(false)
                sbutton.isEnabled=false
                buttoncol.add(sbutton)
                arrayLinearLayout.addView(sbutton)
            }
            buttons.add(buttoncol)
            mainScreen.addView(arrayLinearLayout)
        }
    }

    suspend fun printSolution(board: Array<IntArray>) {
        job2=GlobalScope.launch(Dispatchers.Main) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Solution - " + i.toString(),
                Snackbar.LENGTH_LONG
            ).show()
            i++
            var dataOfOneMatrix: MutableList<MutableList<Int>> = mutableListOf()
            for (i in 0 until boardSize) {
                for (j in 0 until boardSize) {
                    if (board[i][j] == 1) {
                        var dataOfOneBox: MutableList<Int> = mutableListOf()
                        dataOfOneBox.add(i)
                        dataOfOneBox.add(j)
                        buttons[i][j].playAnimation()
                        buttons[i][j].setInactiveImage(R.drawable.ic_crown)
                        dataOfOneMatrix.add(dataOfOneBox)
                    }
                }
            }
            dataHolder.data.add(dataOfOneMatrix)
            job3=CoroutineScope(Dispatchers.Main).launch {
                val objectAnimator = ObjectAnimator.ofObject(
                    parentScreen,
                    "backgroundColor",
                    ArgbEvaluator(),
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#000000")
                )
                objectAnimator.repeatCount = 1
                objectAnimator.repeatMode = ValueAnimator.REVERSE
                objectAnimator.duration = delayTimeLong/2
                objectAnimator.start()

            }
            delay(delayTimeLong)
        }
    }


    suspend fun solveNQUtil(board: Array<IntArray>, col: Int): Boolean {

        if (col == boardSize) {
            var job1=GlobalScope.launch(Dispatchers.Main) {
                printSolution(board)
            }
            job1.join()
            return true
        }

        var res = false
        for (i in 0 until boardSize) {
            if ((ld[i - col + boardSize - 1] != 1 && rd[i + col] != 1) && cl[i] != 1) {
                board[i][col] = 1
                ld[i - col + boardSize - 1] = 1
                rd[i + col] = 1
                cl[i] = 1
                buttons[i][col].playAnimation()
                buttons[i][col].setInactiveImage(R.drawable.ic_crown)
                delay(delayTimeShort)
                var job2=GlobalScope.launch(Dispatchers.Main) {
                    res = solveNQUtil(board, col + 1) || res
                }
                job2.join()
                board[i][col] = 0
                ld[i - col + boardSize - 1] = 0
                rd[i + col] = 0
                cl[i] = 0
                buttons[i][col].playAnimation()
                buttons[i][col].setInactiveImage(R.drawable.ic_mathematics_empty)
                delay(delayTimeShort)
            }
        }
        return res
    }

    suspend fun solveNQ() {
        val board =
            Array(boardSize) { IntArray(boardSize) }
        if (!solveNQUtil(board, 0)) {
            Snackbar.make(findViewById(android.R.id.content), "No results found!", Snackbar.LENGTH_LONG).show()
            delay(delayTimeLong*2)
            startActivity(Intent(this, nQueenActivity::class.java))
            finish()
            return
        }
        var dataString= Gson().toJson(dataHolder)
        if(activityIsCancelled==0) {
            val intent = Intent(this, ShowSolnActivity::class.java)
            intent.putExtra("solutionMatrix", dataString)
            startActivity(intent)
            finish()
        }
        return
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reset()
    }

    private fun reset() {
        job1.cancel()
        job2.cancel()
        job3.cancel()
        activityIsCancelled=1
    }

}