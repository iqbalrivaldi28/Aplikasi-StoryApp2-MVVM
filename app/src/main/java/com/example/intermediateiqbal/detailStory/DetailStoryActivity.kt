package com.example.intermediateiqbal.detailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.intermediateiqbal.R
import com.example.intermediateiqbal.databinding.ActivityDetailStoryBinding
import com.example.intermediateiqbal.retrofit.response.StoryItem

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryItem>(EXTRA_DATA)

        if (story != null){
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivDetailPhoto)
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}