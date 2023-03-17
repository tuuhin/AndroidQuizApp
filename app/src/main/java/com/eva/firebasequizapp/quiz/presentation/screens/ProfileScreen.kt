package com.eva.firebasequizapp.quiz.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.profile.presentation.UserProfileViewModel
import com.eva.firebasequizapp.profile.presentation.composables.ChangeProfileImage
import com.eva.firebasequizapp.profile.presentation.composables.ChangeUserNameSettings
import com.eva.firebasequizapp.profile.presentation.composables.LogoutCard
import com.eva.firebasequizapp.profile.presentation.composables.UserInfoCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->

        LaunchedEffect(key1 = true) {
            viewModel.messages.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> {
                        snackBarHostState.showSnackbar(event.title)
                    }
                    else -> {}
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .padding(padding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Your Profile", style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            viewModel.user?.let { user ->
                UserInfoCard(user = user)
            }
            Text(
                text = stringResource(id = R.string.profile_info),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(0.dp, 4.dp)
            )
            ChangeUserNameSettings()
            ChangeProfileImage()
            LogoutCard()
        }
    }
}