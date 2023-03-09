package com.eva.firebasequizapp.quiz

import com.eva.firebasequizapp.quiz.data.repository.QuizRepoImpl
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
object QuizModule {

    @Provides
    @Singleton
    fun fireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}


@Module
@InstallIn(ViewModelComponent::class)
abstract class QuizRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun getQuizRepoInstance(impl: QuizRepoImpl): QuizRepository

}