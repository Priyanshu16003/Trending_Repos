package com.example.trendingrepos

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingrepos.adapters.GitHubRepoCardRecyclerViewAdapter
import com.example.trendingrepos.databinding.ActivityMainBinding
import com.example.trendingrepos.factory.MainViewModelFactory
import com.example.trendingrepos.fragments.NoInternetPopUpFragment
import com.example.trendingrepos.fragments.NoInternetScreenFragment
import com.example.trendingrepos.utils.NetworkUtils
import com.example.trendingrepos.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: GitHubRepoCardRecyclerViewAdapter
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = (application as TrendingRepoApplication).gitHubProjectRepository
        val networkConnectivityObserver = NetworkConnectivityObserver(this)
        val noInternetPopUpFragment = NoInternetPopUpFragment()

        lifecycleScope.launch {
            networkConnectivityObserver.observ()
                .collect(){
                    when(it){
                        ConnectivityObserver.Status.Unavailable -> {
                            Toast.makeText(applicationContext,"Unavailable", Toast.LENGTH_LONG).show()
                            val noInternetScreenFragment = NoInternetScreenFragment()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.no_internet_fragment, noInternetScreenFragment)
                                .commit()
                        }

                        ConnectivityObserver.Status.Lost -> {
                            Toast.makeText(applicationContext,"Lost", Toast.LENGTH_LONG).show()
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                                .replace(R.id.no_internet_fragment, noInternetPopUpFragment)
                                .commitNow()
                        }

                        ConnectivityObserver.Status.Available -> {
                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                                .remove(noInternetPopUpFragment)
                                .commit()
                        }

                        ConnectivityObserver.Status.Losing -> { Toast.makeText(applicationContext,"Losing", Toast.LENGTH_LONG).show() }

                    }
                }
        }

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)
        mainViewModel.repos.observe(this) {
            binding.shimmerLayout.startShimmer()
            if(it != null){
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.repositoriesRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = GitHubRepoCardRecyclerViewAdapter(it)
                binding.repositoriesRecyclerView.adapter = adapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }
}