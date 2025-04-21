package com.example.text_classification.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.text_classification.data.api.NewsApiService
import com.example.text_classification.data.model.Article


class NewsPagingSource(
    private val api: NewsApiService,
    private val apiKey: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val response = api.getEverything(
                query = "apple",
                from = "2025-03-25",
                sortBy = "publishAt",
                page = page,
                apiKey = apiKey
            )
            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }
}
