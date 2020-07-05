package com.thelazypeople.algorithmvisualizer.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

class LineView : View {

    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet) :super(context,attributeSet)
    constructor(context: Context,attributeSet: AttributeSet,defStyleAttr:Int):super(context,attributeSet,defStyleAttr)


    var paint=Paint()
    var pointA:PointF = PointF(10f,10f)
    var pointB:PointF = PointF(10f,10f)

    override fun onDraw(canvas: Canvas?) {
        paint.setColor(Color.BLACK)
        paint.strokeWidth=5f
        if (canvas != null) {
            canvas.drawLine(pointA.x,pointA.y,pointB.x,pointB.y,paint)
        }
        else
        {
            Toast.makeText(context,"NULL",Toast.LENGTH_SHORT).show()
        }
        super.onDraw(canvas)
    }

    fun draw(){
        invalidate()
        requestLayout()
    }
}