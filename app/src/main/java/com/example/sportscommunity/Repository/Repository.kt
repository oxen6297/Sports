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
}