package com.example.trendingrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.adapters.GitHubRepoCardRecyclerViewAdapter
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.RetrofitInstance
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var response: Response<Repos>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val shimmer = findViewById<ShimmerFrameLayout>(R.id.shimmer_layout)
        val recyclerView = findViewById<RecyclerView>(R.id.repositories_recycler_view)
        val scrollView = findViewById<ScrollView>(R.id.scroll)
        shimmer.startShimmer()

        lifecycleScope.launchWhenCreated{
            Log.d("Mytag","here")
            try {
                response = RetrofitInstance.trendingRepoApi.getTrendingRepositories("score:1.0")
                if (response.isSuccessful && response.body()!= null) {
                    Log.d("Mytag", response.body().toString())
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    scrollView.visibility = View.GONE
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

                    val adapter = GitHubRepoCardRecyclerViewAdapter(response)

                    recyclerView.adapter = adapter
                    Log.d("Nottag", response.body().toString())

                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }
}