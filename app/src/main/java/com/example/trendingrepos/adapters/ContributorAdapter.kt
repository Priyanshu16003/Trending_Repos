package com.example.trendingrepos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.trendingrepos.R
import com.example.trendingrepos.databinding.ContributorImageLayoutBinding
import com.example.trendingrepos.model.Contributor

class ContributorAdapter(val response: List<Contributor>?) : RecyclerView.Adapter<ContributorAdapter.ViewHolder>() {
    class ViewHolder(val binding: ContributorImageLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindContributorImage(contributorImageUrl: String?) {

            binding.contributorImage.load(contributorImageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContributorImageLayoutBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return response?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contributorImageUrl = response?.get(position)?.avatar_url
        holder.bindContributorImage(contributorImageUrl)
    }
}