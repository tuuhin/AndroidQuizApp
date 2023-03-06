package com.eva.firebasequizapp.auth.di

import android.content.Context
import com.eva.firebasequizapp.auth.data.UserAuthRepository
import com.eva.firebasequizapp.auth.data.UserAuthRepositoryImpl
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun fireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun signInClient(@ApplicationContext appContext: Context): SignInClient =
        Identity.getSignInClient(appContext)

    @Module
    @InstallIn(ViewModelComponent::class)
    abstract class AuthRepository {

        @Binds
        @ViewModelScoped
        abstract fun getInstance(repositoryImpl: UserAuthRepositoryImpl): UserAuthRepository
    }
}