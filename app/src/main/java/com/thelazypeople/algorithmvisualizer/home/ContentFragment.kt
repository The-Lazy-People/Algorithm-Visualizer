package com.thelazypeople.algorithmvisualizer.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thelazypeople.algorithmvisualizer.R
import com.thelazypeople.algorithmvisualizer.graph.GraphActivity
import com.thelazypeople.algorithmvisualizer.graph.GraphTutorialActivity
import com.thelazypeople.algorithmvisualizer.kmp.KmpActivity
import com.thelazypeople.algorithmvisualizer.nQueen.nQueenActivity
import com.thelazypeople.algorithmvisualizer.pathfinder.TutorialActivity
import com.thelazypeople.algorithmvisualizer.searching.SearchingActivity
import com.thelazypeople.algorithmvisualizer.sorting.SortingActivity
import kotlinx.android.synthetic.main.fragment_content.*

class ContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity!!.window.statusBarColor = resources.getColor(R.color.dark)
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sorting.setOnClickListener {
            startActivity(Intent(context, SortingActivity::class.java))
        }

        searching.setOnClickListener {
            startActivity(Intent(context, SearchingActivity::class.java))
        }

        pathfinding.setOnClickListener {
            startActivity(Intent(context, TutorialActivity::class.java))
        }

        nQueens.setOnClickListener {
            startActivity(Intent(context, nQueenActivity::class.java))
        }

        kmp.setOnClickListener {
            startActivity(Intent(context, KmpActivity::class.java))
        }

        graph.setOnClickListener {
            startActivity(Intent(context, GraphTutorialActivity::class.java))
        }

    }
}