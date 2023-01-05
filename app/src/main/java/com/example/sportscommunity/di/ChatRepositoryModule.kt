package com.example.sportscommunity.di

import com.example.sportscommunity.repository.ChatRepository
import com.example.sportscommunity.repository.ChatRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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