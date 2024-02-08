package com.example.trendingrepos.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
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

class GitHubRepoCardRecyclerViewAdapter(private val response: Repos?) : RecyclerView.Adapter<GitHubRepoCardRecyclerViewAdapter.ViewHolder>(), Filterable {

    private var contributorsList: List<Contributor>? = null
    private var filteredResponse : Repos? = response

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moreDetails : ConstraintLayout
        val login : TextView
        val repoName : TextView
        val description : TextView
        val contributorImageRecyclerView : RecyclerView
        val language : TextView
        val starCount : TextView
        val forkCount : TextView
        val color : CardView
        val heart : ImageView
        var isHeart : Boolean
        init {
            moreDetails = view.findViewById(R.id.more_details)
            login = view.findViewById(R.id.name)
            repoName = view.findViewById(R.id.repo_name)
            description = view.findViewById(R.id.description)
            contributorImageRecyclerView = view.findViewById(R.id.contributor_recycler_view)
            language = view.findViewById(R.id.language)
            starCount = view.findViewById(R.id.star_count)
            forkCount = view.findViewById(R.id.fork_count)
            color = view.findViewById(R.id.color)
            heart = view.findViewById(R.id.heart_icon)
            isHeart = false
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.github_card_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return RecyclerView.NO_ID
    }

    override fun getItemCount(): Int {
        return filteredResponse?.items?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val repos = filteredResponse?.items?.get(position)

        viewHolder.login.text = repos?.owner?.login
        viewHolder.repoName.text = repos?.name
        viewHolder.description.text = repos?.description
        viewHolder.language.text = repos?.language
        viewHolder.starCount.text = repos?.stargazers_count.toString()
        viewHolder.forkCount.text = repos?.forks_count.toString()

        viewHolder.heart.setOnClickListener{
            if (!viewHolder.isHeart){
                viewHolder.isHeart = true
                viewHolder.heart.setImageResource(R.drawable.ic_heart_filled)
            }
            else{
                viewHolder.isHeart = false
                viewHolder.heart.setImageResource(R.drawable.ic_heart)
            }
        }


        response!!.items[position].owner.login.let { login ->
        response!!.items[position].name.let { repoName ->
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val response = RetrofitInstance.trendingRepoApi.getContributorsAvatar(login, repoName)
                        if (response.isSuccessful) {
                            contributorsList = response.body()
                            val contributorAdapter =
                                ContributorImageRecyclerViewAdapter(response)
                            viewHolder.contributorImageRecyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context,LinearLayoutManager.HORIZONTAL,false)
                            viewHolder.contributorImageRecyclerView.adapter = contributorAdapter
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().toLowerCase().trim()
                val filteredItems = if (charString.isEmpty()) {
                    response!!.items
                } else {
                    response!!.items.filter {
                        it.name.toLowerCase().contains(charString) || it.description.toLowerCase().contains(charString)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = Repos(items = filteredItems)
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d("MyTag",results?.values.toString())
                filteredResponse = results?.values as? Repos ?: Repos(items = emptyList())
                notifyDataSetChanged()
            }
        }
    }
}