package com.thelazypeople.algorithmvisualizer.pathfinder

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.thelazypeople.algorithmvisualizer.R
import com.varunest.sparkbutton.SparkButton
import com.varunest.sparkbutton.SparkButtonBuilder
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.activity_path_finder.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class PathFinderActivity : AppCompatActivity(),SeekBar.OnSeekBarChangeListener {
    val buttonStatusKeeper: MutableList<MutableMap<SparkButton, Int>> = ArrayList()
    val buttons: MutableList<MutableList<SparkButton>> = ArrayList()
    val size = 10
    val sizeb = 20
    var startStatusKeeper: Int = 0
    var endStatusKeeper: Int = 0
    var butsrcx: Int = -1
    var butsrcy: Int = -1
    var butdesx: Int = -1
    var butdesy: Int = -1
    var buttonWeightStatus = 0

    val gdForRedColor: GradientDrawable = GradientDrawable()
    val gdForGreenColor: GradientDrawable = GradientDrawable()
    val gdForBrownColor: GradientDrawable = GradientDrawable()
    val gdForWhiteColor: GradientDrawable = GradientDrawable()
    val gdForBlueColor: GradientDrawable = GradientDrawable()


    var v: MutableList<MutableList<MutableList<MutableList<Int>>>> = mutableListOf()
    var dis: MutableList<MutableList<Int>> = mutableListOf()
    var path: MutableList<MutableList<MutableList<MutableList<Int>>>> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.N)
    var pq: PriorityQueue<Tuple2> = PriorityQueue<Tuple2>(ComparatorTuple)
    var weight: MutableList<MutableList<Int>> = mutableListOf()
    var sized: Int = 0
    var srcx: Int = 0
    var srcy: Int = 0
    var desx: Int = -1
    var desy: Int = -1

    var vis: MutableList<MutableList<Int>> = mutableListOf()
    var dfsPath: MutableList<MutableList<Int>> = mutableListOf()

    var algoChooseValue=0

    var ywallInvalid:MutableList<Int> = mutableListOf()
    var xwallInvalid:MutableList<Int> = mutableListOf()

    var gridButtonActiveOrNot=0

    var bfsqueue: Queue<Tuple2> = LinkedList<Tuple2>()

    var delayTimeShort:Long=80
    var delayTimeMedium:Long=300
    var staticDelayTimeShort:Long=4000
    var staticDelayTimeMedium:Long=15000

    var isSearchRunning=0


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_finder)

        window.statusBarColor = resources.getColor(R.color.dark)

        //Toolbar
        setSupportActionBar(pathFinderToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        speedChangeSeekBarPathfinder.setOnSeekBarChangeListener(this)
        speedChangeSeekBarPathfinder.setProgress(50)

        gradientDrawableValueSetter()
        createButtonGrid()
        search.text="Dijkstra"

        //paintAllButtonsWhite()
        search.setOnClickListener {
            if (startStatusKeeper == 0)
                Toast.makeText(this, "Select Starting Node!!", Toast.LENGTH_LONG).show()
            else if (endStatusKeeper == 0)
                Toast.makeText(this, "Select Ending Node!!", Toast.LENGTH_LONG).show()
            else {
                if(algoChooseValue==0) {
                    GlobalScope.launch(Dispatchers.Main) {
                        findPathdijikstra()
                    }
                }
                else if(algoChooseValue==1)
                {
                    GlobalScope.launch(Dispatchers.Main) {
                        findPathDFS()
                    }
                }
                else if(algoChooseValue==2)
                {
                    GlobalScope.launch(Dispatchers.Main) {
                        findPathBFS()
                    }
                }
                else if(algoChooseValue==3){
                    GlobalScope.launch(Dispatchers.Main) {
                        findPathAStar()
                    }
                }
            }
        }
        weight_btn.setOnClickListener {
            if (buttonWeightStatus == 0) {
                weight_btn.text = "BLOCK"
                buttonWeightStatus = 1
            } else {
                weight_btn.text = "WEIGHT"
                buttonWeightStatus = 0
            }
        }

        clearbut.setOnClickListener {
            clearGrid()
        }


        mazebut.setOnClickListener {
            if (startStatusKeeper == 0) {
                clearGrid()
                GlobalScope.launch(Dispatchers.Main) {
                    recursiveDivisionMaze(0, sizeb, 0, size)
                }
            } else
                Toast.makeText(this, "Maze can only be generated in begining", Toast.LENGTH_LONG)
                    .show()
        }

    }

    private fun createRandomMaze() {
        for (k in 0..100) {
            var i = (0..sizeb).random()
            var j = (0..size).random()
            buttonStatusKeeper[i].put(buttons[i][j], 1)
            buttons[i][j].setInactiveImage(R.drawable.ic_mathematics)
            buttons[i][j].setActiveImage(R.drawable.ic_mathematics)
            buttons[i][j].playAnimation()
        }
    }

    suspend fun recursiveDivisionMaze(xs:Int,xe:Int,ys:Int,ye:Int){
        gridButtonActiveOrNot=1
        if(abs(xe-xs) >= abs(ye-ys)){
            if(abs(xs-xe) >=3) {
                var xwall: Int = ((xs+1)..(xe-1)).random()
                Log.i("walls","xinvalid")
                for (i in 0..xwallInvalid.size-1){
                    Log.i("walls",xwallInvalid.elementAt(i).toString())
                    if(xwall==xwallInvalid[i])
                    {
                        if(xwall==xe-1){
                            xwall=xwall-1
                        }
                        else{
                            xwall=xwall+1
                        }
                    }
                }
                Log.i("walls","xwall"+xwall.toString())
                var clear = (ys..ye).random()
                ywallInvalid.add(clear)
                for (i in ys..ye) {
                    if(i!=clear) {
                        buttonStatusKeeper[xwall].put(buttons[xwall][i], 1)
                        buttons[xwall][i].setInactiveImage(R.drawable.ic_mathematics)
                        buttons[xwall][i].setActiveImage(R.drawable.ic_mathematics)
                        buttons[xwall][i].playAnimation()
                    }

                }
                delay(delayTimeShort)
                var job1=GlobalScope.launch(Dispatchers.Main) {

                    recursiveDivisionMaze(xs, xwall - 1, ys, ye)
                }
                job1.join()
                var job2=GlobalScope.launch(Dispatchers.Main) {

                    recursiveDivisionMaze(xwall + 1, xe, ys, ye)
                }
                job2.join()
                ywallInvalid.remove(clear)
            }
        }
        else{
            if(abs(ye-ys) >=4) {
                var ywall: Int = ((ys+1)..(ye-1)).random()
                Log.i("walls","yinvalid")
                for (i in 0..ywallInvalid.size-1){
                    Log.i("walls",ywallInvalid.elementAt(i).toString())
                    if(ywall==ywallInvalid.elementAt(i)){
                        if (ywall==(ye-1)){
                            ywall=ywall-1
                        }
                        else{
                            ywall=ywall+1
                        }
                    }
                }
                Log.i("walls","ywall"+ywall.toString())
                var clear = (xs..xe).random()
                xwallInvalid.add(clear)
                for (i in xs..xe) {
                    if(i!=clear) {
                        buttonStatusKeeper[i].put(buttons[i][ywall], 1)
                        buttons[i][ywall].setInactiveImage(R.drawable.ic_mathematics)
                        buttons[i][ywall].setActiveImage(R.drawable.ic_mathematics)
                        buttons[i][ywall].playAnimation()
                    }

                }
                delay(delayTimeShort)
                var job1=GlobalScope.launch(Dispatchers.Main) {
                    recursiveDivisionMaze(xs, xe, ys, ywall - 1)
                }
                job1.join()
                var job2=GlobalScope.launch(Dispatchers.Main) {
                    recursiveDivisionMaze(xs, xe, ywall + 1, ye)
                }
                job2.join()
                xwallInvalid.remove(clear)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.path_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isSearchRunning==0) {
            when (item.itemId) {

                R.id.id_dijkstra -> {
                    //dijkstra()
                    algoChooseValue = 0
                    search.text = "Dijkstra"
                    clearGrid()
                    return true
                }
                R.id.id_dfs -> {
                    //dfs()
                    algoChooseValue = 1
                    search.text = "DFS"
                    weight_btn.isClickable = false
                    clearGrid()
                    return true
                }
                R.id.id_bfs -> {
                    //bfs()
                    algoChooseValue = 2
                    search.text = "BFS"
                    weight_btn.isClickable = false
                    clearGrid()
                    return true
                }
                R.id.id_astar -> {
                    //astar()
                    algoChooseValue = 3
                    weight_btn.isClickable = true
                    search.text = "AStar"
                    clearGrid()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearGrid() {
        var screenid = resources.getIdentifier("screen", "id", packageName)
        val screen = findViewById<LinearLayout>(screenid)
        (screen.getParent() as ViewGroup).removeView(screen)
        buttons.removeAll(buttons)
        buttonStatusKeeper.removeAll(buttonStatusKeeper)
        startStatusKeeper = 0
        endStatusKeeper = 0
        butsrcx = -1
        butsrcy = -1
        butdesx = -1
        butdesy = -1
        buttonWeightStatus = 0
        v.removeAll(v)
        dis.removeAll(dis)
        path.removeAll(path)
        weight.removeAll(weight)
        sized = 0
        srcx = 0
        srcy = 0
        desx = -1
        desy = -1
        createButtonGrid()
        vis.removeAll(vis)
        dfsPath.removeAll(dfsPath)
        search.isClickable = true
        pq.removeAll(pq)
        bfsqueue.removeAll(bfsqueue)
        if(algoChooseValue==0)
            weight_btn.isClickable = true
        if(algoChooseValue==3)
            weight_btn.isClickable = true
        gridButtonActiveOrNot=0
        isSearchRunning=0

    }

    suspend fun dfs(x: Int, y: Int): Boolean {
        if (vis[x][y] == 0) {
            buttons[x][y].setInactiveImage(R.drawable.ic_mathematics_blue)
            buttons[x][y].playAnimation()
            delay(delayTimeShort)
            vis[x][y] = 1
            if (x == desx) {
                if (y == desy) {
                    var point: MutableList<Int> = mutableListOf()
                    point.add(x)
                    point.add(y)
                    dfsPath.add(point)
                    return true
                }
            }
            for (i in 0..(v[x][y].size - 1)) {
                var returner_val = false
                var job1 = GlobalScope.launch(Dispatchers.Main) {
                    returner_val = dfs(v[x][y][i][0], v[x][y][i][1])
                }
                job1.join()
                if (returner_val == true) {
                    var point: MutableList<Int> = mutableListOf()
                    point.add(x)
                    point.add(y)
                    dfsPath.add(point)
                    return true
                }
            }
        }
        return false
    }

    suspend fun findPathDFS() {
        isSearchRunning=1
        gridButtonActiveOrNot=1
        search.isClickable = false
        clearbut.isClickable = false
        for (i in 0..(sizeb)) {
            var row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(size)) {
                var point: MutableList<MutableList<Int>> = mutableListOf()
                if (i == 0 && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)

                    point.add(neigh2)

                } else if (i == sizeb && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)

                    point.add(neigh2)

                } else if (i == 0 && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)

                } else if (i == sizeb && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)

                } else if (i == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (i == sizeb) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)

                    point.add(neigh3)

                } else {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    point.add(neigh3)
                    var neigh4: MutableList<Int> = mutableListOf()

                    neigh4.add(i)
                    neigh4.add(j + 1)

                    point.add(neigh4)

                }
                row.add(point)
            }
            v.add(row)
        }

        for (i in 0..(v.size - 1)) {
            var visvec: MutableList<Int> = mutableListOf()
            for (j in 0..(v[i].size - 1)) {

                visvec.add(0)
            }
            vis.add(visvec)
        }

        for (i in 0..sizeb) {
            for (j in 0..size) {
                if (buttonStatusKeeper[i].get(buttons[i][j]) == 1) {
                    vis[i][j] = 1
                }
            }
        }


        srcx = butsrcx
        srcy = butsrcy
        desx = butdesx
        desy = butdesy
        var job2 = GlobalScope.launch(Dispatchers.Main) {
            dfs(srcx, srcy)
        }
        job2.join()

        for (i in (dfsPath.size - 2) downTo 1) {
            buttons[dfsPath[i][0]][dfsPath[i][1]].setInactiveImage(R.drawable.ic_mathematics_green)
            buttons[dfsPath[i][0]][dfsPath[i][1]].setActiveImage(R.drawable.ic_mathematics_green)
            buttons[dfsPath[i][0]][dfsPath[i][1]].playAnimation()
            delay(delayTimeShort)

        }
        if (dfsPath.size == 0) {
            Snackbar.make(parentLayout,"NO PATH FOUND!!",Snackbar.LENGTH_SHORT).show()
        }
        clearbut.isClickable = true
        isSearchRunning=0
    }

    fun weightMaker() {
        for (i in 0..(sizeb)) {
            var weightvec: MutableList<Int> = mutableListOf()
            for (j in 0..(size)) {
                weightvec.add(1)
            }
            weight.add(weightvec)
        }
    }

    suspend fun dijikstra() {
        //buttons[0][0].setBackgroundColor(Color.parseColor("#000000"))
        for (i in 0..(sizeb)) {
            var row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(size)) {
                var point: MutableList<MutableList<Int>> = mutableListOf()
                if (i == 0 && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)
                    neigh2.add(weight[i][j + 1])
                    point.add(neigh2)

                } else if (i == sizeb && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)
                    neigh2.add(weight[i][j + 1])
                    point.add(neigh2)

                } else if (i == 0 && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)

                } else if (i == sizeb && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)

                } else if (i == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (i == sizeb) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    neigh3.add(weight[i][j - 1])
                    point.add(neigh3)

                } else {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    neigh3.add(weight[i][j - 1])
                    point.add(neigh3)
                    var neigh4: MutableList<Int> = mutableListOf()

                    neigh4.add(i)
                    neigh4.add(j + 1)
                    neigh4.add(weight[i][j + 1])
                    point.add(neigh4)

                }
                row.add(point)
            }
            v.add(row)
        }


        for (i in 0..(v.size - 1)) {
            var row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(v[i].size - 1)) {
                var p: MutableList<MutableList<Int>> = mutableListOf()
                row.add(p)
            }
            path.add(row)
        }

        for (i in 0..(v.size - 1)) {
            var disvec: MutableList<Int> = mutableListOf()
            for (j in 0..(v[i].size - 1)) {

                disvec.add(500)
            }
            dis.add(disvec)
        }


        var temp = Tuple2(0, srcx, srcy)
        pq.add(temp)
        dis[srcx][srcy] = 0
        while (!pq.isEmpty()) {
            var u = pq.peek()
            pq.remove()
            var x = u.x
            var y = u.y
            var d = u.d
            //tester.append(x.toString()+" "+y.toString()+"\n")
            if ((x == desx) and (y == desy)) {
                break
            }
            for (i in 0..(v[x][y].size - 1)) {

                if (dis[v[x][y][i][0]][v[x][y][i][1]] > ((dis[x][y]) + (v[x][y][i][2]))) {
                    if ((v[x][y][i][0] != desx) or (v[x][y][i][1] != desy)) {
                        if (weight[v[x][y][i][0]][v[x][y][i][1]] == 1) {
                            buttons[v[x][y][i][0]][v[x][y][i][1]].setInactiveImage(R.drawable.ic_mathematics_blue)
                            buttons[v[x][y][i][0]][v[x][y][i][1]].playAnimation()
                            delay(delayTimeShort)
                        }

                    }
                    dis[v[x][y][i][0]][v[x][y][i][1]] = ((dis[x][y]) + (v[x][y][i][2]))

                    path[v[x][y][i][0]][v[x][y][i][1]].removeAll(path[v[x][y][i][0]][v[x][y][i][1]])

                    path[v[x][y][i][0]][v[x][y][i][1]] =
                        mutableListOf<MutableList<Int>>().apply { addAll(path[x][y]) }
                    var tem: MutableList<Int> = mutableListOf()
                    tem.add(x)
                    tem.add(y)
                    path[v[x][y][i][0]][v[x][y][i][1]].add(tem)
                    var dd: Int = dis[v[x][y][i][0]][v[x][y][i][1]]
                    var xx: Int = v[x][y][i][0]
                    var yy: Int = v[x][y][i][1]
                    var temp2 = Tuple2(dd, xx, yy)
                    pq.add(temp2)
                }
            }
        }
    }

    private suspend fun findPathdijikstra() {
        isSearchRunning=1
        gridButtonActiveOrNot=1
        search.isClickable = false
        weight_btn.isClickable = false
        clearbut.isClickable = false
        sized = size + 1
        srcx = butsrcx
        srcy = butsrcy
        desx = butdesx
        desy = butdesy
        var job2 = GlobalScope.launch(Dispatchers.Main) {
            weightMaker()
        }
        job2.join()
        for (i in 0..sizeb) {
            for (j in 0..size) {
                if (buttonStatusKeeper[i].get(buttons[i][j]) == 1) {
                    weight[i][j] = 1000
                } else if (buttonStatusKeeper[i].get(buttons[i][j]) == 2) {
                    weight[i][j] = 5
                }
            }
        }
        var job1 = GlobalScope.launch(Dispatchers.Main) {
            dijikstra()
        }
        job1.join()
        var pather = path
        //tester.append(srcx.toString()+" "+srcy.toString()+butsrcx.toString()+" "+butsrcy.toString()+"\n")
        //tester.append(desx.toString()+" "+desy.toString()+butdesx.toString()+" "+butdesy.toString()+"\n")
        for (i in 1..(pather[butdesx][butdesy].size - 1)) {
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setInactiveImage(
                R.drawable.ic_mathematics_green
            )
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setActiveImage(
                R.drawable.ic_mathematics_green
            )
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].playAnimation()
            delay(delayTimeMedium)

            //tester.append(pather[butdesx][butdesy][i][0].toString()+" "+pather[butdesx][butdesy][i][1].toString()+"\n")

        }
        if (pather[butdesx][butdesy].size == 0) {
            Snackbar.make(parentLayout,"NO PATH FOUND!!",Snackbar.LENGTH_SHORT).show()
        }
        clearbut.isClickable = true
        isSearchRunning=0
    }

    suspend fun AStar(){
        for (i in 0..(sizeb)) {
            var row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(size)) {
                var point: MutableList<MutableList<Int>> = mutableListOf()
                if (i == 0 && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)
                    neigh2.add(weight[i][j + 1])
                    point.add(neigh2)

                } else if (i == sizeb && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)
                    neigh2.add(weight[i][j + 1])
                    point.add(neigh2)

                } else if (i == 0 && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)

                } else if (i == sizeb && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)

                } else if (i == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (i == sizeb) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    neigh3.add(weight[i][j - 1])
                    point.add(neigh3)

                } else {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    neigh3.add(weight[i][j - 1])
                    point.add(neigh3)
                    var neigh4: MutableList<Int> = mutableListOf()

                    neigh4.add(i)
                    neigh4.add(j + 1)
                    neigh4.add(weight[i][j + 1])
                    point.add(neigh4)

                }
                row.add(point)
            }
            v.add(row)
        }


        for (i in 0..(v.size - 1)) {
            var row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(v[i].size - 1)) {
                var p: MutableList<MutableList<Int>> = mutableListOf()
                row.add(p)
            }
            path.add(row)
        }

        for (i in 0..(v.size - 1)) {
            var disvec: MutableList<Int> = mutableListOf()
            for (j in 0..(v[i].size - 1)) {

                disvec.add(500)
            }
            dis.add(disvec)
        }


        var temp = Tuple2(0, srcx, srcy)
        pq.add(temp)
        dis[srcx][srcy] = 0
        var heristicValueForAStar=0
        while (!pq.isEmpty()) {
            var u = pq.peek()
            pq.remove()
            var x = u.x
            var y = u.y
            var d = u.d
            //tester.append(x.toString()+" "+y.toString()+"\n")
            if ((x == desx) and (y == desy)) {
                break
            }
            for (i in 0..(v[x][y].size - 1)) {

                if (dis[v[x][y][i][0]][v[x][y][i][1]] > ((dis[x][y]) + (v[x][y][i][2]))) {
                    if ((v[x][y][i][0] != desx) or (v[x][y][i][1] != desy)) {
                        if (weight[v[x][y][i][0]][v[x][y][i][1]] == 1) {
                            buttons[v[x][y][i][0]][v[x][y][i][1]].setInactiveImage(R.drawable.ic_mathematics_blue)
                            buttons[v[x][y][i][0]][v[x][y][i][1]].playAnimation()
                            delay(delayTimeShort)
                        }

                    }
                    dis[v[x][y][i][0]][v[x][y][i][1]] = ((dis[x][y]) + (v[x][y][i][2]))

                    path[v[x][y][i][0]][v[x][y][i][1]].removeAll(path[v[x][y][i][0]][v[x][y][i][1]])

                    path[v[x][y][i][0]][v[x][y][i][1]] =
                        mutableListOf<MutableList<Int>>().apply { addAll(path[x][y]) }
                    var tem: MutableList<Int> = mutableListOf()
                    tem.add(x)
                    tem.add(y)
                    path[v[x][y][i][0]][v[x][y][i][1]].add(tem)
                    heristicValueForAStar= abs((v[x][y][i][0])-desx) + abs((v[x][y][i][1])-desy)
                    var dd: Int = dis[v[x][y][i][0]][v[x][y][i][1]]+heristicValueForAStar
                    var xx: Int = v[x][y][i][0]
                    var yy: Int = v[x][y][i][1]
                    var temp2 = Tuple2(dd, xx, yy)
                    pq.add(temp2)
                }
            }
        }
    }

    suspend fun findPathAStar(){
        isSearchRunning=1
        gridButtonActiveOrNot=1
        search.isClickable = false
        weight_btn.isClickable = false
        clearbut.isClickable = false
        sized = size + 1
        srcx = butsrcx
        srcy = butsrcy
        desx = butdesx
        desy = butdesy
        var job2 = GlobalScope.launch(Dispatchers.Main) {
            weightMaker()
        }
        job2.join()
        for (i in 0..sizeb) {
            for (j in 0..size) {
                if (buttonStatusKeeper[i].get(buttons[i][j]) == 1) {
                    weight[i][j] = 1000
                } else if (buttonStatusKeeper[i].get(buttons[i][j]) == 2) {
                    weight[i][j] = 5
                }
            }
        }
        var job1 = GlobalScope.launch(Dispatchers.Main) {
            AStar()
        }
        job1.join()
        var pather = path
        //tester.append(srcx.toString()+" "+srcy.toString()+butsrcx.toString()+" "+butsrcy.toString()+"\n")
        //tester.append(desx.toString()+" "+desy.toString()+butdesx.toString()+" "+butdesy.toString()+"\n")
        for (i in 1..(pather[butdesx][butdesy].size - 1)) {
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setInactiveImage(
                R.drawable.ic_mathematics_green
            )
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setActiveImage(
                R.drawable.ic_mathematics_green
            )
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].playAnimation()
            delay(delayTimeMedium)

            //tester.append(pather[butdesx][butdesy][i][0].toString()+" "+pather[butdesx][butdesy][i][1].toString()+"\n")

        }
        if (pather[butdesx][butdesy].size == 0) {
            Snackbar.make(parentLayout,"NO PATH FOUND!!",Snackbar.LENGTH_SHORT).show()
        }
        clearbut.isClickable = true
        isSearchRunning=0
    }

    suspend fun bfs(){
        for (i in 0..(sizeb)) {
            var row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(size)) {
                var point: MutableList<MutableList<Int>> = mutableListOf()
                if (i == 0 && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)

                    point.add(neigh2)

                } else if (i == sizeb && j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)

                    point.add(neigh2)

                } else if (i == 0 && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)

                } else if (i == sizeb && j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)

                } else if (i == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (i == sizeb) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (j == 0) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    var neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (j == size) {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)

                    point.add(neigh3)

                } else {
                    var neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    var neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    var neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    point.add(neigh3)
                    var neigh4: MutableList<Int> = mutableListOf()

                    neigh4.add(i)
                    neigh4.add(j + 1)

                    point.add(neigh4)

                }
                row.add(point)
            }
            v.add(row)
        }


        for (i in 0..(v.size - 1)) {
            var row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(v[i].size - 1)) {
                var p: MutableList<MutableList<Int>> = mutableListOf()
                row.add(p)
            }
            path.add(row)
        }

        for (i in 0..(v.size - 1)) {
            var disvec: MutableList<Int> = mutableListOf()
            for (j in 0..(v[i].size - 1)) {

                disvec.add(500)
            }
            dis.add(disvec)
        }


        var temp = Tuple2(0, srcx, srcy)
        bfsqueue.add(temp)
        dis[srcx][srcy] = 0
        while (!bfsqueue.isEmpty()) {
            var u = bfsqueue.peek()
            bfsqueue.remove()
            var x = u.x
            var y = u.y
            var d = u.d
            //tester.append(x.toString()+" "+y.toString()+"\n")
            if ((x == desx) and (y == desy)) {
                break
            }
            for (i in 0..(v[x][y].size - 1)) {
                if(vis[v[x][y][i][0]][v[x][y][i][1]]==0) {
                    vis[v[x][y][i][0]][v[x][y][i][1]]=1

                    if ((v[x][y][i][0] != desx) or (v[x][y][i][1] != desy)) {

                        buttons[v[x][y][i][0]][v[x][y][i][1]].setInactiveImage(R.drawable.ic_mathematics_blue)
                        buttons[v[x][y][i][0]][v[x][y][i][1]].playAnimation()
                        delay(delayTimeShort)
                    }
                    dis[v[x][y][i][0]][v[x][y][i][1]] = ((dis[x][y]) + 1)

                    path[v[x][y][i][0]][v[x][y][i][1]].removeAll(path[v[x][y][i][0]][v[x][y][i][1]])

                    path[v[x][y][i][0]][v[x][y][i][1]] =
                        mutableListOf<MutableList<Int>>().apply { addAll(path[x][y]) }
                    var tem: MutableList<Int> = mutableListOf()
                    tem.add(x)
                    tem.add(y)
                    path[v[x][y][i][0]][v[x][y][i][1]].add(tem)
                    var dd: Int = dis[v[x][y][i][0]][v[x][y][i][1]]
                    var xx: Int = v[x][y][i][0]
                    var yy: Int = v[x][y][i][1]
                    var temp2 = Tuple2(0, xx, yy)
                    bfsqueue.add(temp2)
                }

            }
        }
    }

    suspend fun findPathBFS(){
        isSearchRunning=1
        gridButtonActiveOrNot=1
        search.isClickable = false
        weight_btn.isClickable = false
        clearbut.isClickable = false

        srcx = butsrcx
        srcy = butsrcy
        desx = butdesx
        desy = butdesy
        for (i in 0..sizeb) {
            var visvec: MutableList<Int> = mutableListOf()
            for (j in 0..size) {
                visvec.add(0)
            }
            vis.add(visvec)
        }
        for (i in 0..sizeb) {
            for (j in 0..size) {
                if (buttonStatusKeeper[i].get(buttons[i][j]) == 1) {
                    vis[i][j] = 1
                }
            }
        }
        var job1 = GlobalScope.launch(Dispatchers.Main) {
            bfs()
        }
        job1.join()
        var pather = path
        //tester.append(srcx.toString()+" "+srcy.toString()+butsrcx.toString()+" "+butsrcy.toString()+"\n")
        //tester.append(desx.toString()+" "+desy.toString()+butdesx.toString()+" "+butdesy.toString()+"\n")
        for (i in 1..(pather[butdesx][butdesy].size - 1)) {
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setInactiveImage(
                R.drawable.ic_mathematics_green
            )
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setActiveImage(
                R.drawable.ic_mathematics_green
            )
            buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].playAnimation()
            delay(delayTimeMedium)

            //tester.append(pather[butdesx][butdesy][i][0].toString()+" "+pather[butdesx][butdesy][i][1].toString()+"\n")

        }
        if (pather[butdesx][butdesy].size == 0) {
            Snackbar.make(parentLayout,"NO PATH FOUND!!",Snackbar.LENGTH_SHORT).show()
        }
        clearbut.isClickable = true
        isSearchRunning=0
    }

    private fun gradientDrawableValueSetter() {
        gdForRedColor.setColor(Color.parseColor("#FF0000"))
        gdForRedColor.cornerRadius = 10.0f
        gdForRedColor.setStroke(1, Color.parseColor("#000000"))

        gdForBrownColor.setColor(Color.parseColor("#A52A2A"))
        gdForBrownColor.cornerRadius = 10.0f
        gdForBrownColor.setStroke(1, Color.parseColor("#000000"))

        gdForGreenColor.setColor(Color.parseColor("#008000"))
        gdForGreenColor.cornerRadius = 10.0f
        gdForGreenColor.setStroke(1, Color.parseColor("#000000"))

        gdForWhiteColor.setColor(Color.parseColor("#FFFFFF"))
        gdForWhiteColor.cornerRadius = 10.0f
        gdForWhiteColor.setStroke(1, Color.parseColor("#000000"))

        gdForBlueColor.setColor(Color.parseColor("#0000FF"))
        gdForBlueColor.cornerRadius = 10.0f
        gdForBlueColor.setStroke(1, Color.parseColor("#000000"))
    }

    private fun createButtonGrid() {
        val screenLinearLayout = LinearLayout(this)
        screenLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        screenLinearLayout.orientation = LinearLayout.VERTICAL
        var screenid = resources.getIdentifier("screen", "id", packageName)
        screenLinearLayout.id = screenid
        mainscreen.addView(screenLinearLayout)
        for (i in 0..sizeb) {

            val arrayLinearLayout = LinearLayout(this)
            arrayLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
            )
            arrayLinearLayout.orientation = LinearLayout.HORIZONTAL
            //arrayLinearLayout.setPadding(2,2,2,2)

            val buttonStatusRow: MutableMap<SparkButton, Int> = mutableMapOf()
            val buttonRow: MutableList<SparkButton> = mutableListOf()
            for (j in 0..(size)) {
                val sbutton: SparkButton = SparkButtonBuilder(this).setImageSizeDp(30)
                    .setActiveImage(R.drawable.ic_mathematics)
                    .setInactiveImage(R.drawable.ic_mathematics_empty)
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
                    1.0f
                )

                sbutton.setEventListener(object : SparkEventListener {
                    override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {

                    }

                    override fun onEvent(button: ImageView?, buttonState: Boolean) {
                        if (startStatusKeeper == 0) {
                            sbutton.setActiveImage(R.drawable.ic_trending_flat_24px)
                            sbutton.isClickable = false
                            sbutton.setOnClickListener {  }
                            sbutton.setInactiveImage(R.drawable.ic_trending_flat_24px)
                            startStatusKeeper = 1
                            buttonStatusRow.put(sbutton, 3)
                            Log.i(
                                "buttonstatus",
                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                    sbutton
                                ).toString()
                            )
                            butsrcx = i
                            butsrcy = j

                        } else if (endStatusKeeper == 0) {
                            sbutton.setActiveImage(R.drawable.ic_gps_fixed_24px)
                            sbutton.isClickable = false
                            sbutton.setOnClickListener {  }
                            sbutton.setInactiveImage(R.drawable.ic_gps_fixed_24px)
                            //sbutton.pressOnTouch(false)
                            endStatusKeeper = 1
                            buttonStatusRow.put(sbutton, 3)
                            Log.i(
                                "buttonstatus",
                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                    sbutton
                                ).toString()
                            )
                            butdesx = i
                            butdesy = j

                            //findPath()
                        }
                        else  {
                            if (buttonStatusRow.get(sbutton) != 3) {
                                if (buttonWeightStatus == 0) {
                                    sbutton.setActiveImage(R.drawable.ic_mathematics)
                                    val buttonStatus = buttonStatusRow.get(sbutton)
                                    if (buttonStatus == 0) {
                                        buttonStatusRow.put(sbutton, 1)
                                        Log.i(
                                            "buttonstatus",
                                            "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                sbutton
                                            ).toString()
                                        )
                                    } else if (buttonStatus == 1) {
                                        buttonStatusRow.put(sbutton, 0)
                                        Log.i(
                                            "buttonstatus",
                                            "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                sbutton
                                            ).toString()
                                        )
                                    } else {
                                        buttonStatusRow.put(sbutton, 0)
                                        Log.i(
                                            "buttonstatus",
                                            "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                sbutton
                                            ).toString()
                                        )
                                    }
                                } else {
                                    sbutton.setActiveImage(R.drawable.ic_gymnastic)
                                    val buttonStatus = buttonStatusRow.get(sbutton)
                                    if (buttonStatus == 0) {
                                        buttonStatusRow.put(sbutton, 2)
                                        Log.i(
                                            "buttonstatus",
                                            "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                sbutton
                                            ).toString()
                                        )
                                    } else if (buttonStatus == 2) {
                                        buttonStatusRow.put(sbutton, 0)
                                        Log.i(
                                            "buttonstatus",
                                            "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                sbutton
                                            ).toString()
                                        )
                                    } else {
                                        buttonStatusRow.put(sbutton, 0)
                                        Log.i(
                                            "buttonstatus",
                                            "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                sbutton
                                            ).toString()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    override fun onEventAnimationStart(
                        button: ImageView?,
                        buttonState: Boolean
                    ) {

                    }
                })

//

                buttonStatusRow.put(sbutton, 0)
                buttonRow.add(sbutton)
                arrayLinearLayout.addView(sbutton)
            }
            buttonStatusKeeper.add(buttonStatusRow)
            buttons.add(buttonRow)
            screenLinearLayout.addView(arrayLinearLayout)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(progress!=0) {
            delayTimeShort = staticDelayTimeShort / progress
            delayTimeMedium=staticDelayTimeMedium/progress
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}