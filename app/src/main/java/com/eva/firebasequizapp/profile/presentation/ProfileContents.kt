package com.eva.firebasequizapp.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.profile.presentation.composables.ChangeProfileImage
import com.eva.firebasequizapp.profile.presentation.composables.ChangeUserNameSettings
import com.eva.firebasequizapp.profile.presentation.composables.LogoutCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContents(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Card {
            Column(
                modifier = Modifier.padding(8.dp, 0.dp)
            ) {
                ChangeUserNameSettings()
                Divider()
                ChangeProfileImage()
            }
        }
        LogoutCard()
    }

}