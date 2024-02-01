package com.plomba.zasilkovna.jobtask.domain.usecase.db

import com.plomba.zasilkovna.jobtask.data.database.dao.RepoDetailDao
import com.plomba.zasilkovna.jobtask.data.database.entity.toRepoDetail
import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import javax.inject.Inject


class DbRepoListUseCase @Inject constructor(
    private val gitHubRepository: GitHubRepository
): DatabaseUseCase<List<RepoDetail>?, Unit?>(){

    override suspend fun performDbUseCase(arg: Unit?): List<RepoDetail>? {
        return gitHubRepository.getAllLocalRepositories().map { it.toRepoDetail() }
    }
}

