package com.plomba.zasilkovna.jobtask.ui.screen.localsavedrepos

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plomba.zasilkovna.jobtask.common.Constants
import com.plomba.zasilkovna.jobtask.common.Resource
import com.plomba.zasilkovna.jobtask.domain.usecase.db.DbRepoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LocalSavedReposViewModel @Inject constructor(
    private val dbRepoListUseCase: DbRepoListUseCase
) : ViewModel() {

    private val _state = mutableStateOf(LocalSavedRepoListState())
    val state: State<LocalSavedRepoListState> = _state

    fun getRepos() {
        if (!state.value.isLoading) {
            dbRepoListUseCase().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = LocalSavedRepoListState(repos = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.value = LocalSavedRepoListState(
                            error = result.message ?: Constants.UNEXPECTED_ERROR
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = LocalSavedRepoListState(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}