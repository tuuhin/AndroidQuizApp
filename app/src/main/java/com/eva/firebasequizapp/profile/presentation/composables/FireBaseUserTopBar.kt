package com.eva.firebasequizapp.profile.presentation.composables

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.profile.presentation.UserProfileViewModel
import com.eva.firebasequizapp.quiz.util.TabsInfo
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FireBaseUserTopBar(
    pager: PagerState,
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val scope = rememberCoroutineScope()

    val user = viewModel.user
    SmallTopAppBar(modifier = modifier.padding(10.dp, 0.dp),
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(
                onClick = { scope.launch { pager.scrollToPage(TabsInfo.ProfileTab.index) } }
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(2.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        )
                        .clip(MaterialTheme.shapes.small),
                    contentAlignment = Alignment.Center
                ) {
                    if (user?.photoUrl != null)
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(user.photoUrl).build(),
                            contentDescription = "User photo",
                            contentScale = ContentScale.Inside,
                        )
                    else
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User photo placeholder",
                        )
                }
            }
        }

    )
}