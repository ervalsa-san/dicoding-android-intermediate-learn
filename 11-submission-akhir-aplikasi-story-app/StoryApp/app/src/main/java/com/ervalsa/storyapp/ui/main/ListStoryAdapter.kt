package com.ervalsa.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ervalsa.storyapp.R
import com.ervalsa.storyapp.data.remote.response.story.StoryItem
import com.ervalsa.storyapp.ui.main.ListStoryAdapter.ListViewHolder
import com.ervalsa.storyapp.databinding.ItemStoryBinding
import com.ervalsa.storyapp.ui.detail.DetailActivity
import com.ervalsa.storyapp.ui.story.AddStoryActivity
import com.ervalsa.storyapp.utils.withDateFormat

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

        var imgPhoto: ImageView = holder.itemView.findViewById(R.id.img_photo_story)
        var tvName: TextView = holder.itemView.findViewById(R.id.tv_name)
        var tvDate: TextView = holder.itemView.findViewById(R.id.tv_create_at)
        var tvDescription: TextView = holder.itemView.findViewById(R.id.tv_description)

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
            val optionCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(imgPhoto, "image_story"),
                    Pair(tvName, "name"),
                    Pair(tvDate, "time"),
                    Pair(tvDescription, "description")
                )
            it.context.startActivity(intent, optionCompat.toBundle())
        }
    }

    class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItem) {
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
            binding.tvCreateAt.withDateFormat(story.createdAt)
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