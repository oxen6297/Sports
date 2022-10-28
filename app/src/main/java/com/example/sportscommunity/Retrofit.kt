package com.example.sportscommunity

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

object RetrofitClient {
    var retrofit: Retrofit? = null

    fun getClient(baseUrl: String?): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

class Source {
    //json 파일에서 필요한 정보
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var url: String? = null
    var category: String? = null
    var language: String? = null
    var country: String? = null
}

class WebSite() {
    var status: String? = null
    var sources: List<Source>? = null
}

interface NewsService{
    @GET("v2/sources?apiKey=68d1e20073a64ebeb16d8ff1399e61a8")
    fun searchNews(
        @HeaderMap headers: Map<String,String>,
        @Query ("keyword") keyword:String
    ):Call<ArrayList<Source>>
}
