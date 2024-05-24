package com.example.vivacventuresmobile.ui.screens.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.usecases.SearchFriendUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val searchFriendUseCase: SearchFriendUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchUsersState> by lazy {
        MutableStateFlow(SearchUsersState())
    }

    val uiState: MutableStateFlow<SearchUsersState> = _uiState

    init {
        _uiState.value = SearchUsersState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: SearchUsersEvent) {
        when (event) {
            SearchUsersEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is SearchUsersEvent.SaveUsername -> {
                _uiState.value = _uiState.value.copy(username = event.username)
            }

            is SearchUsersEvent.OnSearchChange -> {
                _uiState.value = _uiState.value.copy(search = event.search)
            }

            SearchUsersEvent.DoSearch -> {
                doSearch()
            }

            else -> {}
        }
    }

    private fun doSearch() {
        if (_uiState.value.search.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Search field is empty")

        } else {
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                searchFriendUseCase(_uiState.value.search)
                    .catch(action = { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                loading = false
                            )
                        }
                    })
                    .collect { result ->
                        when (result) {
                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = "Usuario no encontrado",
                                        loading = false
                                    )
                                }
                            }

                            is NetworkResult.Success -> {
                                result.data?.let { friend ->
                                    _uiState.update {
                                        it.copy(
                                            friend = friend,
                                            loading = false
                                        )
                                    }
                                }
                            }

                            is NetworkResult.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        loading = true
                                    )
                                }
                            }
                        }
                    }
            }
        }

    }
}