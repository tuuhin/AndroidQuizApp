package com.eva.firebasequizapp.quiz.di

import com.eva.firebasequizapp.quiz.data.repository.FullQuizRepoImpl
import com.eva.firebasequizapp.quiz.data.repository.QuizRepoImpl
import com.eva.firebasequizapp.quiz.data.repository.QuizResultRepoImpl
import com.eva.firebasequizapp.quiz.domain.repository.FullQuizRepository
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import com.eva.firebasequizapp.quiz.domain.repository.QuizResultsRepository
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

    @Binds
    @ViewModelScoped
    abstract fun getFullQuizRepo(impl: FullQuizRepoImpl): FullQuizRepository

    @Binds
    @ViewModelScoped
    abstract fun resultsRepo(impl: QuizResultRepoImpl): QuizResultsRepository

}