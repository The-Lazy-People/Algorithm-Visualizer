package com.thelazypeople.algorithmvisualizer.pathfinder.tutorialui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.fragment_four.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class fourFragment : Fragment() {
    private var visible=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_four, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser)
        {
            visible=1
            video()
        }
        else{
            visible=0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun video(){
        if(visible==1) {
            CoroutineScope(Dispatchers.Main).launch {
                var mediaController = MediaController(activity)
                videoView.setMediaController(mediaController)
                val path = "android.resource://com.thelazypeople.algorithmvisualizer/" + R.raw.maze;
                val uri = Uri.parse(path)
                videoView.setVideoURI(uri)
                videoView.setMediaController(null)
                videoView.start()

            }
        }
    }
}