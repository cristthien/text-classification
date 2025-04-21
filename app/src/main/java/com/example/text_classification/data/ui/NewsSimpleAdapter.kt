package com.example.text_classification.ui
import com.example.text_classification.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.text_classification.data.model.Article
import com.example.text_classification.data.sentimentanalysis.SentimentAnalyzer
import com.example.text_classification.databinding.ItemNewsBinding

class NewsSimpleAdapter(private val analyzer: SentimentAnalyzer) :
    RecyclerView.Adapter<NewsSimpleAdapter.NewsViewHolder>() {

    private val articles = mutableListOf<Article>()

    fun submitList(list: List<Article>) {
        articles.clear()
        articles.addAll(list)
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.title.text = article.title
            binding.description.text = article.description ?: "Không có mô tả"
            binding.sentiment.text = analyzer.analyze(article.description ?: "")

            Glide.with(binding.image)
                .load(article.urlToImage)
                .placeholder(R.drawable.default_img) // hiển thị trong lúc loading
                .error(R.drawable.default_img)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size
}
