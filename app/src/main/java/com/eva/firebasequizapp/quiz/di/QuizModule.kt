package com.eva.firebasequizapp.quiz.di

import com.eva.firebasequizapp.quiz.data.repository.QuizRepoImpl
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class QuizRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun getQuizRepoInstance(impl: QuizRepoImpl): QuizRepository

}