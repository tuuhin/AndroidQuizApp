package com.eva.firebasequizapp.profile.domain.repository

import android.net.Uri
import com.eva.firebasequizapp.core.util.Resource

interface UserProfileRepository {
    suspend fun updateUserName(name: String): Resource<Unit>
    suspend fun updateImage(uri: Uri): Resource<Unit>
}