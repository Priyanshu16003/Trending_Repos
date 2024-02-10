package com.example.trendingrepos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.databinding.GithubCardLayoutBinding
import com.example.trendingrepos.model.Contributor
import com.example.trendingrepos.model.Repos

class RepositoryAdapter(
    private val response: Repos?,
    private val avatars: List<List<Contributor>>
) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>(), Filterable {
    private var filteredResponse : Repos? = response

    class ViewHolder(val binding : GithubCardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = GithubCardLayoutBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return RecyclerView.NO_ID
    }

    override fun getItemCount(): Int {
        return filteredResponse?.items?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val repos = filteredResponse?.items?.get(position)
        val contributorAvatars : List<Contributor>? = avatars[position]
        val contributorAdapter =
            ContributorAdapter(contributorAvatars)

        with(viewHolder){
            repos?.let{
                binding.login.text = it.owner.login
                binding.repoName.text = it.name
                binding.description.text = it.description
                binding.language.text = it.language
                binding.starCount.text = it.stargazers_count.toString()
                binding.forkCount.text = it.forks_count.toString()
            }
        }

        viewHolder.binding.contributorRv.layoutManager =
            LinearLayoutManager(
            viewHolder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        viewHolder.binding.contributorRv.adapter = contributorAdapter

        viewHolder.itemView.setOnClickListener {
            with(viewHolder){
                binding.description.visibility = View.GONE
                binding.contributorRv.visibility = View.GONE
                binding.language.visibility = View.GONE
                binding.starCount.visibility = View.GONE
                binding.forkCount.visibility = View.GONE
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
                filteredResponse = results?.values as? Repos ?: Repos(items = emptyList())
                notifyDataSetChanged()
            }
        }
    }
}