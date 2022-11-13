package com.example.sportscommunity

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object Retrofits {
    private fun createRetrofit(baseUrl: String): Retrofit {

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getNewsService(): NewsService {
        return createRetrofit("https://newsapi.org/").create(NewsService::class.java)
    }
}

data class News(
    var title: String?,
    var description: String?,
    var url: String?,
    var urlToImage: String?,
    var publishedAt: String
)

data class GroupPlay(
    var groupImg: String?,
    var groupTitle: String?,
    var groupCategory: String?,
    var groupArea: String?,
    var groupComment: String?,
    var groupMemberNumber: String?,
    var groupTime: String?
)

data class PlayWith(
    var userImg: String?,
    var playTitle: String?,
    var userId: String?,
    var userArea: String?,
    var time: String?,
    var goOn: String?,
    var playCategory: String?,
    var manWoman: String?,
    var playComment: String?

)

data class Group(
    var groupImage: String?,
    var category: String?,
    var time: String?
)

data class Content(
    var title: String?,
    var time: String?,
    var profileImage: String?,
    var userId: String?,
    var content: String?,
    var like: String?,
    var bookMark: String?,
    var chat: String?,
    var share: String?

)

data class Shop(
    var title: String?,
    var content: String?,
    var image: String,
    var writer: String?
)

class NewsList {
    var articles: MutableList<News>? = null
}

interface NewsService {
    @GET("v2/top-headlines?country=kr&category=sports&apiKey=68d1e20073a64ebeb16d8ff1399e61a8")
    fun getNewsList(): Call<NewsList>
}
