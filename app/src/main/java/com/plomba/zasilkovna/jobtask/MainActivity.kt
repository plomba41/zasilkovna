package com.plomba.zasilkovna.jobtask

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plomba.zasilkovna.jobtask.common.Constants
import com.plomba.zasilkovna.jobtask.databinding.ActivityMainBinding
import com.plomba.zasilkovna.jobtask.ui.screen.BaseScreen
import com.plomba.zasilkovna.jobtask.ui.screen.localsavedrepos.LocalSavedReposScreen
import com.plomba.zasilkovna.jobtask.ui.screen.detail.RepoDetailScreen
import com.plomba.zasilkovna.jobtask.ui.screen.repolist.RepoListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = BaseScreen.RepoListScreen.route
            ) {
                composable(
                    route = BaseScreen.RepoListScreen.route
                ) {
                    RepoListScreen(baseContext, navController)
                }
                composable(
                    route = BaseScreen.LocalSavedReposScreen.route
                ) {
                    LocalSavedReposScreen(navController)
                }
                composable(
                    route = BaseScreen.RepoDetailScreen.route +
                            "/{${Constants.PARAM_OWNER_LOGIN}}" +
                            "/{${Constants.PARAM_REPO_NAME}}"
                ) {
                    RepoDetailScreen(navController)
                }
                composable(
                    route = BaseScreen.RepoDetailScreen.route +
                            "/{${Constants.PARAM_REPO_ID}}" +
                            "/{${Constants.PARAM_OWNER_LOGIN}}" +
                            "/{${Constants.PARAM_REPO_NAME}}"
                ) {
                    RepoDetailScreen(navController)
                }
            }
        }



    }
}