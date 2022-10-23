package com.ervalsa.storyapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ervalsa.storyapp.data.remote.response.story.StoryItem
import com.ervalsa.storyapp.databinding.ItemStoryBinding

class ListStoryAdapter : ListAdapter<StoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private var itemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val ListStoryBinding = ItemStoryBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false)
        return ListViewHolder(ListStoryBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
    }

//    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
//    }

    class ListViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryItem) {
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description

            itemView.setOnClickListener {

            }
        }
    }

    fun setOnItemClickCallback(ItemClickCallback: OnItemClickCallback) {
        this.itemClickCallback = ItemClickCallback
    }

    interface OnItemClickCallback {
        fun itemClicked(story: StoryItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StoryItem,
                newItem: StoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}