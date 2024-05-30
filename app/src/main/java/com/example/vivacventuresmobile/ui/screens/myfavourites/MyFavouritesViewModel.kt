package com.example.vivacventuresmobile.ui.screens.myfavourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.usecases.DeleteListSharedWithUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteListUseCase
import com.example.vivacventuresmobile.domain.usecases.GetFriendsUseCase
import com.example.vivacventuresmobile.domain.usecases.GetListUseCase
import com.example.vivacventuresmobile.domain.usecases.GetListsSharedWithUseCase
import com.example.vivacventuresmobile.domain.usecases.ShareListUseCase
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
class MyFavouritesViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
    private val stringProvider: StringProvider,
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getListsSharedWithUseCase: GetListsSharedWithUseCase,
    private val shareListUseCase: ShareListUseCase,
    private val deleteListSharedWithUseCase: DeleteListSharedWithUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MyFavouritesState> by lazy {
        MutableStateFlow(MyFavouritesState())
    }

    val uiState: StateFlow<MyFavouritesState> = _uiState

    init {
        _uiState.value = MyFavouritesState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: MyFavouritesEvent) {
        when (event) {
            MyFavouritesEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is MyFavouritesEvent.SaveListId -> {
                _uiState.value = _uiState.value.copy(listId = event.listId)
                getVivacPlaces()
                getListSharedWith()
            }

            is MyFavouritesEvent.SaveUsername -> {
                _uiState.value = _uiState.value.copy(username = event.username)
                getFriends()
            }

            is MyFavouritesEvent.DeleteList -> deleteList()

            is MyFavouritesEvent.ShareList -> shareListWith(event.username)

            is MyFavouritesEvent.UnShareList -> deleteListShareWith(event.username)

            is MyFavouritesEvent.GetVivacPlaces -> getVivacPlaces()
        }
    }

    private fun deleteListShareWith(username: String) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            deleteListSharedWithUseCase(_uiState.value.listId, username)
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
                                    error = result.message,
                                    loading = false
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { friends ->
                                _uiState.update {
                                    it.copy(
                                        error = stringProvider.getString(R.string.list_unshared),
                                        loading = false
                                    )
                                }
                                getListSharedWith()
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

    private fun shareListWith(username: String) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            shareListUseCase(_uiState.value.listId, username)
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
                                    error = result.message,
                                    loading = false
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { friends ->
                                _uiState.update {
                                    it.copy(
                                        error = stringProvider.getString(R.string.list_shared),
                                        loading = false
                                    )
                                }
                                getListSharedWith()
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

    private fun getListSharedWith() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getListsSharedWithUseCase(_uiState.value.listId)
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
                                    error = result.message,
                                    loading = false
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { friends ->
                                _uiState.update {
                                    it.copy(
                                        sharedWith = friends,
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
                                        val acceptedFriends = friends.filter { it.status }
                                        val requesterNames: List<String> = acceptedFriends.map { if (it.requested == _uiState.value.username) it.requester else it.requested }
                                        it.copy(
                                            friends = requesterNames,
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

    private fun deleteList() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            deleteListUseCase(_uiState.value.listId)
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
                                    error = result.message,
                                    loading = false
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { places ->
                                _uiState.update {
                                    it.copy(
                                        error = stringProvider.getString(R.string.list_deleted),
                                        loading = false,
                                        listDeleted = true

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

    private fun getVivacPlaces() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getListUseCase(_uiState.value.listId)
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
                                    error = result.message,
                                    loading = false,
                                    firstTime = false
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { places ->
                                _uiState.update {
                                    it.copy(
                                        list = places,
                                        loading = false,
                                        firstTime = false
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