package com.plomba.zasilkovna.jobtask.ui.screen.detail

import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.model.RepoListItem

data class RepoDetailState(
    val repo: RepoDetail? = null,
    val error: String = ""
)