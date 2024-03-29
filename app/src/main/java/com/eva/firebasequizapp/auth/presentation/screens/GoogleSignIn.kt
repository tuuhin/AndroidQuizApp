package com.eva.firebasequizapp.auth.presentation.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.core.util.Resource
import com.google.android.gms.auth.api.identity.BeginSignInResult
import kotlinx.coroutines.launch

@Composable
fun GoogleSignIn(
    modifier: Modifier = Modifier,
    viewModel: GoogleSignInViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val clientId = stringResource(id = R.string.default_web_client_id)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        viewModel.activityResolverGoogleSignIn(result)
    }


    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    OutlinedButton(
        onClick = {
            if (!viewModel.isLoading.value)
                scope.launch {
                    when (val signIn = viewModel.signWithGoogle(clientId)) {
                        is Resource.Success -> {
                            launch(signIn.value!!)
                        }
                        else -> {
                            Log.d("SIGNING", signIn.message ?: "")
                        }
                    }
                }
        }, modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (viewModel.isLoading.value)
            CircularProgressIndicator(
                modifier = Modifier.padding(2.dp)
            )
        else {
            Icon(
                painter = painterResource(R.drawable.icons8_google),
                contentDescription = "Google Icon",
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Sign With Google")
        }
    }
}