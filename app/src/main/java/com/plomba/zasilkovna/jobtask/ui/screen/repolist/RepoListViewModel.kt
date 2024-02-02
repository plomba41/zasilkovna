package com.plomba.zasilkovna.jobtask.ui.screen.repolist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plomba.zasilkovna.jobtask.common.Constants
import com.plomba.zasilkovna.jobtask.common.Resource
import com.plomba.zasilkovna.jobtask.domain.model.RepoListItem
import com.plomba.zasilkovna.jobtask.domain.usecase.http.HttpRepoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val repoListUseCase: HttpRepoListUseCase
)
    : ViewModel() {

    private val _state = mutableStateOf(RepoListState())
    val state: State<RepoListState> = _state
    private val _startFrom = MutableLiveData<Int>(0)
    val startFrom: LiveData<Int> = _startFrom

    init {
        getRepos(0)
    }

    fun searchFrom(since: Int){
        _startFrom.postValue(since)
        _state.value = RepoListState()
        getRepos(since)
    }

    fun getRepos(since: Int) {
        if (!state.value.isLoading) {
            repoListUseCase(since).onEach { result ->
                val repoList: MutableList<RepoListItem> = mutableListOf()
                repoList.addAll(state.value.repos)
                when (result) {
                    is Resource.Success -> {
                        repoList.addAll(result.data ?: emptyList())
                        _state.value = RepoListState(repos = repoList)
                    }

                    is Resource.Error -> {
                        _state.value = RepoListState(
                            repos = repoList,
                            error = result.message ?: Constants.UNEXPECTED_ERROR
                        )
                    }

                    is Resource.Loading -> {

                        _state.value = RepoListState(
                            repos = repoList,
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    /*private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _state = MutableStateFlow(emptyList<AllReposQuery.Edge?>())
    val state: StateFlow<List<AllReposQuery.Edge?>>
        get() = _state


    init {
        val apolloClient = ApolloClient.Builder()

            .serverUrl("https://api.github.com/graphql")
            .addHttpHeader(
                "Authorization",
                "bearer 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"
            ).addHttpHeader(
                "X-Github-Next-Global-ID",
                "1"
            )

            .build()
        viewModelScope.launch {
                try {
                    val response = apolloClient.query(AllReposQuery()).execute()
                    Log.d("LaunchList", "Success ${response.data}")
                    _state.value = response.data!!.search.edges!!

                } catch (e: Exception) {
                }
        }


    }*/
}