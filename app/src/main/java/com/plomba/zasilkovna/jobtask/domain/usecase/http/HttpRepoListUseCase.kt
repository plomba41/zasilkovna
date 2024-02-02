package com.plomba.zasilkovna.jobtask.domain.usecase.http

import com.plomba.zasilkovna.jobtask.data.remote.rest.RestRepoDto
import com.plomba.zasilkovna.jobtask.domain.model.RepoListItem
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import com.plomba.zasilkovna.jobtask.domain.usecase.BaseUseCase
import javax.inject.Inject


class HttpRepoListUseCase @Inject constructor(
    private val repository: GitHubRepository
): HttpUseCase<List<RepoListItem>, Int>(){
    
    override suspend fun performHttpUseCase(arg: Int?): List<RepoListItem>? {
        if (arg != null) {
            return repository.getAllRepos(arg).map { it.toRepoListItem() }
        } else {
            return null
        }
    }
}

fun RestRepoDto.toRepoListItem(): RepoListItem {
    return RepoListItem(
        name = name,
        owner = owner.login,
        id = id
    )
}

