package com.example.trendingrepos.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.trendingrepos.R
import com.example.trendingrepos.model.Contributor
import retrofit2.Response

class ContributorImageRecyclerViewAdapter(val response: Response<List<Contributor>>?) : RecyclerView.Adapter<ContributorImageRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val contributorImage : ImageView

        fun bindContributorImage(contributorImageUrl: String?) {

            contributorImage.load(contributorImageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
        init {
            contributorImage = view.findViewById(R.id.contributor_image)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.contributor_image_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return response?.body()!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.d("MyTag",response?.body().toString())
        val contributorImageUrl = response?.body()?.get(position)?.avatar_url
        holder.bindContributorImage(contributorImageUrl)
    }
}