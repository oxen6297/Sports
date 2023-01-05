package com.example.sportscommunity.repository

import com.example.sportscommunity.Retrofits
import retrofit2.Response

class ChatRepositoryImpl:ChatRepository {

    override suspend fun getChat(hashMap:HashMap<String,Any>,getComment:String): Response<String> {
        return Retrofits.getCommentService().getComment(hashMap,getComment)
    }

    override suspend fun getReChat(hashMap:HashMap<String,Any>,getReComment:String): Response<String> {
        return Retrofits.getReCommentService().getComment(hashMap,getReComment)
    }

    override suspend fun postChat(hashMap:HashMap<String,Any>,postComment:String): Response<String> {
        return Retrofits.postComments().postComment(hashMap,postComment)
    }

    override suspend fun postReChat(hashMap:HashMap<String,Any>,postReComment:String): Response<String> {
        return Retrofits.postReComments().postReComment(hashMap,postReComment)
    }
}