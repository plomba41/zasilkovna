package com.plomba.zasilkovna.jobtask.domain.usecase.db

import com.plomba.zasilkovna.jobtask.data.database.dao.RepoDetailDao
import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.model.toRepoDetailEntity
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import javax.inject.Inject

class SaveRepoLocalUseCase @Inject constructor(
    val gitHubRepository: GitHubRepository
): DatabaseUseCase<Long, RepoDetail?>() {
    override suspend fun performDbUseCase(arg: RepoDetail?): Long? {
        if (arg != null) {
            return gitHubRepository.saveRepositoryLocaly(arg.toRepoDetailEntity())
        } else {
            return null
        }
    }
}