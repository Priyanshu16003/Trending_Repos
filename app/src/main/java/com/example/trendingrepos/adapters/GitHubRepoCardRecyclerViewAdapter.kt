package com.example.trendingrepos.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.R
import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.Repos
import com.example.trendingrepos.networking.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GitHubRepoCardRecyclerViewAdapter(private val response: Repos) : RecyclerView.Adapter<GitHubRepoCardRecyclerViewAdapter.ViewHolder>() {

    private var contributorsList: List<Contributor>? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView : CardView
        val moreDetails : ConstraintLayout
        val login : TextView
        val repoName : TextView
        val description : TextView
        val contributorImageRecyclerView : RecyclerView
        val language : TextView
        val starCount : TextView
        val forkCount : TextView
        val gitHubRepoApi = RetrofitInstance.trendingRepoApi

        init {
            cardView = view.findViewById(R.id.color)
            moreDetails = view.findViewById(R.id.more_details)
            login = view.findViewById(R.id.name)
            repoName = view.findViewById(R.id.repo_name)
            description = view.findViewById(R.id.description)
            contributorImageRecyclerView = view.findViewById(R.id.contributor_recycler_view)
            language = view.findViewById(R.id.language)
            starCount = view.findViewById(R.id.star_count)
            forkCount = view.findViewById(R.id.fork_count)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.github_card_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return response.items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.login.text = response.items[position].owner.login
        viewHolder.repoName.text = response.items[position].name
        viewHolder.description.text = response.items[position].description
        viewHolder.language.text = response.items[position].language
        viewHolder.starCount.text = response.items[position].stargazers_count.toString()
        viewHolder.forkCount.text = response.items[position].forks_count.toString()

        response.items[position].owner.login?.let { login ->
        response.items[position].name.let { repoName ->
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val response = RetrofitInstance.trendingRepoApi.getContributorsAvatar(login, repoName)
                        if (response.isSuccessful) {

                            Log.d("MyTag1",response.body().toString())
                            contributorsList = response.body()
                            val contributorAdapter =
                                ContributorImageRecyclerViewAdapter(response)
                            viewHolder.contributorImageRecyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context,LinearLayoutManager.HORIZONTAL,false)
                            viewHolder.contributorImageRecyclerView.adapter = contributorAdapter
                        } else {
                            Log.d("MyTag1",response.body().toString())
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}