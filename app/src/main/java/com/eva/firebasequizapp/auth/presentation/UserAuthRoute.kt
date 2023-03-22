package com.eva.firebasequizapp.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.auth.presentation.screens.*
import com.eva.firebasequizapp.core.util.DialogContents
import com.eva.firebasequizapp.core.util.UiEvent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun UserAuthRoute(
    signInViewModel: UserSignInViewModel = hiltViewModel(),
    signUpViewModel: UserSignUpViewModel = hiltViewModel(),
    googleSignInViewModel: GoogleSignInViewModel = hiltViewModel()
) {
    val snackBarHost = remember { SnackbarHostState() }
    var dialogContents = remember { DialogContents("", null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    val pager = rememberPagerState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
    ) { padding ->

        LaunchedEffect(key1 = signUpViewModel) {
            signUpViewModel.authFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> {
                        snackBarHost.showSnackbar(event.title)
                    }
                    is UiEvent.ShowDialog -> {
                        isDialogOpen = true
                        dialogContents =
                            DialogContents(title = event.title, desc = event.description)
                    }
                }
            }
        }
        LaunchedEffect(key1 = signInViewModel) {
            signInViewModel.authFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> {
                        snackBarHost.showSnackbar(event.title)
                    }
                    is UiEvent.ShowDialog -> {
                        isDialogOpen = true
                        dialogContents =
                            DialogContents(title = event.title, desc = event.description)
                    }
                }
            }
        }
        LaunchedEffect(key1 = googleSignInViewModel) {
            googleSignInViewModel.googleAuthFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> {
                        snackBarHost.showSnackbar(event.title)
                    }
                    is UiEvent.ShowDialog -> {
                        isDialogOpen = true
                        dialogContents =
                            DialogContents(title = event.title, desc = event.description)
                    }
                }
            }
        }
        if (isDialogOpen) {
            AlertDialog(onDismissRequest = { isDialogOpen = !isDialogOpen },
                confirmButton = {
                    Button(onClick = { isDialogOpen = !isDialogOpen }) {
                        Text(text = "Ok I got it")
                    }
                },
                titleContentColor = MaterialTheme.colorScheme.primary,
                title = { Text(text = dialogContents.title) },
                text = { Text(text = dialogContents.desc ?: "") })
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                count = 2,
                state = pager
            ) {
                when (it) {
                    0 -> SignInScreen(pagerState = pager)
                    1 -> SignUpScreen(pagerState = pager)
                }
            }
        }

    }
}