package com.thelazypeople.algorithmvisualizer.markov

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.Gson
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.activity_suggestions.*

class SuggestionsActivity : AppCompatActivity() {
    var pre=""
    var str=""
    var buttons:MutableList<Button> = ArrayList()
    var buttons2:MutableList<Button> = ArrayList()
    lateinit var dataKeeper:TrainingDataKeeper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestions)
        val sharedPreferences=this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
        str= sharedPreferences.getString("Trainingset","").toString()
        val dataInString=sharedPreferences.getString("DataKeeper","").toString()
        dataKeeper= Gson().fromJson(dataInString,TrainingDataKeeper::class.java)

        createlayout()


        etprefix.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var preWords = etprefix.text.toString().split(" ")
                var size=preWords.size
                pre=preWords[size-1]
                wordPredictor(pre)

            }
        })
//        etprefix.doOnTextChanged { text, start, before, count ->
//            pre=etprefix.text.toString()
//            wordPredictor(pre)
//            //suggestions.text=pre
//            // second part of function
//        }

        back.setOnClickListener {
            val intent= Intent(this,MarkovActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun wordPredictor(prefix:String){
        for(i in 0..2){
            buttons[i].text=""
            buttons[i].isClickable=false
            buttons2[i].isClickable=false
            buttons2[i].text=""
        }
        if(dataKeeper.dictFutureWord[prefix]?.size!=null)
            for(i in 0..dataKeeper.dictFutureWord[prefix]?.size!!-1){
                buttons2[i].text=(dataKeeper.dictFutureWord[prefix]?.toList()?.sortedBy { it.second }?.get(dataKeeper.dictFutureWord[prefix]?.size!!-i-1)?.first)
                //println(dataKeeper.dictFutureWord[prefix]?.toList()?.sortedBy { it.second }?.get(dataKeeper.dictFutureWord[prefix]?.size!!-i-1)?.second)
                if(i>=2){
                    break
                }
            }
        if(dataKeeper.dictCompleteWord[prefix]?.size!=null)
            for(i in 0..dataKeeper.dictCompleteWord[prefix]?.size!!-1){
                buttons[i].text=(dataKeeper.dictCompleteWord[prefix]?.toList()?.sortedBy { it.second }?.get(dataKeeper.dictCompleteWord[prefix]?.size!!-i-1)?.first)
                //println(dataKeeper.dictCompleteWord[prefix]?.toList()?.sortedBy { it.second }?.get(dataKeeper.dictCompleteWord[prefix]?.size!!-i-1)?.second)
                if(i>=2){
                    break
                }
            }

        for(i in 0..2)
        {
            if(buttons[i].text!="")
            {
                //buttons[i].visibility=
                buttons[i].isClickable=true
                buttons[i].setOnClickListener {
                    val wordsList=etprefix.text.toString().split(' ','\n')
                    var unchangedString=""
                    if (wordsList.size>=1)
                        unchangedString=wordsList.subList(0,wordsList.size-1).joinToString(" ")
                    etprefix.setText(unchangedString+" "+buttons[i].text)
                    etprefix.setSelection(etprefix.getText().length);
                }
            }
        }

        for(i in 0..2)
        {
            if(buttons2[i].text!="")
            {
                //buttons[i].visibility=
                buttons2[i].isClickable=true
                buttons2[i].setOnClickListener {
                    etprefix.setText(etprefix.text.toString() + " " + buttons2[i].text.toString())
                    etprefix.setSelection(etprefix.getText().length);
                }
            }
        }
    }

    private fun createlayout()
    {
        var screenid = resources.getIdentifier("screen", "id", packageName)
        val screen=findViewById<LinearLayout>(screenid)

        val mainscreen = LinearLayout(this)
        mainscreen.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,1.0f
        )
        mainscreen.orientation = LinearLayout.VERTICAL
        var mainscreenid = resources.getIdentifier("mainscreen", "id", packageName)
        mainscreen.id=mainscreenid
        screen.addView(mainscreen)

        val mainscreen2 = LinearLayout(this)
        mainscreen2.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,1.0f
        )
        mainscreen2.orientation = LinearLayout.VERTICAL
        var mainscreenid2 = resources.getIdentifier("mainscreen2", "id", packageName)
        mainscreen2.id=mainscreenid2
        screen.addView(mainscreen2)
        for(i in 0..2)
        {
            val button =Button(this)
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f
            )
            button.text=""
            button.setBackgroundColor(Color.WHITE)
            buttons.add(button)
            mainscreen.addView(button)
        }
        for(i in 0..2)
        {
            val button =Button(this)
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f
            )
            button.setBackgroundColor(Color.WHITE)
            buttons2.add(button)
            mainscreen2.addView(button)
        }

    }
}