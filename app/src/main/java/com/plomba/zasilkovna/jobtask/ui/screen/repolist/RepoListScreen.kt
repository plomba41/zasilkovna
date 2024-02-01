package com.plomba.zasilkovna.jobtask.ui.screen.repolist

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plomba.zasilkovna.jobtask.R
import com.plomba.zasilkovna.jobtask.ui.screen.BaseScreen
import dagger.hilt.android.qualifiers.ApplicationContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RepoListScreen(
    @ApplicationContext context: Context,
    navController: NavController,
    repoListViewModel: RepoListViewModel = hiltViewModel()
){
    val keyBoarController = LocalSoftwareKeyboardController.current
    val state = repoListViewModel.state
    var text by remember { mutableStateOf(repoListViewModel.startFrom.value.toString()) }

    Column {
        TopAppBar(
            title = { Text(text = stringResource(R.string.repo_list_title)) },
            )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
            navController.navigate(BaseScreen.LocalSavedReposScreen.route)
        }) {
            Text(text = stringResource(R.string.show_localy_stored_repositories))
        }
        TextField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = text,
            onValueChange = { text = it })
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = {
            try {
                repoListViewModel.searchFrom(text.toInt())
                keyBoarController?.hide()
            } catch (e: NumberFormatException){
                Toast.makeText(context, "Type a number", Toast.LENGTH_LONG).show()
            }

        }) {
            Text(text = "Search from selected Id")
        }
        if (state.value.error.isNotBlank()) {
            Text(text = state.value.error)
        } else {

            LazyColumn {
                items(state.value.repos.size) { i ->
                    val repo = state.value.repos.get(i)
                    if (i >= state.value.repos.size - 1) {
                        repoListViewModel.getRepos(repo.id)
                    }
                    Column(modifier = Modifier
                        .clickable {
                            navController.navigate(
                                BaseScreen.RepoDetailScreen.route + "/${repo.owner}/${repo.name}"
                            )
                        }) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "(id: "
                                    + repo.id.toString()
                                    + ") "
                                    + repo.name
                                    + "/"
                                    + repo.owner
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