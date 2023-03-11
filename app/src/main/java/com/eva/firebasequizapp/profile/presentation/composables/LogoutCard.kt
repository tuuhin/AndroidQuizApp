package com.eva.firebasequizapp.profile.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.profile.presentation.UserLogoutEvents
import com.eva.firebasequizapp.profile.presentation.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutCard(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val isDialogOpen = viewModel.logoutDialog.value

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.onLogoutEvent(UserLogoutEvents.LogoutDialogCanceled) },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = { viewModel.onLogoutEvent(UserLogoutEvents.LogoutDialogAccepted) }
                ) {
                    Text(text = "Logout", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onLogoutEvent(UserLogoutEvents.LogoutDialogCanceled) }
                ) {
                    Text(text = "Canceled")
                }
            },
            title = { Text(text = "Logout", color = MaterialTheme.colorScheme.error) },
            text = { Text(text = "Are you sure to logout")
            }
        )
    }


    Card(
        modifier = modifier
            .padding(PaddingValues(top = 4.dp))
            .clickable { viewModel.onLogoutEvent(UserLogoutEvents.LogoutButtonClicked) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Logout,
                contentDescription = "Logout button",
                modifier = Modifier.weight(.2f),
                tint = MaterialTheme.colorScheme.error
            )
            Column(
                modifier = Modifier.weight(.8f)
            ) {
                Text(text = "Logout", color = MaterialTheme.colorScheme.error)
                Text(
                    text = "Logout the current user",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}