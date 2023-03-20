package com.eva.firebasequizapp.contribute_quiz.di

import com.eva.firebasequizapp.contribute_quiz.data.repository.CreateQuestionRepoImpl
import com.eva.firebasequizapp.contribute_quiz.data.repository.CreateQuizRepoImpl
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuestionsRepo
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class ContributeQuizModule {

    @Binds
    @ViewModelScoped
    abstract fun quizRepoInstance(repo: CreateQuizRepoImpl): CreateQuizRepository

    @Binds
    @ViewModelScoped
    abstract fun questionRepoInstance(repo: CreateQuestionRepoImpl):CreateQuestionsRepo
}