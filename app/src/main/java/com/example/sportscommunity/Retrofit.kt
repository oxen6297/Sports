package com.example.sportscommunity

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

object Retrofits {
    private fun createRetrofit(baseUrl: String): Retrofit {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getNewsService(): NewsService {
        return createRetrofit("https://newsapi.org/").create(NewsService::class.java)
    }

    fun postUserInfo(): UserInfo {
        return createRetrofit("http://172.16.100.202:8080/").create(UserInfo::class.java)
    }
}

class User(
    val id: Int,
    val email: String?
) {
    constructor(email: String?) : this(0, email)
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
    var writer: String?,
    var chat: String?,
    var like: String?,
    var chatNum: String?,
    var timeText: String?,
    var area: String?,
    var category: String?,
    var price: String?
)

class NewsList {
    var articles: MutableList<News>? = null
}


interface NewsService {
    @GET("v2/top-headlines?country=kr&category=sports&apiKey=68d1e20073a64ebeb16d8ff1399e61a8")
    fun getNewsList(): Call<NewsList>
}

interface UserInfo {
    @POST("ribbon/.idea/server/apis/sign.php")
    fun postUser(
        @Body params: HashMap<String,Any>
    ): Call<User>
}