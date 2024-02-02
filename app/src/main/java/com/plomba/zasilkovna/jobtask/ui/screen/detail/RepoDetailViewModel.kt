package com.plomba.zasilkovna.jobtask.ui.screen.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plomba.zasilkovna.jobtask.R
import com.plomba.zasilkovna.jobtask.common.Constants
import com.plomba.zasilkovna.jobtask.common.OwnerNameVariable
import com.plomba.zasilkovna.jobtask.common.Resource
import com.plomba.zasilkovna.jobtask.domain.usecase.http.AddStarUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.db.RemoveRepoLocalUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.http.RemoveStarUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.http.HttpRepoDetailsUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.db.RetrieveRepoLocalUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.db.SaveRepoLocalUseCase
import com.plomba.zasilkovna.jobtask.domain.usecase.http.StarUseCaseAbstract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    private val repoDetailsUseCase: HttpRepoDetailsUseCase,
    private val addStarUseCase: AddStarUseCase,
    private val removeStarUseCase: RemoveStarUseCase,
    private val saveRepoLocalUseCase: SaveRepoLocalUseCase,
    private val removeRepoLocalUseCase: RemoveRepoLocalUseCase,
    private val retrieveRepoLocalUseCase: RetrieveRepoLocalUseCase,
    private val savedStateHandle: SavedStateHandle
)
    : ViewModel() {

    private val _repoDetailState = mutableStateOf(RepoDetailState())
    val repoDetailState: State<RepoDetailState> = _repoDetailState
    private val _loadingState = mutableStateOf(LoadingState())
    val loadingState: State<LoadingState> = _loadingState
    private val _dbSaveState = mutableStateOf(DbSaveState())
    val dbSaveState: State<DbSaveState> = _dbSaveState

    val currentId: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        val repoId = savedStateHandle.get<String>(Constants.PARAM_REPO_ID)
        if(repoId != null){
            getRepoDetailLocal(repoId)
        } else {
            loadRemote {}
        }
    }

    fun loadRemote(onSuccess: () -> Unit){
        val owner = savedStateHandle.get<String>(Constants.PARAM_OWNER_LOGIN)
        val name = savedStateHandle.get<String>(Constants.PARAM_REPO_NAME)
        getRepoDetailRemote(owner!!, name!!, onSuccess)
    }

    fun addStar(){
        performStarAction(addStarUseCase) {
            loadRemote {
                performSaveToDatabase()
            }
        }
    }

    fun removeStar(){
        performStarAction(removeStarUseCase) {
            loadRemote {
                performRemoveFromDatabase()
            }
        }
    }

    fun confirmSaveState(){
        _dbSaveState.value = DbSaveState()
    }

    fun getRepoDetailLocal(repoId: String) {
        if (!loadingState.value.loading) {
            retrieveRepoLocalUseCase(repoId).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _loadingState.value = LoadingState(false)
                        _repoDetailState.value = RepoDetailState(repo = result.data)
                        currentId.postValue(result.data!!.id)
                    }
                    is Resource.Error -> {
                        _loadingState.value = LoadingState(loading = false)
                        _repoDetailState.value = RepoDetailState(
                            error = result.message ?: Constants.UNEXPECTED_ERROR
                        )
                    }
                    is Resource.Loading -> {
                        _loadingState.value = LoadingState(loading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getRepoDetailRemote(owner: String, name: String, onSuccess: () -> Unit) {
        if (!loadingState.value.loading) {
            repoDetailsUseCase(OwnerNameVariable(owner, name)).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _loadingState.value = LoadingState(false)
                        _repoDetailState.value = RepoDetailState(repo = result.data)
                        currentId.postValue(result.data!!.id)
                        onSuccess()
                    }
                    is Resource.Error -> {
                        _loadingState.value = LoadingState(loading = false)
                        _repoDetailState.value = RepoDetailState(
                            error = result.message ?: Constants.UNEXPECTED_ERROR
                        )
                    }
                    is Resource.Loading -> {
                        _loadingState.value = LoadingState(loading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun performStarAction(starUseCase: StarUseCaseAbstract, onSuccess: () -> Unit) {
        val savedId = currentId.value
        if (savedId != null) {
            starUseCase(savedId).onEach {result ->
                when (result) {
                    is Resource.Success -> {
                        _loadingState.value = LoadingState(loading = false)
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _loadingState.value = LoadingState(loading = false)
                        _repoDetailState.value = RepoDetailState(
                            error = result.message ?: Constants.UNEXPECTED_ERROR
                        )
                    }

                    is Resource.Loading -> {
                        _loadingState.value = LoadingState(loading = true)
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            _repoDetailState.value = RepoDetailState(
                error = Constants.UNEXPECTED_ERROR
            )
        }
    }

    fun performSaveToDatabase(){
        saveRepoLocalUseCase(repoDetailState.value.repo).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _loadingState.value = LoadingState(loading = false)
                    _dbSaveState.value = DbSaveState(messageResId = R.string.succesfuly_saved_repository_to_database)
                }

                is Resource.Error -> {
                    _loadingState.value = LoadingState(loading = false)
                    _dbSaveState.value = DbSaveState(
                        messageResId = R.string.error_at_saving_repository_to_database,
                        isError = true
                    )
                }

                is Resource.Loading -> {
                    _loadingState.value = LoadingState(loading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun performRemoveFromDatabase(){
        removeRepoLocalUseCase(repoDetailState.value.repo).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _loadingState.value = LoadingState(loading = false)
                    _dbSaveState.value = DbSaveState(messageResId = R.string.successfuly_removed_repository_from_database)
                }

                is Resource.Error -> {
                    _loadingState.value = LoadingState(loading = false)
                    _dbSaveState.value = DbSaveState(
                        messageResId = R.string.error_at_removing_repository_from_database,
                        isError = true
                    )
                }

                is Resource.Loading -> {
                    _loadingState.value = LoadingState(loading = true)
                }
            }
        }.launchIn(viewModelScope)
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