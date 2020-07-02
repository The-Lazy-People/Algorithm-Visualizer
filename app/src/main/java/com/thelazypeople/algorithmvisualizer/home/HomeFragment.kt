package com.thelazypeople.algorithmvisualizer.home

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.squareup.picasso.Picasso
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load("https://img.shields.io/github/issues/The-Lazy-People/Algorithm-Visualizer").into(issues)

        thelazypeople.movementMethod = LinkMovementMethod.getInstance()
        adarsh.movementMethod = LinkMovementMethod.getInstance()
        abhishek.movementMethod = LinkMovementMethod.getInstance()
        ayushi.movementMethod = LinkMovementMethod.getInstance()

    }

}