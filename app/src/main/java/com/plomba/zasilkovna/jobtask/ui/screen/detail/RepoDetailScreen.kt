package com.plomba.zasilkovna.jobtask.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plomba.zasilkovna.jobtask.R
import com.yazantarifi.compose.library.MarkdownConfig
import com.yazantarifi.compose.library.MarkdownViewComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    navController: NavController,
    viewModel: RepoDetailViewModel = hiltViewModel()
) {
    Box(Modifier
        .fillMaxSize()) {
        Column {
            TopAppBar(
                title = { Text(text = stringResource(R.string.repository_detail)) },
                navigationIcon = {

                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.Rounded.ArrowBack, "")
                    }
                })
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                if (viewModel.loadingState.value.loading) {
                    Text(text = "Loading...")
                } else if (viewModel.repoDetailState.value.error.isNotBlank()) {
                    Text(text = viewModel.repoDetailState.value.error)
                } else if (viewModel.repoDetailState.value.repo != null) {
                    val repo = viewModel.repoDetailState.value.repo!!

                    Column(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (repo.viewerHasStarred) {
                            Text(
                                modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                                text = "STARRED")
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                imageVector = Icons.Rounded.Star, contentDescription = "")
                            Button(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                onClick = { viewModel.removeStar() }) {
                                Text(text = "remove star")
                            }
                        } else {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                text = "NOT STARRED"
                            )
                            Button(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                onClick = { viewModel.addStar() }) {
                                Text(text = "add star")
                            }
                        }
                    }

                    DetailRow(propertyName = "name", propertyValue = repo.name)
                    DetailRow(propertyName = "owner", propertyValue = repo.owner)
                    DetailRow(propertyName = "created", propertyValue = repo.createdAt)
                    DetailRow(propertyName = "updated", propertyValue = repo.updatedAt)
                    DetailRow(propertyName = "repo ID", propertyValue = repo.id)
                    DetailRow(
                        propertyName = "disk usage",
                        propertyValue = repo.diskUsage?.toString()
                    )
                    DetailRow(
                        propertyName = "forking allowed",
                        propertyValue = repo.forkingAllowed.toString()
                    )
                    DetailRow(
                        propertyName = "stargazer count",
                        propertyValue = repo.stargazerCount.toString()
                    )
                    DetailRow(
                        propertyName = "languages",
                        propertyValue = repo.languages
                    )

                    if (!repo.readme.isNullOrBlank()) {
                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                        )
                        Divider()
                        Text(
                            modifier = Modifier
                                .background(Color.Blue)
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            text = "README",
                            fontSize = 16.sp
                        )
                        Divider()
                        MarkdownViewComposable(
                            modifier = Modifier.padding(16.dp),
                            content = repo.readme,
                            config = MarkdownConfig(
                                isLinksClickable = false,
                                isImagesClickable = false,
                                isScrollEnabled = false
                            ),
                            onLinkClickListener = { str, int -> {} }
                        )
                    }
                }
            }

        }
        if(viewModel.dbSaveState.value.messageResId != null) {
            val color = if(viewModel.dbSaveState.value.isError) Color.Red else Color.Green
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color)
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                val resId:Int? = viewModel.dbSaveState.value.messageResId
                Text(modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                    text = stringResource(id = resId!!))
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = { viewModel.confirmSaveState() }
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
            }
        }
    }
}