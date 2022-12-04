package com.example.sportscommunity

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
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
        return createRetrofit("http://192.168.0.38:8080/").create(UserInfo::class.java)
    }

    fun postCommunity(): WriteCommunity {
        return createRetrofit("http://192.168.0.38:8080/").create(WriteCommunity::class.java)
    }

    fun postGroup(): WriteGroup {
        return createRetrofit("http://192.168.0.38:8080/").create(WriteGroup::class.java)
    }

    fun postAlone(): WriteAlone {
        return createRetrofit("http://192.168.0.38:8080/").create(WriteAlone::class.java)
    }

    fun postShop(): WriteShopInfo {
        return createRetrofit("http://192.168.0.38:8080/").create(WriteShopInfo::class.java)
    }

    fun postLike(): LikeInfo {
        return createRetrofit("http://192.168.0.38:8080/").create(LikeInfo::class.java)
    }

    fun postChat(): ChatInfo {
        return createRetrofit("http://192.168.0.38:8080/").create(ChatInfo::class.java)
    }
}

// 뉴스 api
data class News(
    var title: String?,
    var description: String?,
    var url: String?,
    var urlToImage: String?,
    var publishedAt: String
)

//회원가입 및 네이버, 카카오 로그인
class User(
    val username: String?,
    val password: String?,
    val email: String?,
    val nickname: String?,
    val mobile: String?,
    val sns_type: String?,
    val sns_connect_date: String?,
    val modify_date: String?,
    val birth: String?,
    val image: String?,
    val gender: String?
)

//함께해요 단체
data class GroupPlay(
    var groupImg: String?,
    var groupTitle: String?,
    var groupCategory: String?,
    var groupArea: String?,
    var groupComment: String?,
    var groupMemberNumber: String?,
    var groupTime: String?
)

//함께해요 단체 글 작성
class WriteGroupPlay(
    var type: String?,
    var categoryType: String?,
    var date: String?,
    var time: String?,
    var area: String?,
    var title: String?,
    var shortContent: String?,
    var content: String?,
    var number_member: String?,
    var gender: String?,
    var age: String?,
    var image: String?,
    var write_time: String?
)

//함께해요 개인
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

//함께해요 개인 글 작성
class WritePlayWith(
    var categoryType: String?,
    var area: String?,
    var title: String?,
    var date: String?,
    var time: String?,
    var content: String?,
    var gender: String?,
    var age: String?,
    var write_time: String?
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
    var chat: String?,
)

//커뮤니티 글 작성
class WriteContent(
    var categoryType: String?,
    var title: String?,
    var content: String?,
    var image: String?,
    var write_time: String?
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

//중고거래 글 작성
class WriteShop(
    var categoryType: String?,
    var area: String?,
    var title: String?,
    var content: String?,
    var image: String?,
    var price: String?,
    var write_time: String?
)

class Like(
    var type: String?,
    var id: String
)

class Chat(
    var type: String?,
    var id: String,
    var content: String
)

class NewsList {
    var articles: MutableList<News>? = null
}


interface NewsService {
    @GET("v2/top-headlines?country=kr&category=sports&apiKey=68d1e20073a64ebeb16d8ff1399e61a8")
    fun getNewsList(): Call<NewsList>
}

interface UserInfo {
    @POST("ribbon/.idea/server/apis/sign_sns.php")
    fun postUser(
        @Body params: HashMap<String, Any>
    ): Call<User>
}

interface WriteCommunity {
    @POST("")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteContent>
}

interface WriteGroup {
    @POST("")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteGroupPlay>
}

interface WriteAlone {
    @POST("")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WritePlayWith>
}

interface WriteShopInfo {
    @POST("")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteShop>
}

interface LikeInfo {
    @POST("")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<Like>
}

interface ChatInfo {
    @POST("")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<Chat>
}