package com.example.trendingrepos.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.R
import com.example.trendingrepos.model.Repos
import retrofit2.Response

class GitHubRepoCardRecyclerViewAdapter(private val response: Response<Repos>) : RecyclerView.Adapter<GitHubRepoCardRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView : CardView
        val moreDetails : ConstraintLayout
        val fullName : TextView
        val description : TextView
        val contributorImageRecyclerView : RecyclerView
        val language : TextView
        val starCount : TextView
        val forkCount : TextView

        init {
            cardView = view.findViewById(R.id.color)
            moreDetails = view.findViewById(R.id.more_details)
            fullName = view.findViewById(R.id.full_name)
            description = view.findViewById(R.id.description)
            contributorImageRecyclerView = view.findViewById(R.id.contributor_recycler_view)
            language = view.findViewById(R.id.language)
            starCount = view.findViewById(R.id.star_count)
            forkCount = view.findViewById(R.id.fork_count)

            cardView.setOnClickListener {
                moreDetails.visibility = View.VISIBLE
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.github_card_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return response.body()!!.items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Log.d("Mytag1",response.body()!!.items[0].toString())
            viewHolder.fullName.text = response.body()!!.items[position].full_name
            viewHolder.description.text = response.body()!!.items[position].description
            viewHolder.language.text = response.body()!!.items[position].language
            viewHolder.starCount.text = response.body()!!.items[position].stargazers_count.toString()
            viewHolder.forkCount.text = response.body()!!.items[position].forks_count.toString()

    }
}