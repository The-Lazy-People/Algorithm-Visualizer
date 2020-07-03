package com.thelazypeople.algorithmvisualizer.pathfinder

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class Tuple2(var d:Int,var x:Int,var y:Int){
    fun getx():Int{
        return x
    }
    fun gety():Int{
        return y
    }
    fun getd():Int{
        return d
    }
}
class ComparatorTuple {

    companion object : Comparator<Tuple2> {

        override fun compare(a: Tuple2, b: Tuple2): Int = when {
            a.d != b.d -> a.d - b.d
            else -> 1
        }
    }
}
