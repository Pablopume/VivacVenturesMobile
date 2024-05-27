package com.example.vivacventuresmobile.ui.screens.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
import com.example.vivacventuresmobile.domain.usecases.SearchFriendUseCase
import com.example.vivacventuresmobile.domain.usecases.SendFriendRequestUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val searchFriendUseCase: SearchFriendUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchUsersState> by lazy {
        MutableStateFlow(SearchUsersState())
    }

    val uiState: StateFlow<SearchUsersState> = _uiState

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

            SearchUsersEvent.SendFriendRequest -> {
                sendFriendRequest()
            }
        }
    }

    private fun sendFriendRequest() {
        if (_uiState.value.friend.username.isEmpty()) {
            _uiState.value =
                _uiState.value.copy(error = stringProvider.getString(R.string.search_user_first))
        } else {
            viewModelScope.launch {
                sendFriendRequestUseCase(
                    FriendRequest(
                        0,
                        uiState.value.username,
                        uiState.value.friend.username,
                        false
                    )
                )
                    .catch { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message
                            )
                        }
                    }
                    .collect { result ->
                        when (result) {
                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = result.message,
                                        loading = false
                                    )
                                }
                            }

                            is NetworkResult.Success -> {
                                _uiState.update {
                                    it.copy(
                                        error = stringProvider.getString(R.string.friend_request_sent),
                                        loading = false
                                    )
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

    private fun doSearch() {
        if (_uiState.value.search.isEmpty()) {
            _uiState.value =
                _uiState.value.copy(error = stringProvider.getString(R.string.search_field_empty))

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
                                        error = stringProvider.getString(R.string.user_not_found),
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