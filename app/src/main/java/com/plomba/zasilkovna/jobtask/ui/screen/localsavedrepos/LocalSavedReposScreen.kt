package com.plomba.zasilkovna.jobtask.ui.screen.localsavedrepos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.plomba.zasilkovna.jobtask.R
import com.plomba.zasilkovna.jobtask.ui.screen.BaseScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalSavedReposScreen(
    navController: NavController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    localSavedReposViewModel: LocalSavedReposViewModel = hiltViewModel()
){
    var lifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            lifecycleEvent = event
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            localSavedReposViewModel.getRepos()
        }
    }


    val state = localSavedReposViewModel.state
    Column {
        TopAppBar(
            title = { Text(text = stringResource(R.string.local_repo_list_title)) },
            navigationIcon = {

                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.Rounded.ArrowBack, "")
                }
            })
        if (state.value.error.isNotBlank()) {
            Text(text = state.value.error)
        } else {

            LazyColumn {
                items(state.value.repos.size) { i ->
                    val repo = state.value.repos[i]
                    Column(modifier = Modifier
                        .clickable {
                            navController.navigate(BaseScreen.RepoDetailScreen.route + "/${repo.id}/${repo.owner}/${repo.name}")
                        }) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = repo.name
                        )
                        Divider()
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (state.value.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(25.dp)
                                    .height(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}