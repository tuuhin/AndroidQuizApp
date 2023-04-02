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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutCard(
    showDialog: Boolean,
    onLogoutButtonClicked: () -> Unit,
    onDialogAccepted: () -> Unit,
    onDialogCanceled: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDialogCanceled,
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    onClick = onDialogAccepted
                ) {
                    Text(text = "Logout", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDialogCanceled
                ) {
                    Text(text = "Cancel", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            },
            title = { Text(text = "Logout", color = MaterialTheme.colorScheme.error) },
            text = {
                Text(text = "Are you sure to logout")
            }
        )
    }

    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable(onClick = onLogoutButtonClicked),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Logout,
                contentDescription = "Logout button",
                modifier = Modifier.weight(.2f),
            )
            Column(
                modifier = Modifier.weight(.8f)
            ) {
                Text(text = "Logout", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Logout the current user",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}