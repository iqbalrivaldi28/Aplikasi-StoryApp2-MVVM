package com.example.intermediateiqbal.detailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intermediateiqbal.databinding.ActivityDetailStoryBinding
import com.example.intermediateiqbal.retrofit.response.StoryItem
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding : ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userStory = intent.getParcelableExtra<StoryItem>(EXTRA_DATA)
        if (userStory != null){
            binding.tvDetailName.text = userStory.name
            binding.tvDetailDescription.text = userStory.description
            Glide.with(this)
                .load(userStory.photoUrl)
                .into(binding.ivDetailPhoto)
        }
    }
}