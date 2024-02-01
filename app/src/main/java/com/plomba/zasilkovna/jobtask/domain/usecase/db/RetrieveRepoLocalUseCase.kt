package com.plomba.zasilkovna.jobtask.domain.usecase.db

import com.plomba.zasilkovna.jobtask.data.database.dao.RepoDetailDao
import com.plomba.zasilkovna.jobtask.data.database.entity.toRepoDetail
import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import javax.inject.Inject

class RetrieveRepoLocalUseCase @Inject constructor(
    val gitHubRepository: GitHubRepository
): DatabaseUseCase<RepoDetail, String>() {
    override suspend fun performDbUseCase(arg: String?): RepoDetail? {
        if (arg != null) {
            return gitHubRepository.retrieveLocalRepository(arg).toRepoDetail()
        } else {
            return null
        }
    }
}