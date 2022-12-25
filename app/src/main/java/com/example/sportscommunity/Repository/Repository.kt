package com.example.sportscommunity.Repository

import com.example.sportscommunity.*
import retrofit2.Response

class Repository {
    suspend fun getNews(): Response<NewsList> {
        return Retrofits.getNewsService().getNewsList()
    }

    suspend fun getGroup(): Response<GroupPlayTab> {
        return Retrofits.getGroupPlayService().getGroupPlay()
    }

    suspend fun getAlone(): Response<AlonePlay> {
        return Retrofits.getAlonePlayService().getAlonePlay()
    }

    suspend fun getShop(): Response<ShopTab> {
        return Retrofits.getShopService().getShop()
    }

    suspend fun getBestBoard(): Response<BestBoardTab> {
        return Retrofits.getBestBoardService().getBestBoard()
    }

    suspend fun getCommunity(number: String):Response<FreeBoardTab> {
        return Retrofits.getFreeBoardService().getCommunity(number)
    }

    suspend fun writeAlone(hashMap: HashMap<String,Any>):Response<WritePlayWith> {
        return Retrofits.postAlone().postContent(hashMap)
    }

    suspend fun writeGroup(hashMap: HashMap<String, Any>):Response<WriteGroupPlay>{
        return Retrofits.postGroup().postContent(hashMap)
    }

    suspend fun writeShop(hashMap: HashMap<String, Any>):Response<WriteShop>{
        return Retrofits.postShop().postContent(hashMap)
    }

    suspend fun writeCommunity(hashMap: HashMap<String, Any>):Response<WriteContent>{
        return Retrofits.postCommunity().postContent(hashMap)
    }
}