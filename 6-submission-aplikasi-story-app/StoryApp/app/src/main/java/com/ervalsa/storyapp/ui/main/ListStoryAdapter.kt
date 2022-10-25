package com.ervalsa.storyapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ervalsa.storyapp.data.remote.response.story.StoryItem
import com.ervalsa.storyapp.ui.main.ListStoryAdapter.ListViewHolder
import com.ervalsa.storyapp.databinding.ItemStoryBinding
import com.ervalsa.storyapp.ui.detail.DetailActivity

class ListStoryAdapter : ListAdapter<StoryItem, ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listStoryBinding = ItemStoryBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false)
        return ListViewHolder(listStoryBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)

        holder.itemView.setOnClickListener {
            val data = StoryItem(
                stories.id,
                stories.name,
                stories.description,
                stories.photoUrl,
                stories.createdAt,
                stories.lat,
                stories.lon
            )

            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, data)
            it.context.startActivity(intent)
        }
    }

    class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItem) {
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(RequestOptions())
                .into(binding.imgPhotoStory)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryItem> =
            object : DiffUtil.ItemCallback<StoryItem>() {
                override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                    return oldItem == newItem
                }
            }
    }
}