package com.example.sportscommunity.repository

import retrofit2.Response

interface ChatRepository{
    suspend fun getChat(hashMap: HashMap<String,Any>,getComment:String):Response<String>
    suspend fun getReChat(hashMap: HashMap<String,Any>,getReComment:String):Response<String>
    suspend fun postChat(hashMap: HashMap<String,Any>,postComment:String):Response<String>
    suspend fun postReChat(hashMap: HashMap<String,Any>,postReComment:String):Response<String>
}