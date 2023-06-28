package com.example.intermediateiqbal.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intermediateiqbal.databinding.ItemLayoutBinding
import com.example.intermediateiqbal.retrofit.response.StoryItem

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val listStory = ArrayList<StoryItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListStory(itemStory: List<StoryItem>) {
        val diffCallback = DiffCallback(this.listStory, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.listStory.clear()
        this.listStory.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
        this.notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bind(listStory[position])
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(listStory[position]) }
    }

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

    override fun getItemCount(): Int = listStory.size

    interface OnItemClickCallback {
        fun onItemClicked(story: StoryItem)
    }


}

class DiffCallback(
    private val mOldFavList: List<StoryItem>,
    private val mNewFavList: List<StoryItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = mOldFavList.size

    override fun getNewListSize() = mNewFavList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavList[oldItemPosition]
        val newEmployee = mNewFavList[newItemPosition]
        return oldEmployee.id == newEmployee.id
    }
}