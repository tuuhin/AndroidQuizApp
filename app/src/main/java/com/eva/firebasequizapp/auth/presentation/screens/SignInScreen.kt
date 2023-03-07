package com.eva.firebasequizapp.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.auth.presentation.UserSignInFormEvents
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SignInScreen(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    viewModel: UserSignInViewModel = hiltViewModel()
) {
    val state = viewModel.formState.value
    var isPasswordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)

    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(UserSignInFormEvents.EmailChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            maxLines = 1,
            isError = state.emailMessage != null,
            shape = RoundedCornerShape(10.dp),
            label = { Text("Email") },
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                errorIndicatorColor = Color.Transparent,
            ),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },

            modifier = if (state.emailMessage != null) Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.error,
                    RoundedCornerShape(10.dp)
                ) else Modifier
                .fillMaxWidth()
        )

        Box(modifier = Modifier.height(16.dp)) {
            state.emailMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        TextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(UserSignInFormEvents.PasswordChanged(it)) },
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                errorIndicatorColor = Color.Transparent,
            ),
            isError = state.passwordMessage != null,
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            maxLines = 1,
            shape = RoundedCornerShape(10.dp),
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.lock),
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    if (isPasswordVisible) Icon(
                        painterResource(id = R.drawable.eye),
                        contentDescription = "eye"
                    ) else Icon(
                        painterResource(id = R.drawable.eye_open),
                        contentDescription = "Eye open"
                    )
                }
            },
            modifier = if (state.passwordMessage != null) Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.error,
                    RoundedCornerShape(10.dp)
                ) else Modifier
                .fillMaxWidth()

        )
        Box(modifier = Modifier.height(16.dp)) {
            state.passwordMessage?.let {
                Text(
                    text = it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium

                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.onEvent(UserSignInFormEvents.FormSubmit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (viewModel.isLoading.value) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.padding(2.dp)
                )
            } else {
                Text(text = "Sign In")
            }
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Divider(color = MaterialTheme.colorScheme.secondary)
            Text(
                text = "OR",
                modifier = Modifier
                    .padding(8.dp, 2.dp)
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
        GoogleSignIn()
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(
            onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = MaterialTheme.colorScheme.secondary)
                    ) {
                        append("Don't have an account? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Sign up ")
                    }
                }
            )
        }

    }
}
