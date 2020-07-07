package com.thelazypeople.algorithmvisualizer.searching

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.activity_searching.*
import kotlinx.android.synthetic.main.activity_searching.arraySizeSeekBar
import kotlinx.android.synthetic.main.activity_searching.randamizebtn
import kotlinx.android.synthetic.main.activity_sorting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.min

class SearchingActivity : AppCompatActivity(),SeekBar.OnSeekBarChangeListener {

    private val buttons: MutableList<MutableList<Button>> = ArrayList()
    //size of grid
    var size = 5
    //array to store numbers in array to be sorted
    var arrayToBeSearched:MutableList<Int> = ArrayList()
    //white color
    val whiteColor:String="#FFFFFF"
    //red color
    val redColor:String="#FF0000"
    val pinkColor:String="#ffdab9"
    //green color
    val greenColor:String="#228B22"
    val blueColor:String="#0000FF"
    var selected:Int=-1
    var numberShouldNotBeDublicate:MutableMap<Int,Int> = mutableMapOf()
    var searchAlgoSelectedValue:Int=0
    var arraySortedOrNot=0

    var delayTimeLong:Long=1000
    var delayTimeMedium:Long=400
    var delayTimeShort:Long=100
    var staticDelayTimeShort:Long=5000
    var staticDelayTimeMedium:Long=20000
    var staticdelayTimeLong:Long=50000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching)

        window.statusBarColor = resources.getColor(R.color.dark)

        setSupportActionBar(searchingToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        //seekbar added
        arraySizeSeekBar.setOnSeekBarChangeListener(this)
        speedChangeSeekBarSearching.setOnSeekBarChangeListener(this)
        speedChangeSeekBarSearching.setProgress(50)

        createButtonGrid(size)
        searchbtn.text = "Linear Search"

        randamizebtn.setOnClickListener {
            //cancelAllJobs()
            paintAllButtonsWhiteAgain(size)
            randamize(size)
        }
        searchbtn.setOnClickListener {
            if(selected!=-1) {
                if (searchAlgoSelectedValue == 0) {
                    linearSearch()
                }
                else if (searchAlgoSelectedValue == 1) {
                    makeAlertBox("Binary Search")
                }
                else if (searchAlgoSelectedValue == 2) {
                    makeAlertBox("Jump Search")
                }
                else if(searchAlgoSelectedValue==3){
                    makeAlertBox("Interpolation Search")
                }
                else if(searchAlgoSelectedValue==4){
                    makeAlertBox("Exponential Search")
                }
            }
            else if(selected==-1){
                Toast.makeText(this,"Select bar to be searched", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun makeAlertBox(searchingAlgoName:String) {
        if(arraySortedOrNot==0) {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Array Need To be Sorted")
            //set message for alert dialog
            builder.setMessage("$searchingAlgoName can only work on Sorted data. You need to sort the array first.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("SORT!") { dialogInterface, which ->
                Toast.makeText(applicationContext, "SORTING!!", Toast.LENGTH_LONG).show()

                if (searchAlgoSelectedValue == 1) {
                    GlobalScope.launch(Dispatchers.Main) {
                        binarySearch(0, size, selected)
                    }
                } else if (searchAlgoSelectedValue == 2) {
                    GlobalScope.launch(Dispatchers.Main) {
                        jumpSearch()
                    }
                } else if (searchAlgoSelectedValue == 3) {
                    GlobalScope.launch(Dispatchers.Main) {
                        interpolationSearch(0, size, selected)
                    }
                } else if (searchAlgoSelectedValue == 4) {
                    GlobalScope.launch(Dispatchers.Main) {
                        ExponentialSearch(selected)
                    }
                }
            }
            //performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                Toast.makeText(
                    applicationContext,
                    "Cancelled! choose diff algorithm",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //performing negative action

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
        else if(arraySortedOrNot==1){
            if (searchAlgoSelectedValue == 1) {
                GlobalScope.launch(Dispatchers.Main) {
                    binarySearch(0, size, selected)
                }
            } else if (searchAlgoSelectedValue == 2) {
                GlobalScope.launch(Dispatchers.Main) {
                    jumpSearch()
                }
            } else if (searchAlgoSelectedValue == 3) {
                GlobalScope.launch(Dispatchers.Main) {
                    interpolationSearch(0, size, selected)
                }
            } else if (searchAlgoSelectedValue == 4) {
                GlobalScope.launch(Dispatchers.Main) {
                    ExponentialSearch(selected)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.searching_algo_name, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.linearSearch -> {
                paintAllButtonsWhiteAgain(size)
                randamize(size)
                searchAlgoSelectedValue=0
                searchbtn.text="Linear Search"
                return true }
            R.id.binarySearch -> {
                paintAllButtonsWhiteAgain(size)
                randamize(size)
                searchAlgoSelectedValue=1
                searchbtn.text="Binary Search"
                return true }
            R.id.jumpSearch -> {
                paintAllButtonsWhiteAgain(size)
                randamize(size)
                searchAlgoSelectedValue=2
                searchbtn.text="Jump Search"
                return true }
            R.id.interpolationSearch -> {
                paintAllButtonsWhiteAgain(size)
                randamize(size)
                searchAlgoSelectedValue=3
                searchbtn.text="Interpolation Search"
                return true }
            R.id.exponentialSearch -> {
                paintAllButtonsWhiteAgain(size)
                randamize(size)
                searchAlgoSelectedValue=4
                searchbtn.text="Exponential Search"
                return true }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun linearSearch() {
        GlobalScope.launch(Dispatchers.IO)
        {
            if (selected == -1) {
                //Toast.makeText(this, "Element not selected", Toast.LENGTH_LONG).show()
            } else {
                for (i in 0..size) {
                    colorButton(i, arrayToBeSearched[i], redColor)
                    delay(delayTimeMedium)
                    paintSingleColWhite(i)
                    colorButton(i, arrayToBeSearched[i], greenColor)
                    if (arrayToBeSearched[i] == selected) {
                        //Toast.makeText(this, "Element found at $i", Toast.LENGTH_LONG).show()
                        colorButton(i, arrayToBeSearched[i], blueColor)
                        break;
                    }
                }
            }
        }
    }

    private suspend fun binarySearch(p: Int, q: Int, x: Int){
        if(arraySortedOrNot==0) {
            if (searchAlgoSelectedValue == 1) {
                var job = GlobalScope.launch(Dispatchers.Main) {
                    insertionSort()
                }
                job.join()
            }
        }
        GlobalScope.launch (Dispatchers.IO) {
            //Toast.makeText(this, "$p $q $x", Toast.LENGTH_LONG).show()
            var l = p
            var r = q
            while (l <= r) {
                val m = l + (r - l) / 2
                colorButton(m, arrayToBeSearched[m], pinkColor)
                delay(delayTimeLong)
                paintSingleColWhite(m)
                colorButton(m, arrayToBeSearched[m], greenColor)
                if (arrayToBeSearched[m] == x) {
                    colorButton(m, arrayToBeSearched[m], blueColor)
                    break
                }

                if (arrayToBeSearched[m] < x) {
                    l = m + 1
                    for (i in l..r){
                        colorButton(i, arrayToBeSearched[i], blueColor)
                    }
                    delay(delayTimeLong)
                    for (i in l..r){
                        colorButton(i, arrayToBeSearched[i], greenColor)
                    }
                } else {
                    r = m - 1
                    for (i in l..r){
                        colorButton(i, arrayToBeSearched[i], blueColor)
                    }
                    delay(delayTimeLong)
                    for (i in l..r){
                        colorButton(i, arrayToBeSearched[i], greenColor)
                    }
                }
            }
        }
    }

    private suspend fun jumpSearch() {
        if(arraySortedOrNot==0) {
            var job = GlobalScope.launch(Dispatchers.Main) {
                insertionSort()
            }
            job.join()
        }
        GlobalScope.launch(Dispatchers.Main) {
            val n = arrayToBeSearched.size

            // Finding block size to be jumped
            var step = Math.floor(Math.sqrt(n.toDouble())).toInt()

            // Finding the block where element is
            // present (if it is present)
            var prev = 0
            while (arrayToBeSearched[min(step, n) - 1] < selected) {
                colorButton(min(step, n) - 1, arrayToBeSearched[min(step, n) - 1], pinkColor)
                delay(delayTimeLong)
                paintSingleColWhite(min(step, n) - 1)
                colorButton(min(step, n) - 1, arrayToBeSearched[min(step, n) - 1], greenColor)
                prev = step
                step += floor(Math.sqrt(n.toDouble())).toInt()
                if (prev >= n)
                    break;
            }
            // Doing a linear search for x in block
            // beginning with prev.
            while (arrayToBeSearched[prev] < selected) {
                colorButton(prev, arrayToBeSearched[prev], pinkColor)
                delay(delayTimeLong)
                paintSingleColWhite(prev)
                colorButton(prev, arrayToBeSearched[prev], greenColor)
                prev++
            }

            if (arrayToBeSearched[prev] == selected)
            {
                colorButton(prev, arrayToBeSearched[prev], blueColor)
                delay(delayTimeLong)
            }
        }
    }

    private suspend fun interpolationSearch(p: Int, q: Int, x: Int){
        if(arraySortedOrNot==0) {
            var job = GlobalScope.launch(Dispatchers.Main) {
                insertionSort()
            }
            job.join()
        }
        GlobalScope.launch (Dispatchers.IO) {
            //Toast.makeText(this, "$p $q $x", Toast.LENGTH_LONG).show()
            var l = p
            var r = q
            while (l <= r) {
                val m = l + ((x-arrayToBeSearched[l])*(r-l))/(arrayToBeSearched[r]-arrayToBeSearched[l])
                colorButton(m, arrayToBeSearched[m], pinkColor)
                delay(delayTimeLong)
                paintSingleColWhite(m)
                colorButton(m, arrayToBeSearched[m], greenColor)
                if (arrayToBeSearched[m] == x) {
                    colorButton(m, arrayToBeSearched[m], blueColor)
                    break
                }

                if (arrayToBeSearched[m] < x) {
                    l = m + 1
                    for (i in l..r){
                        colorButton(i, arrayToBeSearched[i], blueColor)
                    }
                    delay(delayTimeLong)
                    for (i in l..r){
                        colorButton(i, arrayToBeSearched[i], greenColor)
                    }
                } else {
                    r = m - 1
                    for (i in l..r) {
                        colorButton(i, arrayToBeSearched[i], blueColor)
                    }
                    delay(delayTimeLong)
                    for (i in l..r) {
                        colorButton(i, arrayToBeSearched[i], greenColor)
                    }
                }
            }
        }
    }

    suspend fun ExponentialSearch(x:Int){
        if(arraySortedOrNot==0) {
            var job = GlobalScope.launch(Dispatchers.Main) {
                insertionSort()
            }
            job.join()
        }
        if (arrayToBeSearched[0] === x){
            colorButton(0, arrayToBeSearched[0], pinkColor)
            delay(delayTimeLong)
            paintSingleColWhite(0)
            colorButton(0, arrayToBeSearched[0], blueColor)
        }
        else {

            var i = 1
            while (i <=size && arrayToBeSearched[i] <= x) {
                i = i * 2
                colorButton(i/2,arrayToBeSearched[i/2],redColor)
                colorButton(min(i,size),arrayToBeSearched[min(i,size)],redColor)
                delay(delayTimeLong/2)
                colorButton(i/2,arrayToBeSearched[i/2],greenColor)
                colorButton(min(i,size),arrayToBeSearched[min(i,size)],greenColor)
            }


            binarySearch(i / 2, min(i, size), x)
        }
    }

    private suspend fun insertionSort(){
        var job = GlobalScope.launch (Dispatchers.Main )
        {
            for (i in 1..size) {
                // println(items)
                val item = arrayToBeSearched[i]
                var j = i - 1
                while (j >= 0 && arrayToBeSearched[j] > item) {
                    colorButton(j+1,arrayToBeSearched[j+1],redColor)
                    delay(delayTimeShort)
                    paintSingleColWhite(j + 1)
                    colorButton(j + 1, arrayToBeSearched[j], greenColor)
                    arrayToBeSearched[j + 1] = arrayToBeSearched[j]
                    j = j - 1
                }
                colorButton(j+1,arrayToBeSearched[j+1],redColor)
                delay(delayTimeShort)
                paintSingleColWhite(j + 1)
                colorButton(j + 1, item, greenColor)
                arrayToBeSearched[j + 1] = item
            }
            arraySortedOrNot = 1
        }
        job.join()
    }

    private fun paintSingleColWhite(col: Int) {
        for (i in 0..size){
            buttons[col][i].setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
    }

    private fun numberShouldNotBeDublicateIntializer(){
        for (i in 0..size){
            numberShouldNotBeDublicate.put(i,0)
        }
    }

    private fun randamize(size: Int) {
        arraySortedOrNot=0
        selected=-1
        numberShouldNotBeDublicate.clear()
        arrayToBeSearched.removeAll(arrayToBeSearched)
        numberShouldNotBeDublicateIntializer()
        for(col in 0..size)
        {
            var row = (0..size).random()
            if(numberShouldNotBeDublicate.get(row)==1){
                for (i in 0..size){
                    if(numberShouldNotBeDublicate.get(i)==0){
                        row=i
                        numberShouldNotBeDublicate.put(row,1)
                        break
                    }
                }
            }
            else{
                numberShouldNotBeDublicate.put(row,1)
            }

            arrayToBeSearched.add(row)
            colorButton(col,row,greenColor)
        }

    }

    private fun colorButton(col: Int, row: Int,color:String) {
        for (i in 0..row){
            buttons[col][i].isEnabled=true
            buttons[col][i].setOnClickListener {
                Toast.makeText(this, "${arrayToBeSearched[col]} is selected", Toast.LENGTH_SHORT).show()
                selected = arrayToBeSearched[col]
            }
            buttons[col][i].setBackgroundColor(Color.parseColor(color))
        }
        for (i in row+1..size)
        {
            buttons[col][i].isEnabled=false
        }
    }

    private fun createButtonGrid(size: Int) {
        // xml declared LIinear layout
        var screenid = resources.getIdentifier("screen", "id", packageName)
        val screen=findViewById<LinearLayout>(screenid)


        // new dynamically declared linear layout inside screen linearlayout so grid can be deleted at any time
        val mainscreen = LinearLayout(this)
        mainscreen.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        mainscreen.orientation = LinearLayout.HORIZONTAL
        var mainscreenid = resources.getIdentifier("mainscreen", "id", packageName)
        mainscreen.id=mainscreenid
        screen.addView(mainscreen)

        for (i in 0..size) {

            val arrayLinearLayout = LinearLayout(this)
            arrayLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1.0f
            )
            arrayLinearLayout.orientation = LinearLayout.VERTICAL
            arrayLinearLayout.setPadding(2,2,2,2)

            val buttoncol: MutableList<Button> = ArrayList()
            for (j in 0..size) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
                )
                buttoncol.add(button)
                arrayLinearLayout.addView(button)
            }

            buttons.add(buttoncol)

            mainscreen.addView(arrayLinearLayout)
        }
        paintAllButtonsWhiteAgain(size)
    }

    private fun paintAllButtonsWhiteAgain(size: Int) {
        for (i in 0..size){
            for (j in 0..size){
                buttons[i][j].setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }
        selected=-1
    }

    private fun deleteMainScreen() {
        var mainscreenid = resources.getIdentifier("mainscreen", "id", packageName)
        val mainscreen=findViewById<LinearLayout>(mainscreenid)
        (mainscreen.getParent() as ViewGroup).removeView(mainscreen)
        buttons.removeAll(buttons)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        if(seekBar==arraySizeSeekBar) {
            size=(progress/4)+5
            deleteMainScreen()
            createButtonGrid(size)
            randamize(size)
        }
        else if (seekBar==speedChangeSeekBarSearching){
            if(progress!=0) {
                delayTimeMedium = staticDelayTimeMedium / progress
                delayTimeShort = staticDelayTimeShort / progress
                delayTimeLong = staticdelayTimeLong / progress
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if(seekBar==arraySizeSeekBar)
            paintAllButtonsWhiteAgain(size)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}