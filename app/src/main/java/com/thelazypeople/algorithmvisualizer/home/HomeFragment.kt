package com.thelazypeople.algorithmvisualizer.home

import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        issues.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
        }
        issues.loadUrl("https://img.shields.io/github/issues/The-Lazy-People/Algorithm-Visualizer")
        issues.webViewClient = object :WebViewClient(){
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                issues.visibility = View.INVISIBLE
                super.onReceivedError(view, request, error)
            }
        }
        issues.setOnTouchListener(OnTouchListener { v, event -> true })

        stars.settings.apply {
            javaScriptEnabled=true
            allowContentAccess = true
        }
        stars.loadUrl("https://img.shields.io/github/stars/The-Lazy-People/Algorithm-Visualizer")
        stars.webViewClient = object :WebViewClient(){
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                stars.visibility = View.INVISIBLE
                super.onReceivedError(view, request, error)
            }
        }
        stars.setOnTouchListener(OnTouchListener { v, event -> true })

        forks.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
        }
        forks.loadUrl("https://img.shields.io/github/forks/The-Lazy-People/Algorithm-Visualizer")
        forks.webViewClient = object :WebViewClient(){
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                forks.visibility = View.INVISIBLE
                super.onReceivedError(view, request, error)
            }
        }
        forks.setOnTouchListener(OnTouchListener { v, event -> true })


        thelazypeople.movementMethod = LinkMovementMethod.getInstance()
        adarsh.movementMethod = LinkMovementMethod.getInstance()
        abhishek.movementMethod = LinkMovementMethod.getInstance()
        ayushi.movementMethod = LinkMovementMethod.getInstance()
    }

}