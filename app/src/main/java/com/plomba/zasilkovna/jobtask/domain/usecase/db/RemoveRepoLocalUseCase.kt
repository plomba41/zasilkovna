package com.plomba.zasilkovna.jobtask.domain.usecase.db

import com.plomba.zasilkovna.jobtask.data.database.dao.RepoDetailDao
import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.model.toRepoDetailEntity
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import javax.inject.Inject

class RemoveRepoLocalUseCase @Inject constructor(
    val gitHubRepository: GitHubRepository
): DatabaseUseCase<Int, RepoDetail?>() {
    override suspend fun performDbUseCase(arg: RepoDetail?): Int? {
        if (arg != null) {
            val result = gitHubRepository.removeLocalRepository(arg.toRepoDetailEntity())
            if(result == 0){
                return null
            } else {
                return result
            }
        } else {
            return null
        }
    }
}