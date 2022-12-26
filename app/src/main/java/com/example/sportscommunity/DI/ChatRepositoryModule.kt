package com.example.sportscommunity.DI

import com.example.sportscommunity.Repository.ChatRepository
import com.example.sportscommunity.Repository.ChatRepositoryImpl
import com.example.sportscommunity.Retrofits
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ChatRepositoryModule {

    @Singleton
    @Provides
    fun provideChatRepository(): ChatRepository{
        return ChatRepositoryImpl()
    }
}