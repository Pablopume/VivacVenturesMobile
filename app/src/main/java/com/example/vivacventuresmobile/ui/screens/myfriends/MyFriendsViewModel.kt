package com.example.vivacventuresmobile.ui.screens.myfriends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
import com.example.vivacventuresmobile.domain.usecases.AcceptFriendRequestUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteFriendRequestUseCase
import com.example.vivacventuresmobile.domain.usecases.GetFriendsUseCase
import com.example.vivacventuresmobile.domain.usecases.RejectFriendRequestUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFriendsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val deleteFriendRequestUseCase: DeleteFriendRequestUseCase,
    private val rejectFriendRequestUseCase: RejectFriendRequestUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MyFriendsState> by lazy {
        MutableStateFlow(MyFriendsState())
    }

    val uiState: MutableStateFlow<MyFriendsState> = _uiState

    init {
        _uiState.value = MyFriendsState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: MyFriendsEvent) {
        when (event) {
            MyFriendsEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is MyFriendsEvent.SaveUsername -> {
                _uiState.value = _uiState.value.copy(username = event.username)
                getFriends()
            }
            MyFriendsEvent.GetFriends -> getFriends()
            is MyFriendsEvent.AcceptFriendRequest -> acceptFriendRequest(event.friendRequest)
            is MyFriendsEvent.RejectFriendRequest -> rejectFriendRequest(event.friendRequest)
            is MyFriendsEvent.DeleteFriendRequest -> deleteFriendRequest(event.friendRequest)

        }
    }

    private fun deleteFriendRequest(friendRequest: FriendRequest) {
        viewModelScope.launch {
            deleteFriendRequestUseCase(friendRequest)
                .catch(action = { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message
                        )
                    }
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    error = null
                                )
                            }
                            getFriends()
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

    private fun rejectFriendRequest(friendRequest: FriendRequest) {
        viewModelScope.launch {
            rejectFriendRequestUseCase(friendRequest)
                .catch(action = { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message
                        )
                    }
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    error = null
                                )
                            }
                            getFriends()
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

    private fun acceptFriendRequest(friendRequest: FriendRequest) {
        viewModelScope.launch {
            acceptFriendRequestUseCase(friendRequest)
                .catch(action = { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message
                        )
                    }
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    error = null
                                )
                            }
                            getFriends()
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

    private fun getFriends() {
        if (_uiState.value.username.isNotEmpty()) {
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                getFriendsUseCase(_uiState.value.username)
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
                                        loading = false
                                    )
                                }
                            }

                            is NetworkResult.Success -> {
                                result.data?.let { friends ->
                                    _uiState.update {
                                        val (acceptedFriends, pendingFriends) = friends.partition { it.status }
                                        it.copy(
                                            friends = acceptedFriends,
                                            pendingFriends = pendingFriends,
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