package com.plomba.zasilkovna.jobtask.ui.screen


sealed class BaseScreen(val route: String) {
    object RepoListScreen: BaseScreen("repo_list_screen")
    object LocalSavedReposScreen: BaseScreen("local_saved_repos_screen")
    object RepoDetailScreen: BaseScreen("repo_detail_screen")
}
