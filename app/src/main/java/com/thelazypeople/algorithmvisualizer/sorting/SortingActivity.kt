package com.thelazypeople.algorithmvisualizer.sorting

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.activity_sorting.*
import kotlinx.coroutines.*

class SortingActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    //array to store all button 2d array
    val buttons: MutableList<MutableList<Button>> = ArrayList()
    //size of grid
    var size = 5
    //array to store numbers in array to be sorted
    var arrayToBeSorted:MutableList<Int> = ArrayList()
    //white color
    val whiteColor:String="#3b3f42"
    //red color
    val redColor:String="#FF0000"
    //green color
    val greenColor:String="#228B22"
    // sort val
    var sortval=0
    lateinit var jobBubbleSort: Job
    lateinit var jobInsertionSort: Job
    lateinit var jobQuickSort1: Job
    lateinit var jobQuickSort2: Job
    lateinit var jobQuickSort3: Job
    lateinit var jobSelectionSort: Job
    lateinit var jobMergeSort1: Job
    lateinit var jobMergeSort2: Job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorting)

        window.statusBarColor = resources.getColor(R.color.dark)

        //false job initiazation
        falseJobInit()

        //Toolbar added
        setSupportActionBar(sortingToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //seekbar added
        arraySizeSeekBar.setOnSeekBarChangeListener(this)


        //grid created for first time
        createButtonGrid(size)
        sortbtn.text = "Bubble Sort"
        //button[0][0].setBackgroundColor(Color.parseColor("#FF0000"))



        //randomize button listener
        randamizebtn.setOnClickListener {
            cancelAllJobs()
            paintAllButtonsWhiteAgain(size)
            randamize(size)

        }
        //sortbtn listener
        sortbtn.setOnClickListener {
            cancelAllJobs()

            when(sortval)
            {
                0 -> bubbleSort()
                1 -> selectionSort()
                2 -> mergeSort(arrayToBeSorted)
                3 -> insertionSort()
                4 -> quicksort(arrayToBeSorted,0,size)
            }
        }

    }

    private fun falseJobInit() {
        jobSelectionSort= GlobalScope.launch {  }
        jobQuickSort1= GlobalScope.launch {  }
        jobQuickSort2= GlobalScope.launch {  }
        jobQuickSort3= GlobalScope.launch {  }
        jobInsertionSort= GlobalScope.launch {  }
        jobBubbleSort= GlobalScope.launch {  }
        jobMergeSort1= GlobalScope.launch {  }
        jobMergeSort2= GlobalScope.launch {  }
    }

    private fun cancelAllJobs() {
        jobBubbleSort.cancel()
        jobInsertionSort.cancel()
        jobQuickSort1.cancel()
        jobQuickSort2.cancel()
        jobQuickSort3.cancel()
        jobSelectionSort.cancel()
        jobMergeSort1.cancel()
        jobMergeSort2.cancel()
    }

    // Menu toolbar component
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.bubble_sort -> { sortbtn.text="Bubble Sort"
                sortval=0
                return true }
            R.id.selection_sort -> { sortbtn.text="Selection Sort"
                sortval=1
                return true }
            R.id.merge_sort -> { sortbtn.text="Merge Sort"
                sortval=2
                return true }
            R.id.insertion_sort -> { sortbtn.text="Insertion Sort"
                sortval=3
                return true }
            R.id.quick_sort -> { sortbtn.text="Quick Sort"
                sortval=4
                return true }
        }
        return super.onOptionsItemSelected(item)
    }
    //bubble sort
    private fun bubbleSort(){
        jobBubbleSort= GlobalScope.launch (Dispatchers.Main )
        {
            var swap = true
            while (swap) {
                swap = false
                for (i in 0 until arrayToBeSorted.size - 1) {
                    if (arrayToBeSorted[i] > arrayToBeSorted[i + 1]) {
                        //delay(100)
                        replaceTwoColInGrid(i, i + 1)
                        delay(200)
                        val temp = arrayToBeSorted[i]
                        arrayToBeSorted[i] = arrayToBeSorted[i + 1]
                        arrayToBeSorted[i + 1] = temp
                        swap = true
                    }
                }
            }
        }
    }
    //selection sort
    private fun selectionSort(){
        jobSelectionSort= GlobalScope.launch (Dispatchers.Main )
        {
            var n = arrayToBeSorted.size
            var temp: Int
            for (i in 0..n - 1) {
                var indexOfMin = i
                for (j in n - 1 downTo i) {
                    if (arrayToBeSorted[j] < arrayToBeSorted[indexOfMin])
                        indexOfMin = j
                }
                if (i != indexOfMin) {
                    replaceTwoColInGrid(i, indexOfMin)
                    delay(400)
                    temp = arrayToBeSorted[i]
                    arrayToBeSorted[i] = arrayToBeSorted[indexOfMin]
                    arrayToBeSorted[indexOfMin] = temp
                }
            }
        }
    }
    //merge sort component
    fun mergeSort(list: MutableList<Int>){
        GlobalScope.launch (Dispatchers.Main) {
            merger(list)
        }
    }
    suspend fun merger(list: MutableList<Int>): MutableList<Int> {

        if (list.size <= 1) {
            return list
        }
        val middle = list.size / 2
        var left = list.subList(0,middle);
        var right = list.subList(middle,list.size);
        var lleft:MutableList<Int> = mutableListOf()
        var lright:MutableList<Int> = mutableListOf()
        jobMergeSort1= GlobalScope.launch(Dispatchers.Main) {
            lleft = merger(left)
        }
        jobMergeSort1.join()
        jobMergeSort2= GlobalScope.launch(Dispatchers.Main) {
            lright = merger(right)
        }
        jobMergeSort2.join()


        var indexLeft = 0
        var indexRight = 0
        var newList : MutableList<Int> = mutableListOf()
        var i=0
        while (indexLeft < lleft.count() && indexRight < lright.count()) {
            if (lleft[indexLeft] <= lright[indexRight]) {
                paintSingleColWhite(i)
                colorButton(i,lleft[indexLeft],redColor)
                delay(200)
                colorButton(i,lleft[indexLeft],greenColor)
                i++
                newList.add(lleft[indexLeft])
                indexLeft++
            } else {
                paintSingleColWhite(i)
                colorButton(i,lright[indexRight],redColor)
                delay(200)
                colorButton(i,lright[indexRight],greenColor)
                i++
                newList.add(lright[indexRight])
                indexRight++
            }
        }

        while (indexLeft < lleft.size) {
            paintSingleColWhite(i)
            colorButton(i,lleft[indexLeft],redColor)
            delay(200)
            colorButton(i,lleft[indexLeft],greenColor)
            i++
            newList.add(lleft[indexLeft])
            indexLeft++
        }

        while (indexRight < lright.size) {
            paintSingleColWhite(i)
            colorButton(i,lright[indexRight],redColor)
            delay(200)
            colorButton(i,lright[indexRight],greenColor)
            i++
            newList.add(lright[indexRight])
            indexRight++
        }

        return newList;
    }

    //insertion sort
    private fun insertionSort(){
        jobInsertionSort= GlobalScope.launch (Dispatchers.Main )
        {
            for (i in 1..size) {
                // println(items)
                val item = arrayToBeSorted[i]
                var j = i - 1
                while (j >= 0 && arrayToBeSorted[j] > item) {
                    colorButton(j+1,arrayToBeSorted[j+1],redColor)
                    delay(100)
                    paintSingleColWhite(j + 1)
                    colorButton(j + 1, arrayToBeSorted[j], greenColor)
                    arrayToBeSorted[j + 1] = arrayToBeSorted[j]
                    j = j - 1
                }
                colorButton(j+1,arrayToBeSorted[j+1],redColor)
                delay(100)
                paintSingleColWhite(j + 1)
                colorButton(j + 1, item, greenColor)
                arrayToBeSorted[j + 1] = item
            }
        }
    }
    //quick sort
    private fun quicksort(A: MutableList<Int>, p: Int, r: Int) {
        jobQuickSort1= GlobalScope.launch(Dispatchers.Main) {
            if (p < r) {
                var q: Int = partition(A, p, r)
                quicksort(A, p, q - 1)
                quicksort(A, q + 1, r)
            }
        }
    }
    //quick sort component
    suspend fun partition(A: MutableList<Int>, p: Int, r: Int): Int {
        var i=0
        jobQuickSort2= GlobalScope.launch(Dispatchers.Main) {
            var x = A[r]
            i = p - 1
            for (j in p until r) {
                if (A[j] <= x) {
                    i++
                    exchange(A, i, j)
                }
            }
            exchange(A, i + 1, r)
        }
        jobQuickSort2.join()
        return i + 1
    }
    //quick sort component
    suspend fun exchange(A: MutableList<Int>, i: Int, j: Int) {
        jobQuickSort3= GlobalScope.launch(Dispatchers.Main) {
            replaceTwoColInGrid(i, j)
            var temp = A[i]
            A[i] = A[j]
            A[j] = temp
        }
        jobQuickSort3.join()
    }
    // replaces 2 coloumn in the grid
    private suspend fun replaceTwoColInGrid(a: Int, b: Int) {
        val job= GlobalScope.launch(Dispatchers.Main) {
            colorButton(a, arrayToBeSorted[a], redColor)
            colorButton(b, arrayToBeSorted[b], redColor)
            delay(200)
            paintSingleColWhite(a)
            paintSingleColWhite(b)
            colorButton(a, arrayToBeSorted[b], greenColor)
            colorButton(b, arrayToBeSorted[a], greenColor)
        }
        job.join()
    }

    //make all the buttons white color
    private fun paintAllButtonsWhiteAgain(size: Int) {
        for (i in 0..size){
            for (j in 0..size){
                buttons[i][j].setBackgroundColor(resources.getColor(R.color.lightDark))
            }
        }
    }
    // fill the array with random numbers
    private fun randamize(size: Int) {
        arrayToBeSorted.removeAll(arrayToBeSorted)
        for(col in 0..size)
        {
            val row = (0..size).random()
            arrayToBeSorted.add(row)
            colorButton(col,row,greenColor)
        }
    }
    // color a coloumn of grid till a specific row
    private fun colorButton(col: Int, row: Int,color:String) {
        for (i in 0..row){
            buttons[col][i].setBackgroundColor(Color.parseColor(color))
        }
    }
    // make a single coloumn of grid white
    private fun paintSingleColWhite(col: Int) {
        for (i in 0..size){
            buttons[col][i].setBackgroundColor(resources.getColor(R.color.lightDark))
        }
    }
    // create grid of size - parameter size
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
    //delete the dynamically created mainscreen linearlayout and clear the button array
    private fun deleteMainScreen() {
        var mainscreenid = resources.getIdentifier("mainscreen", "id", packageName)
        val mainscreen=findViewById<LinearLayout>(mainscreenid)
        (mainscreen.getParent() as ViewGroup).removeView(mainscreen)
        buttons.removeAll(buttons)
    }
    //seekbar function
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        size=(progress/4)+5
        deleteMainScreen()
        createButtonGrid(size)
        randamize(size)
    }
    //seekbar function
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        cancelAllJobs()
        paintAllButtonsWhiteAgain(size)
    }

    //seekbar function
    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
    //Menu Toolbar Component
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.sorting_name, menu)
        return true
    }
}