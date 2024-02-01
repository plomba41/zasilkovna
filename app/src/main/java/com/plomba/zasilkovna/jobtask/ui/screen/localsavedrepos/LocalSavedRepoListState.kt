package com.plomba.zasilkovna.jobtask.ui.screen.localsavedrepos

import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.model.RepoListItem

data class LocalSavedRepoListState(
    val isLoading: Boolean = false,
    val repos: List<RepoDetail> = emptyList(),
    val error: String = ""
)