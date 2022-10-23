package com.ervalsa.storyapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ervalsa.storyapp.data.remote.response.story.StoryItem
import com.ervalsa.storyapp.databinding.ItemStoryBinding

class ListStoryAdapter : ListAdapter<StoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listStoryBinding = ItemStoryBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false)
        return ListViewHolder(listStoryBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
    }

    class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItem) {
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(RequestOptions())
                .into(binding.imgPhotoStory)

            itemView.setOnClickListener {

            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}