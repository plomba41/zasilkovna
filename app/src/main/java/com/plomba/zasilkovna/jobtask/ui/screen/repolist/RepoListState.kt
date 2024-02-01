package com.plomba.zasilkovna.jobtask.ui.screen.repolist

import com.plomba.zasilkovna.jobtask.domain.model.RepoListItem

data class RepoListState(
    val isLoading: Boolean = false,
    val repos: List<RepoListItem> = emptyList(),
    val error: String = ""
)