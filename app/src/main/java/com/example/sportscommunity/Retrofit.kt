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

//    //유저정보
//    fun getUserService(): UserService {
//        return createRetrofit("http://172.30.1.24:8080/").create(UserService::class.java)
//    }

    //함께해요 단체 및 홈화면
    fun getGroupPlayService(): GroupPlayService {
        return createRetrofit("http://172.30.1.18:8080/").create(GroupPlayService::class.java)
    }

    //함께해요 개인
    fun getAlonePlayService(): AlonePlayService {
        return createRetrofit("http://172.30.1.18:8080/").create(AlonePlayService::class.java)
    }

    //커뮤니티 자유게시판
    fun getFreeBoardService(): FreeBoardService {
        return createRetrofit("http://172.30.1.18:8080/").create(FreeBoardService::class.java)
    }

    //커뮤니티 질문게시판
    fun getQuestionBoardService(): QuestionService {
        return createRetrofit("http://172.30.1.18:8080/").create(QuestionService::class.java)
    }

    //커뮤니티 문의게시판
    fun getFaqBoardService(): FaqService {
        return createRetrofit("http://172.30.1.18:8080/").create(FaqService::class.java)
    }

    //커뮤니티 구기종목
    fun getBallSportsService(): BallSportsService {
        return createRetrofit("http://172.30.1.18:8080/").create(BallSportsService::class.java)
    }

    //커뮤니티 레져스포츠
    fun getLeisureSportsService(): LeisureSportsService {
        return createRetrofit("http://172.30.1.18:8080/").create(LeisureSportsService::class.java)
    }

    //커뮤니티 생활스포츠
    fun getLifeSportsService(): LifeSportsService {
        return createRetrofit("http://172.30.1.18:8080/").create(LifeSportsService::class.java)
    }

    //커뮤니티 동계스포츠
    fun getWinterSportsService(): WinterSportsService {
        return createRetrofit("http://172.30.1.18:8080/").create(WinterSportsService::class.java)
    }

    //커뮤니티 해양스포츠
    fun getWaterSportsService(): WaterSportsService {
        return createRetrofit("http://172.30.1.18:8080/").create(WaterSportsService::class.java)
    }

    //커뮤니티 이스포츠
    fun getESportsService(): ESportsService {
        return createRetrofit("http://172.30.1.18:8080/").create(ESportsService::class.java)
    }

    //중고거래
    fun getShopService(): ShopService {
        return createRetrofit("http://172.30.1.18:8080/").create(ShopService::class.java)
    }

    fun postUserInfo(): UserInfo {
        return createRetrofit("http://172.30.1.18:8080/").create(UserInfo::class.java)
    }

    fun postCommunity(): WriteCommunity {
        return createRetrofit("http://172.30.1.18:8080/").create(WriteCommunity::class.java)
    }

    fun postGroup(): WriteGroup {
        return createRetrofit("http://172.30.1.18:8080/").create(WriteGroup::class.java)
    }

    fun postAlone(): WriteAlone {
        return createRetrofit("http://172.30.1.18:8080/").create(WriteAlone::class.java)
    }

    fun postShop(): WriteShopInfo {
        return createRetrofit("http://172.30.1.18:8080/").create(WriteShopInfo::class.java)
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

//data class UserId(
//    var id: Int,
//    var email: String
//)

//함께해요 단체
data class GroupPlay(
    var id: Int?,
    var once: String?,
    var local: String?,
    var title: String?,
    var line: String?,
    var description: String?,
    var peoplenum: String?,
    var peoplenownum: String?,
    var gender: String?,
    var nickname: String?,
    var minage: String?,
    var titleimage: String?,
    var userid: Int?,
    var maxage: String?,
    var writedate: String?
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
    var userimage: String?,
    var title: String?,
    var userid: Int?,
    var local: String?,
    var writedate: String?,
    var date: String?,
    var id: Int?,
    var nickname: String?,
    var description: String?,
    var minage: String?,
    var maxage: String?,
    var gender: String?,
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
    var title: String?,
    var writedate: String?,
    var profileimage: String?,
    var nickname: String?,
    var img: String?,
    var id: Int?,
    var userid: Int?,
    var description: String?
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

//중고거래탭
data class Shop(
    var title: String?,
    var description: String?,
    var usedimage1: String,
    var usedimage2: String,
    var usedimage3: String,
    var usedimage4: String,
    var usedimage5: String,
    var userid: Int,
    var nickname: String?,
    var writedate: String?,
    var local: String?,
    var id: Int?,
    var price: String?,
    var userimage: String?
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

////유저 정보
//class UserEmail {
//    var userInfo: MutableList<UserId>? = null
//}

//함께해요 단체 및 홈화면
class GroupPlayTab {
    var groupwrite: MutableList<GroupPlay>? = null
}

//함께해요 개인
class AlonePlay {
    var individualwrite: MutableList<PlayWith>? = null
}

//커뮤니티
class FreeBoardTab {
    var boardwrite7: MutableList<Content>? = null
}

//커뮤니티
class FaqBoardTab {
    var boardwrite10: MutableList<Content>? = null
}

//커뮤니티
class QuestionBoardTab {
    var boardwrite9: MutableList<Content>? = null
}

//커뮤니티
class BallSportsTab {
    var boardwrite1: MutableList<Content>? = null
}

//커뮤니티
class LeisureTab {
    var boardwrite2: MutableList<Content>? = null
}

//커뮤니티
class LifeSportsTab {
    var boardwrite4: MutableList<Content>? = null
}

//커뮤니티
class WinterSportsTab {
    var boardwrite5: MutableList<Content>? = null
}

//커뮤니티
class WaterSportsTab {
    var boardwrite3: MutableList<Content>? = null
}

//커뮤니티
class ESportsTab {
    var boardwrite6: MutableList<Content>? = null
}

//중고거래
class ShopTab {
    var usedwrite: MutableList<Shop>? = null
}

/**
 * @GET 요청부분
 */
//뉴스
interface NewsService {
    @GET("v2/top-headlines?country=kr&category=sports&apiKey=68d1e20073a64ebeb16d8ff1399e61a8")
    fun getNewsList(): Call<NewsList>
}

////유저정보
//interface UserService {
//    @GET("php")
//    fun getUserInfo(): Call<UserEmail>
//}

//함께해요 단체면 및 홈화면
interface GroupPlayService {
    @GET("ribbon/.idea/server/apis/groupwritedb.php")
    fun getGroupPlay(): Call<GroupPlayTab>
}

//함께해요 개인
interface AlonePlayService {
    @GET("ribbon/.idea/server/apis/individualdb.php")
    fun getAlonePlay(): Call<AlonePlay>
}

//커뮤니티 자유게시판
interface FreeBoardService {
    @GET("ribbon/.idea/server/apis/category7board.php")
    fun getCommunity(): Call<FreeBoardTab>
}

//커뮤니티 질문게시판
interface QuestionService {
    @GET("ribbon/.idea/server/apis/category9board.php")
    fun getCommunity(): Call<QuestionBoardTab>
}

//커뮤니티 문의게시판
interface FaqService {
    @GET("ribbon/.idea/server/apis/category10board.php")
    fun getCommunity(): Call<FaqBoardTab>
}

//커뮤니티 구기종목
interface BallSportsService {
    @GET("ribbon/.idea/server/apis/category1board.php")
    fun getCommunity(): Call<BallSportsTab>
}

//커뮤니티 레져스포츠
interface LeisureSportsService {
    @GET("ribbon/.idea/server/apis/category2board.php")
    fun getCommunity(): Call<LeisureTab>
}

//커뮤니티 생활스포츠
interface LifeSportsService {
    @GET("ribbon/.idea/server/apis/category4board.php")
    fun getCommunity(): Call<LifeSportsTab>
}

//커뮤니티 동계스포츠
interface WinterSportsService {
    @GET("ribbon/.idea/server/apis/category5board.php")
    fun getCommunity(): Call<WinterSportsTab>
}

//커뮤니티 해양스포츠
interface WaterSportsService {
    @GET("ribbon/.idea/server/apis/category3board.php")
    fun getCommunity(): Call<WaterSportsTab>
}

//커뮤니티 이스포츠
interface ESportsService {
    @GET("ribbon/.idea/server/apis/category6board.php")
    fun getCommunity(): Call<ESportsTab>
}

//중고거래
interface ShopService {
    @GET("ribbon/.idea/server/apis/usedwritedb.php")
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
    @POST("ribbon/.idea/server/apis/boardwrite.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteContent>
}

interface WriteGroup {
    @POST("ribbon/.idea/server/apis/groupwrite.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteGroupPlay>
}

interface WriteAlone {
    @POST("ribbon/.idea/server/apis/individualwrite.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WritePlayWith>
}

interface WriteShopInfo {
    @POST("ribbon/.idea/server/apis/usedwrite.php")
    fun postContent(
        @Body params: HashMap<String, Any>
    ): Call<WriteShop>
}

var writeFlag = hashMapOf<String, Any>()

var titleHash = hashMapOf<String?, Any>()
var categoryHash = hashMapOf<String?, Any>()
var onceHash = hashMapOf<String?, Any>()
var localHash = hashMapOf<String?, Any>()
var lineHash = hashMapOf<String?, Any>()
var descriptionHash = hashMapOf<String?, Any>()
var nownumHash = hashMapOf<String?, Any>()
var peoplenumHash = hashMapOf<String?, Any>()
var genderHash = hashMapOf<String?, Any>()
var minageHash = hashMapOf<String?, Any>()
var maxageHash = hashMapOf<String?, Any>()
var titleImageHash = hashMapOf<String?, Any>()
var nicknameHash = hashMapOf<String?, Any>()
var writedateHash = hashMapOf<String?, Any>()
var userImageHash = hashMapOf<String?, Any>()
var priceHash = hashMapOf<String?, Any>()
var shopImageHash = hashMapOf<String?,Any>()
var shopImageHashTwo = hashMapOf<String?,Any>()
var shopImageHashThree = hashMapOf<String?,Any>()
var shopImageHashFour = hashMapOf<String?,Any>()
var shopImageHashFive = hashMapOf<String?,Any>()

