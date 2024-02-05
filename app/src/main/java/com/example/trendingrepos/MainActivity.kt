package com.example.trendingrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.adapters.GitHubRepoCardRecyclerViewAdapter
import com.example.trendingrepos.databinding.ActivityMainBinding
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.RetrofitInstance
import com.example.trendingrepos.repository.GitHubProjectRepository
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var connectivityObserver: ConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        val gitHubRepoApi = RetrofitInstance.trendingRepoApi
        val repository = GitHubProjectRepository(gitHubRepoApi)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)
        mainViewModel.repos.observe(this) {
            binding.shimmerLayout.startShimmer()
            if(it != null){
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.repositoriesRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                val adapter = GitHubRepoCardRecyclerViewAdapter(it)
                binding.repositoriesRecyclerView.adapter = adapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }
}