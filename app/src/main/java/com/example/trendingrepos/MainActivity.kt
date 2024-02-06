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
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.utils.Resource
import com.example.trendingrepos.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
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

        observeNetworkChanges(networkConnectivityObserver)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        mainViewModel.repos.observe(this) {
            when(it){
                is Resource.LOADING -> { showLoading()}
                is Resource.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    hideLoading()
                    showSuccessUI(it.data)
                }
                is Resource.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                    hideLoading()
                    showErrorUI(it.message)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observeNetworkChanges(networkConnectivityObserver: NetworkConnectivityObserver) {
        lifecycleScope.launch(Dispatchers.Main) {
            val noInternetPopUpFragment = NoInternetPopUpFragment()
            networkConnectivityObserver.observ()
                .collect{
                    when(it){
                        ConnectivityObserver.Status.Unavailable -> {}

                        ConnectivityObserver.Status.Lost -> {

                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                                .replace(R.id.no_internet_fragment, noInternetPopUpFragment)
                                .commitNow()
                        }

                        ConnectivityObserver.Status.Available -> {
                            supportFragmentManager.beginTransaction()
                                .remove(noInternetPopUpFragment)
                                .commitNow()
                        }

                        ConnectivityObserver.Status.Losing -> { Toast.makeText(applicationContext,"Losing", Toast.LENGTH_LONG).show() }
                    }
                }
        }
    }

    private fun showErrorUI(message: String?) {
        val noInternetScreenFragment = NoInternetScreenFragment()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            .replace(R.id.no_internet_fragment, noInternetScreenFragment)
            .commitNow()
    }

    private fun showSuccessUI(response: Repos?) {
        if(response != null){
            binding.repositoriesRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = GitHubRepoCardRecyclerViewAdapter(response)
            binding.repositoriesRecyclerView.adapter = adapter
        }
    }

    private fun hideLoading() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
    }

    private fun showLoading() {
        binding.shimmerLayout.startShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }
}