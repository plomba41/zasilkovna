package com.plomba.zasilkovna.jobtask.domain.usecase.http

import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import javax.inject.Inject

class RemoveStarUseCase @Inject constructor(
    private val repository: GitHubRepository
): StarUseCaseAbstract(){

    override suspend fun performHttpUseCase(arg: String?): Unit? {
        if (arg != null) {
            return repository.unstarRepo(arg)
        } else {
            return null
        }
    }
}
