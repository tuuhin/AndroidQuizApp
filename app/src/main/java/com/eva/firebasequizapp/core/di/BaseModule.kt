package com.eva.firebasequizapp.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BaseModule {
    // THIS MODULE CONTAIN THE BASE FIREBASE INSTANCES

    @Provides
    fun currentUser(auth:FirebaseAuth): FirebaseUser? = auth.currentUser

    @Provides
    fun getAuthInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun getFireStoreInstance(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun getCloudStorageInstance(): FirebaseStorage = FirebaseStorage.getInstance()
}