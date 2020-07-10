package com.thelazypeople.algorithmvisualizer.home

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.thelazypeople.algorithmvisualizer.GithubDetails
import com.thelazypeople.algorithmvisualizer.R
import kotlinx.android.synthetic.main.forks.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.issues.*
import kotlinx.android.synthetic.main.stars.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


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

        //checking internet connection.
        if(context?.let { isNetworkAvailable(it) }!!){
            getDetails()
        }else{  //If wifi or mobile Network in not On, we will not show the badges.
            issues.visibility = View.INVISIBLE
            forks.visibility = View.INVISIBLE
            stars.visibility = View.INVISIBLE
        }
        thelazypeople.movementMethod = LinkMovementMethod.getInstance()
        adarsh.movementMethod = LinkMovementMethod.getInstance()
        abhishek.movementMethod = LinkMovementMethod.getInstance()
        ayushi.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false
        val networkInfo = cm.activeNetworkInfo
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected
    }

    private fun getDetails(){
        val okHttpClient =OkHttpClient()
        val request = Request.Builder()
            .url("https://api.github.com/repos/The-Lazy-People/Algorithm-Visualizer")
            .build()
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        GlobalScope.launch(Dispatchers.Main) {
            var response = withContext(Dispatchers.IO){
                okHttpClient.newCall(request).execute()
            }
            val job = withContext(Dispatchers.IO){
                if(response.isSuccessful) {
                    stars.visibility = View.VISIBLE
                    forks.visibility = View.VISIBLE
                    issues.visibility = View.VISIBLE

                    val body = response.body?.string()
                    val githubDetails = gson.fromJson<GithubDetails>(body, GithubDetails::class.java)
                    val issues = "${githubDetails.openIssuesCount.toString()} OPEN"
                    val work = withContext(Dispatchers.Main){
                        stars_result.text = githubDetails.stargazersCount.toString()
                        forks_result.text = githubDetails.forksCount.toString()
                        issues_result.text = issues
                        }
                    }
                 else{
                    val work = withContext(Dispatchers.Main){
                        stars.visibility = View.INVISIBLE
                        forks.visibility = View.INVISIBLE
                        issues.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

}