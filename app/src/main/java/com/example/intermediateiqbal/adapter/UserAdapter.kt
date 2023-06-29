package com.example.intermediateiqbal.adapter

import com.bumptech.glide.Glide
import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.example.intermediateiqbal.databinding.ItemLayoutBinding
import com.example.intermediateiqbal.retrofit.response.StoryItem

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    private val storyList = ArrayList<StoryItem>()

    class ViewHolder (private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(story: StoryItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(imgItemPhoto)
                tvItemName.text = story.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bind(storyList[position])
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(storyList[position]) }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListStory(itemStory: List<StoryItem>) {
        val diffCallback = DiffCallback(this.storyList, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.storyList.clear()
        this.storyList.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
        this.notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback { fun onItemClicked(story: StoryItem) }
}

class DiffCallback(private val mOldFavList: List<StoryItem>, private val mNewFavList: List<StoryItem> ) : DiffUtil.Callback() {

    override fun getOldListSize() = mOldFavList.size
    override fun getNewListSize() = mNewFavList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavList[oldItemPosition]
        val newEmployee = mNewFavList[newItemPosition]
        return oldEmployee.id == newEmployee.id
    }

}