package com.example.text_classification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.text_classification.data.api.NewsApiService
import com.example.text_classification.data.sentimentanalysis.SentimentAnalyzer
import com.example.text_classification.databinding.ActivityMainBinding
import com.example.text_classification.ui.NewsSimpleAdapter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsSimpleAdapter
    private lateinit var analyzer: SentimentAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analyzer = SentimentAnalyzer(this)
        adapter = NewsSimpleAdapter(analyzer)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        val api = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)

        lifecycleScope.launch {
            try {
                val response = api.getEverything(
                    query = "apple",
                    from = "2025-03-25",
                    sortBy = "publishedAt",
                    page=1,
                    apiKey = "0db8aaefe18a476b86f1970327cfe251"
                )
                adapter.submitList(response.articles)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}