package com.eva.firebasequizapp.profile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import com.eva.firebasequizapp.profile.data.repository.UserProfileRepoImpl
import com.eva.firebasequizapp.profile.domain.repository.UserProfileRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileModule {

    @Binds
    @ViewModelScoped
    abstract fun instance(repo: UserProfileRepoImpl): UserProfileRepository

}