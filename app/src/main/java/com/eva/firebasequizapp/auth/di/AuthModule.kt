package com.eva.firebasequizapp.auth.di

import com.eva.firebasequizapp.auth.data.UserAuthRepository
import com.eva.firebasequizapp.auth.data.UserAuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun fireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Module
    @InstallIn(ViewModelComponent::class)
    abstract class AuthRepository {

        @Binds
        @ViewModelScoped
        abstract fun getInstance(repositoryImpl: UserAuthRepositoryImpl): UserAuthRepository
    }
}