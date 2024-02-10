package com.example.trendingrepos

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingrepos.adapters.RepositoryAdapter
import com.example.trendingrepos.databinding.ActivityMainBinding
import com.example.trendingrepos.factory.MainViewModelFactory
import com.example.trendingrepos.fragments.NoInternetPopUpFragment
import com.example.trendingrepos.fragments.NoInternetScreenFragment
import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.utils.ConnectivityObserver
import com.example.trendingrepos.utils.NetworkConnectivityObserver
import com.example.trendingrepos.utils.Resource
import com.example.trendingrepos.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var noInternetScreenFragment: NoInternetScreenFragment
    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val repository = (application as TrendingRepoApplication).gitHubProjectRepository
        val networkConnectivityObserver = NetworkConnectivityObserver(this)

        noInternetScreenFragment = NoInternetScreenFragment()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            observeNetworkChanges(networkConnectivityObserver)
        }
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        mainViewModel.fetchTrendingRepos()
        mainViewModel.repos.observe(this) {
            when(it) {
                is Resource.LOADING -> {
                    showLoading()
                }

                is Resource.SUCCESS -> {
                    binding.swRefresh.isRefreshing = false
                    binding.swRefresh.isRefreshing = false
                    hideLoading()
                    showSuccessUI(it.data)
                }

                is Resource.ERROR -> {
                    binding.swRefresh.isRefreshing = false
                    binding.swRefresh.isRefreshing = false
                    hideLoading()
                    showErrorUI(it.message)
                }
            }
        }
        binding.swRefresh.setOnRefreshListener {
            mainViewModel.fetchTrendingRepos()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observeNetworkChanges(networkConnectivityObserver: NetworkConnectivityObserver) {
        lifecycleScope.launch(Dispatchers.Main) {
            val noInternetPopUpFragment = NoInternetPopUpFragment()
            networkConnectivityObserver.observ()
                .collect {
                    when(it) {
                        ConnectivityObserver.Status.Unavailable -> {}

                        ConnectivityObserver.Status.Lost -> {

                            supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                                .replace(R.id.noInternetFr, noInternetPopUpFragment)
                                .commitNow()
                        }

                        ConnectivityObserver.Status.Available -> {
                            mainViewModel.fetchTrendingRepos()
                            supportFragmentManager.beginTransaction()
                                .remove(noInternetPopUpFragment)
                                .remove(noInternetScreenFragment)
                                .commitNow()
                        }

                        ConnectivityObserver.Status.Losing -> { Toast.makeText(applicationContext,"Losing", Toast.LENGTH_LONG).show() }
                    }
                }
        }
    }

    private fun showErrorUI(message: String?) {
        noInternetScreenFragment = NoInternetScreenFragment()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            .replace(R.id.noInternetFr, noInternetScreenFragment)
            .commitNow()
    }

    private fun showSuccessUI(response: Repos?) {
        if(response != null){
            lifecycleScope.launch(Dispatchers.Main) {
                var avtars: List<List<Contributor>> = getContributorAvtar(response)

                binding.repositoriesRv.layoutManager =
                    LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.VERTICAL,
                        false)

                adapter = RepositoryAdapter(response, avtars)
                binding.repositoriesRv.adapter = adapter
            }
        }
    }

    private suspend fun getContributorAvtar(response: Repos): List<List<Contributor>> {
        return coroutineScope {
            response.items.map { item ->
                async {
                    val login = item.owner.login
                    val name = item.name
                    mainViewModel.fetchContributorsAvtar(login, name)
                    return@async mainViewModel.contributorAvtar.value?.data ?: emptyList()
                }
            }.awaitAll()
        }
    }

    private fun hideLoading() {
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.GONE
    }

    private fun showLoading() {
        binding.shimmer.startShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val menuItem = menu!!.findItem(R.id.search_icon)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
        return true
    }
}