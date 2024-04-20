package com.example.sharedbasket.di

import com.example.sharedbasket.repository.AuthRepository
import com.example.sharedbasket.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun getFirebaseAuthRepository(repo : AuthRepositoryImpl) : AuthRepository

}