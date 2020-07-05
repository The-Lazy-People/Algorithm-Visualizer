package com.thelazypeople.algorithmvisualizer.markov

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import com.google.gson.Gson
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.activity_markov.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MarkovActivity : AppCompatActivity() {
    var dataKeeper:TrainingDataKeeper= TrainingDataKeeper()
    var str=""
    var startTest=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markov)

        show.movementMethod= ScrollingMovementMethod()
        show.computeScroll()

        val sharedPreferences=this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
        val prefEditor=sharedPreferences.edit()

        start.setOnClickListener {

            str=train.text.toString()
            display.text="PREFIX"
            display2.text="SUFFIX"
            show2.movementMethod= ScrollingMovementMethod()
            markov(1, 2)
            startTest=1
        }

        test.setOnClickListener {
            if(startTest==0)
            {
                Toast.makeText(this,"Firstly Train the model", Toast.LENGTH_LONG).show()
            }
            else {
                val dataInString= Gson().toJson(dataKeeper)
                prefEditor.putString("Trainingset", str)
                prefEditor.putString("DataKeeper",dataInString)
                prefEditor.apply()
                prefEditor.commit()
                val intent = Intent(this, SuggestionsActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun markov(keySize: Int, outputSize: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            //require(keySize >= 1) { "Key size can't be less than 1" }
            // val str="jump? asked the Scarecrow. Never. He sits day after day in the great fields. They kept on walking, however, and at night the moon came out and shone brightly. So they lay down among the sweet smelling yellow flowers and slept soundly until morning. When it was daylight, the girl bathed her face in her hands, and she set to work in one of the shelves as she passed; it was labelled 'ORANGE MARMALADE', but to her great delight it fitted! Alice opened the door and closer to one another, for the stillness of the empty room was more dreadful"

            val words = str.toLowerCase().trimEnd().split(' ',',','.','?','!','\n')


            //print(words)

            var prefix = ""
            var suffix = ""

            for (i in 0..(words.size - 1)) {

                prefix = words.subList(i, i + 1).joinToString(" ")
                show.text =show.text.toString()+ prefix+"\n"

                delay(10)
                var scrollAmount = show.getLayout().getLineTop(show.getLineCount()) - show.getHeight();
                // if there is no need to scroll, scrollAmount will be <=0
                if (scrollAmount > 6)
                    show.scrollTo(0, scrollAmount);
                else
                    show.scrollTo(0, 0);

                suffix = if (i + 1 < words.size) words[i + 1] else ""
                show2.text =show2.text.toString()+ suffix+"\n"
                delay(10)
                var scrollAmount2 = show.getLayout().getLineTop(show2.getLineCount()) - show2.getHeight();
                // if there is no need to scroll, scrollAmount will be <=0
                if (scrollAmount2 > 6)
                    show2.scrollTo(0, scrollAmount2);
                else
                    show2.scrollTo(0, 0);
                delay(10)

                lateinit var suffixes: MutableMap<String,Int>
                suffixes = dataKeeper.dictFutureWord.getOrPut(prefix) { mutableMapOf<String,Int>() }
                var prevVal=suffixes.getOrPut(suffix){0}
                suffixes.put(suffix,prevVal+1)
                for(j in 0..prefix.length-1){
                    val prefixWord = prefix.substring(0,j)
                    val suffixWord = prefix
                    lateinit var suffixes: MutableMap<String,Int>
                    suffixes = dataKeeper.dictCompleteWord.getOrPut(prefixWord) { mutableMapOf<String,Int>() }
                    var prevValWord=suffixes.getOrPut(suffixWord){0}
                    suffixes.put(suffixWord,prevValWord+1)
                }
            }

        }
    }
}