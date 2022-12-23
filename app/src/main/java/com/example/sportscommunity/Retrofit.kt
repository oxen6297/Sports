package com.example.sportscommunity

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.*

object Retrofits {
    private fun createRetrofit(baseUrl: String): Retrofit {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val yongWon = "http://112.148.33.214:8080/"

    //뉴스
    fun getNewsService(): NewsService {
        return createRetrofit("https://newsapi.org/").create(NewsService::class.java)
    }

    fun getCommentService(): CommentService{
        return createRetrofit(yongWon).create(CommentService::class.java)
    }
    fun getReCommentService(): ReCommentService{
        return createRetrofit(yongWon).create(ReCommentService::class.java)
    }

    //함께해요 단체 및 홈화면
    fun getGroupPlayService(): GroupPlayService {
        return createRetrofit(yongWon).create(GroupPlayService::class.java)
    }

    //함께해요 개인
    fun getAlonePlayService(): AlonePlayService {
        return createRetrofit(yongWon).create(AlonePlayService::class.java)
    }

    //커뮤니티 자유게시판
    fun getFreeBoardService(): FreeBoardService {
        return createRetrofit(yongWon).create(FreeBoardService::class.java)
    }

    //중고거래
    fun getShopService(): ShopService {
        return createRetrofit(yongWon).create(ShopService::class.java)
    }

    fun getBestBoardService(): BestBoardService {
        return createRetrofit(yongWon).create(BestBoardService::class.java)
    }

    fun postUserInfo(): UserInfo {
        return createRetrofit(yongWon).create(UserInfo::class.java)
    }

    fun postCommunity(): WriteCommunity {
        return createRetrofit(yongWon).create(WriteCommunity::class.java)
    }

    fun postGroup(): WriteGroup {
        return createRetrofit(yongWon).create(WriteGroup::class.java)
    }

    fun postAlone(): WriteAlone {
        return createRetrofit(yongWon).create(WriteAlone::class.java)
    }

    fun postShop(): WriteShopInfo {
        return createRetrofit(yongWon).create(WriteShopInfo::class.java)
    }

    fun postLikeNumber(): LikeNum {
        return createRetrofit(yongWon).create(LikeNum::class.java)
    }

    fun postAloneLikeNumber(): AloneLikeNum {
        return createRetrofit(yongWon).create(AloneLikeNum::class.java)
    }

    fun postShopLikeNumber(): ShopLikeNum {
        return createRetrofit(yongWon).create(ShopLikeNum::class.java)
    }

    fun communityDeleteLikeNumber(): DeleteService {
        return createRetrofit(yongWon).create(DeleteService::class.java)
    }

    fun aloneDeleteLikeNumber(): AloneDeleteService {
        return createRetrofit(yongWon).create(AloneDeleteService::class.java)
    }

    fun shopDeleteLikeNumber(): ShopDeleteService {
        return createRetrofit(yongWon).create(ShopDeleteService::class.java)
    }

    fun postComLikeCount(): LikeCount {
        return createRetrofit(yongWon).create(LikeCount::class.java)
    }

    fun postAloneLikeCount(): AloneLikeCount {
        return createRetrofit(yongWon).create(AloneLikeCount::class.java)
    }

    fun postShopLikeCount(): ShopLikeCount {
        return createRetrofit(yongWon).create(ShopLikeCount::class.java)
    }

    fun postComments(): Comments{
        return createRetrofit(yongWon).create(Comments::class.java)
    }

    fun postReComments(): ReComments{
        return createRetrofit(yongWon).create(ReComments::class.java)
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

data class Comment(
    var nickname: String?,
    var description: String?,
    var writedate: String?,
    var inherentid: Int?,
    var categoryid: Int?,
    var userId: Int?,
    var profileimage: String?,
    var isrecomment: Int,
    var commentsid: Int?,
    var likedcount: Int?,
    var replyid: Int?
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
    var writedate: String?,
    var groupid: Int?
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
    var individualid: Int?,
    var likedcount: String?
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
    var boardid: Int?,
    var title: String?,
    var writedate: String?,
    var profileimage: String?,
    var nickname: String?,
    var img: String?,
    var categoryid: Int?,
    var userid: Int?,
    var description: String?,
    var likedcount: String?,
    var commentcount: String?
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
    var userimage: String?,
    var usedid: Int?,
    var likedcount: String?
)

data class BestBoard(
    var nickname: String?,
    var writedate: String?,
    var boardid: Int?,
    var title: String?,
    var likedcount: Int?,
    var profileimage: String?,
    var description: String?,
    var id: Int?
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

class Like(
    var postid: Int?,
    var categoryId: Int?,
    var userId: Int?,
    var likedid: Int?
)

class LikeCountNum(
    var inherentid: Int?,
    var categoryid: Int?
)

data class GetLikeCountNum(
    var likedCount: Int?
)


//뉴스
class NewsList {
    var articles: MutableList<News>? = null
}

class BestBoardTab {
    var bestwrite: MutableList<BestBoard>? = null
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
    var boardwrite1: MutableList<Content>? = null
    var boardwrite2: MutableList<Content>? = null
    var boardwrite3: MutableList<Content>? = null
    var boardwrite4: MutableList<Content>? = null
    var boardwrite5: MutableList<Content>? = null
    var boardwrite6: MutableList<Content>? = null
    var boardwrite9: MutableList<Content>? = null
    var boardwrite10: MutableList<Content>? = null
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
    suspend fun getNewsList(): Response<NewsList>
}

interface BestBoardService {
    @GET("ribbon/.idea/server/realtime/realtimeup.php")
    suspend fun getBestBoard(): Response<BestBoardTab>
}

////유저정보
//interface UserService {
//    @GET("php")
//    fun getUserInfo(): Call<UserEmail>
//}

//함께해요 단체면 및 홈화면
interface GroupPlayService {
    @GET("ribbon/.idea/server/apis/groupwritedb.php")
    suspend fun getGroupPlay(): Response<GroupPlayTab>
}

//함께해요 개인
interface AlonePlayService {
    @GET("ribbon/.idea/server/apis/individualdb.php")
    suspend fun getAlonePlay(): Response<AlonePlay>
}

//커뮤니티 자유게시판
interface FreeBoardService {
    @GET("/ribbon/.idea/server/categoryboard/categoryboard{number}.php")
    suspend fun getCommunity(
        @Path("number") number:String
    ): Response<FreeBoardTab>
}

//중고거래
interface ShopService {
    @GET("ribbon/.idea/server/apis/usedwritedb.php")
    suspend fun getShop(): Response<ShopTab>
}

/**
 * @POST 요청부분
 */

interface CommentService {
    @POST("ribbon/.idea/server/comreply/commentsinfo.php")
    fun getComment(
        @Body params: HashMap<String,Any>
    ): Call<String>
}

interface ReCommentService {
    @POST("ribbon/.idea/server/comreply/replyinfo.php")
    fun getComment(
        @Body params: HashMap<String,Any>
    ): Call<String>
}

interface Comments{
    @POST("ribbon/.idea/server/comreply/commentswrite.php")
    fun postComment(
        @Body params: HashMap<String,Any>
    ):Call<String>
}

interface ReComments{
    @POST("ribbon/.idea/server/comreply/replywrite.php")
    fun postReComment(
        @Body params: HashMap<String,Any>
    ):Call<String>
}

interface LikeCount {
    @POST("ribbon/.idea/server/liked/likedcount.php")
    fun postLikeCount(
        @Body params: HashMap<String, Any>
    ): Call<String>
}

interface AloneLikeCount {
    @POST("ribbon/.idea/server/liked/individuallikedcount.php")
    fun postLikeCount(
        @Body params: HashMap<String, Any>
    ): Call<String>
}

interface ShopLikeCount {
    @POST("ribbon/.idea/server/liked/usedlikedcount.php")
    fun postLikeCount(
        @Body params: HashMap<String, Any>
    ): Call<String>
}

interface LikeNum {
    @POST("ribbon/.idea/server/liked/likedwrite.php")
    fun postLike(
        @Body params: HashMap<String, Any>
    ): Call<String>
}

interface AloneLikeNum {
    @POST("ribbon/.idea/server/liked/individuallikedwrite.php")
    fun postLike(
        @Body params: HashMap<String, Any>
    ): Call<String>
}

interface ShopLikeNum {
    @POST("ribbon/.idea/server/liked/usedlikedwrite.php")
    fun postLike(
        @Body params: HashMap<String, Any>
    ): Call<String>
}

interface UserInfo {
    @POST("ribbon/.idea/server/apis/sign2.php")
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

interface DeleteService {
    @HTTP(method = "DELETE", hasBody = true, path = "ribbon/.idea/server/liked/likeddelete.php")
    fun comDeleteLike(
        @Body params: HashMap<String, Any>
    ): Call<String>
}

interface AloneDeleteService {
    @HTTP(
        method = "DELETE",
        hasBody = true,
        path = "ribbon/.idea/server/liked/individualdelete.php"
    )
    fun aloneDeleteLike(
        @Body params: HashMap<String, Any>
    ): Call<Like>
}

interface ShopDeleteService {
    @HTTP(method = "DELETE", hasBody = true, path = "ribbon/.idea/server/liked/useddelete.php")
    fun shopDeleteLike(
        @Body params: HashMap<String, Any>
    ): Call<Like>
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
var shopImageHash = hashMapOf<String?, Any>()
var shopImageHashTwo = hashMapOf<String?, Any>()
var shopImageHashThree = hashMapOf<String?, Any>()
var shopImageHashFour = hashMapOf<String?, Any>()
var shopImageHashFive = hashMapOf<String?, Any>()
var FreeBoardId = hashMapOf<String?, Any>()
var QuestionBoardId = hashMapOf<String?, Any>()
var FaqBoardId = hashMapOf<String?, Any>()
var BallBoardId = hashMapOf<String?, Any>()
var LeisureBoardId = hashMapOf<String?, Any>()
var LifeBoardId = hashMapOf<String?, Any>()
var WaterBoardId = hashMapOf<String?, Any>()
var WinterBoardId = hashMapOf<String?, Any>()
var ESportsId = hashMapOf<String?, Any>()
var ShopBoardId = hashMapOf<String?, Any>()
var AloneBoardId = hashMapOf<String?, Any>()
var AloneCategoryHash = hashMapOf<String?, Any>()
var commentsId = hashMapOf<String?, Any>()

