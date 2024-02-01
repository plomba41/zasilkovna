package com.plomba.zasilkovna.jobtask.domain.usecase.http

import com.plomba.zasilkovna.jobtask.RepoDetailsQuery
import com.plomba.zasilkovna.jobtask.common.OwnerNameVariable
import com.plomba.zasilkovna.jobtask.domain.model.RepoDetail
import com.plomba.zasilkovna.jobtask.domain.repository.GitHubRepository
import javax.inject.Inject

class RepoDetailsUseCase @Inject constructor(
    private val repository: GitHubRepository
): HttpUseCase<RepoDetail, OwnerNameVariable>(){

    override suspend fun performHttpUseCase(arg: OwnerNameVariable?): RepoDetail? {
        if(arg != null){
            return repository.getRepoDetail(arg.owner, arg.name)?.repository?.toRepoDetail()
        } else {
            return null
        }
    }
}

fun RepoDetailsQuery.Repository.toRepoDetail(): RepoDetail {
    val readme:String?
    //trying to get README or README.md
    if(this.readme?.onBlob?.text != null){
        readme = this.readme.onBlob.text
    } else {
        readme = this.readmemd?.onBlob?.text
    }

    return RepoDetail(
        createdAt = createdAt.toString(),
        diskUsage = diskUsage,
        forkingAllowed = forkingAllowed,
        id = id,
        languages = languages?.nodes?.map { it?.name }?.joinToString("\n"),
        name = name,
        owner = owner.login,
        readme = readme,
        stargazerCount = stargazerCount,
        updatedAt = updatedAt.toString(),
        viewerHasStarred = viewerHasStarred
    )
}