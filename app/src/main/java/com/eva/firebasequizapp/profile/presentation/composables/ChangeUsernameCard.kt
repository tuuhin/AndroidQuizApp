package com.eva.firebasequizapp.profile.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.profile.presentation.ChangeNameEvent
import com.eva.firebasequizapp.profile.presentation.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeUserNameSettings(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val isDialogOpen = viewModel.userNameState.value.isDialogOpen
    val user = viewModel.user

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.onChangeNameEvent(ChangeNameEvent.ToggleDialog) },
            confirmButton = {
                Button(
                    onClick = { viewModel.onChangeNameEvent(ChangeNameEvent.SubmitRequest) }
                ) {
                    Text(text = "Change")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onChangeNameEvent(ChangeNameEvent.ToggleDialog) }
                ) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "Change UserName") },
            text = {
                Column(
                    modifier = Modifier.wrapContentHeight()
                ) {
                    TextField(
                        value = viewModel.userNameState.value.name,
                        isError = viewModel.userNameState.value.error != null,
                        onValueChange = {
                            viewModel.onChangeNameEvent(ChangeNameEvent.NameChanged(it))
                        },
                        placeholder = { Text(text = "New username") },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            errorIndicatorColor = Color.Transparent,
                        ),
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(16.dp)
                    ) {
                        viewModel.userNameState.value.error?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        )
    }
    Card(
        modifier = modifier.padding(PaddingValues(top = 4.dp))
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(.75f)
            ) {
                Text(text = "Change UserName", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Don't like the current name,change it if so.",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            TextButton(
                onClick = { viewModel.onChangeNameEvent(ChangeNameEvent.ToggleDialog) },
                modifier = Modifier.weight(.25f)
            ) {
                Text(text = if (user?.displayName != null) "Change" else "Set")
            }
        }

    }
}