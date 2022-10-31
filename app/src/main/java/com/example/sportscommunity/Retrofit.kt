package com.example.sportscommunity

import retrofit2.Call
import retrofit2.http.GET

data class News(
    var author: String?,
    var title: String?,
    var description: String?,
    var url: String?,
    var urlToImage: String?,
    var publishedAt: String?,
    var content: String?
)

class NewsList{
    var articles: MutableList<News>? = null
}

interface NewsService {
    @GET("v2/top-headlines?country=kr&category=sports&apiKey=68d1e20073a64ebeb16d8ff1399e61a8")
    fun getNewsList(): Call<NewsList>

}
