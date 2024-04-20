package com.example.sharedbasket.di

import com.example.sharedbasket.notification.FCMNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedBasketModule {
    @Provides
    @Singleton
    fun getFCMNotificationManager() : FCMNotificationManager = FCMNotificationManager()
}