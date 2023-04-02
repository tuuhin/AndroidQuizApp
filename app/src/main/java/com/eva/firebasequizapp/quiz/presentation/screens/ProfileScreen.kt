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
import com.eva.firebasequizapp.profile.presentation.ChangeImageEvents
import com.eva.firebasequizapp.profile.presentation.ChangeNameEvent
import com.eva.firebasequizapp.profile.presentation.UserLogoutEvents
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

    LaunchedEffect(viewModel) {
        viewModel.messages
            .collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.title)
                    else -> {}
                }
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(id = R.string.profile_info),
                style = MaterialTheme.typography.bodyMedium,
            )
            viewModel.user?.let { user ->
                UserInfoCard(
                    user = user,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            ChangeUserNameSettings(
                state = viewModel.userNameState.value,
                user = viewModel.user,
                onToggle = {
                    viewModel.onChangeNameEvent(ChangeNameEvent.ToggleDialog)
                },
                onSubmit = {
                    viewModel.onChangeNameEvent(ChangeNameEvent.SubmitRequest)
                },
                onChange = {
                    viewModel.onChangeNameEvent(ChangeNameEvent.NameChanged(it))
                }
            )
            ChangeProfileImage(
                user = viewModel.user,
                state = viewModel.profileImageState.value,
                onSubmit = {
                    viewModel.onProfileChangeEvent(ChangeImageEvents.SubmitChanges)
                },
                onToggle = {
                    viewModel.onProfileChangeEvent(ChangeImageEvents.ToggleDialog)
                },
                onProfileChange = { results ->
                    results.uriContent?.let { uri ->
                        viewModel.onProfileChangeEvent(ChangeImageEvents.PickImage(uri))
                    }
                }
            )
            LogoutCard(
                showDialog = viewModel.logoutDialog.value,
                onLogoutButtonClicked = {
                    viewModel.onLogoutEvent(UserLogoutEvents.LogoutButtonClicked)
                },
                onDialogCanceled = {
                    viewModel.onLogoutEvent(UserLogoutEvents.LogoutDialogCanceled)
                },
                onDialogAccepted = {
                    viewModel.onLogoutEvent(UserLogoutEvents.LogoutDialogAccepted)
                }
            )
        }
    }
}