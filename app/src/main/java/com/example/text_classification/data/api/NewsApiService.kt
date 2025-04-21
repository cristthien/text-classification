package com.example.text_classification.data.api

import com.example.text_classification.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query: String = "tesla",
        @Query("from") from: String = "2025-03-21",
        @Query("sortBy") sortBy: String = "publishAt",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20,
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String
    ): NewsResponse

}
