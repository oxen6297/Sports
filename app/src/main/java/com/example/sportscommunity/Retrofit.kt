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

    //뉴스
    fun getNewsService(): NewsService {
        return createRetrofit("https://newsapi.org/").create(NewsService::class.java)
    }

    //유저정보
    fun getUserService(): UserService {
        return createRetrofit("http://172.30.1.24:8080/").create(UserService::class.java)
    }

    //함께해요 단체 및 홈화면
    fun getGroupPlayService(): GroupPlayService {
        return createRetrofit("http://172.30.1.24:8080/").create(GroupPlayService::class.java)
    }

    //함께해요 개인
    fun getAlonePlayService(): AlonePlayService {
        return createRetrofit("http://172.30.1.24:8080/").create(AlonePlayService::class.java)
    }

    //커뮤니티
    fun getCommunityService(): CommunityService {
        return createRetrofit("http://172.30.1.24:8080/").create(CommunityService::class.java)
    }

    //중고거래
    fun getShopService(): ShopService {
        return createRetrofit("http://172.30.1.24:8080/").create(ShopService::class.java)
    }

    fun postUserInfo(): UserInfo {
        return createRetrofit("http://172.30.1.24:8080/").create(UserInfo::class.java)
    }

    fun postCommunity(): WriteCommunity {
        return createRetrofit("http://172.30.1.24:8080/").create(WriteCommunity::class.java)
    }

    fun postGroup(): WriteGroup {
        return createRetrofit("http://172.30.1.24:8080/").create(WriteGroup::class.java)
    }

    fun postAlone(): WriteAlone {
        return createRetrofit("http://172.30.1.24:8080/").create(WriteAlone::class.java)
    }

    fun postShop(): WriteShopInfo {
        return createRetrofit("http://172.30.1.24:8080/").create(WriteShopInfo::class.java)
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

//회원가입 및 네이버, 카카오 로그인 post
class User(
    var id: Int,
    var username: String?,
    var password: String?,
    var email: String?,
    var nickname: String?,
    var mobile: String?,
    var sns_type: String?,
    var sns_connect_date: String?,
    var modify_date: String?,
    var birth: String?,
    var image: String?,
    var gender: String?
)

data class UserId(
    var id: Int,
    var email: String
)

//함께해요 단체
data class GroupPlay(
    var groupImg: String?,
    var groupTitle: String?,
    var groupCategory: String?,
    var groupArea: String?,
    var content: String?,
    var groupComment: String?,
    var groupMemberNumber: String?,
    var groupTime: String?
)

//함께해요 단체 글 작성
class WriteGroupPlay(
    var userId: Int?,
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
    var minage: String?,
    var maxage: String?,
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
    var playCategory: String?,
    var content: String?,
    var manWoman: String?,
    var like: String?,
    var chat: String
)

//함께해요 개인 글 작성
class WritePlayWith(
    var userId: Int?,
    var categoryType: String?,
    var area: String?,
    var title: String?,
    var date: String?,
    var time: String?,
    var content: String?,
    var gender: String?,
    var minage: String?,
    var maxage: String?,
    var write_time: String?
)

//커뮤니티
data class Content(
    var categoryId: Int?,
    var title: String?,
    var time: String?,
    var profileImage: String?,
    var userId: String?,
    var content: String?,
    var like: String?,
    var chat: String?
)

//커뮤니티 글 작성
class WriteContent(
    var id: Int?,
    var userId: Int?,
    var categoryid: Int?,
    var title: String?,
    var description: String?,
    var titleimage: String?,
    var writedate: String?
)
//{
//    constructor(
//        categoryid: Int?, title: String?, description: String?, titleimage: String?, writedate: String?
//    ) : this(5, categoryid, title, description, titleimage, writedate)
//}

//중고거래탭
data class Shop(
    var title: String?,
    var content: String?,
    var image: String,
    var writer: String?,
    var chat: String?,
    var like: String?,
    var timeText: String?,
    var area: String?,
    var category: String?,
    var price: String?
)

//중고거래 글 작성
class WriteShop(
    var userId: Int?,
    var categoryType: String?,
    var area: String?,
    var title: String?,
    var content: String?,
    var image: String?,
    var price: String?,
    var write_time: String?,
)

//뉴스
class NewsList {
    var articles: MutableList<News>? = null
}

//유저 정보
class UserEmail {
    var userInfo: MutableList<UserId>? = null
}

//함께해요 단체 및 홈화면
class GroupPlayTab {
    var group: MutableList<GroupPlay>? = null
}

//함께해요 개인
class AlonePlay {
    var alone: MutableList<PlayWith>? = null
}

//커뮤니티
class CommunityTab {
    var community: MutableList<Content>? = null
}

//중고거래
class ShopTab {
    var shop: MutableList<Shop>? = null
}

/**
 * @GET 요청부분
 */
//뉴스
interface NewsService {
    @GET("v2/top-headlines?country=kr&category=sports&apiKey=68d1e20073a64ebeb16d8ff1399e61a8")
    fun getNewsList(): Call<NewsList>
}

//유저정보
interface UserService {
    @GET("php")
    fun getUserInfo(): Call<UserEmail>
}

//함께해요 단체면 및 홈화면
interface GroupPlayService {
    @GET("php")
    fun getGroupPlay(): Call<GroupPlayTab>
}

//함께해요 개인
interface AlonePlayService {
    @GET("php")
    fun getAlonePlay(): Call<AlonePlay>
}

//커뮤니티
interface CommunityService {
    @GET("php")
    fun getCommunity(): Call<CommunityTab>
}

//중고거래
interface ShopService {
    @GET("php")
    fun getShop(): Call<ShopTab>
}


/**
 * @POST 요청부분
 */

interface UserInfo {
    @POST("ribbon/.idea/server/apis/sign.php")
    fun postUser(
        @Body params: HashMap<String, Any>
    ): Call<User>
}

interface WriteCommunity {
    @POST("ribbon/.idea/server/apis/boards.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteContent>
}

interface WriteGroup {
    @POST("ribbon/.idea/server/apis/group.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteGroupPlay>
}

interface WriteAlone {
    @POST("ribbon/.idea/server/apis/alone.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WritePlayWith>
}

interface WriteShopInfo {
    @POST("ribbon/.idea/server/apis/used.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteShop>
}

var writeFlag = hashMapOf<String,Any>()
var minNum = hashMapOf<String,Any>()
var maxNum = hashMapOf<String,Any>()
