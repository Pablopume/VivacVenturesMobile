package com.example.vivacventuresmobile.ui.screens.mylists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.ListFavs
import com.example.vivacventuresmobile.domain.usecases.GetListsUseCase
import com.example.vivacventuresmobile.domain.usecases.SaveListUseCase
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
class MyListsViewModel @Inject constructor(
    private val getListsUseCase: GetListsUseCase,
    private val createListUseCase: SaveListUseCase,
    private val stringProvider: StringProvider

) : ViewModel() {
    private val _uiState: MutableStateFlow<MyListsState> by lazy {
        MutableStateFlow(MyListsState())
    }

    val uiState: StateFlow<MyListsState> = _uiState

    init {
        _uiState.value = MyListsState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: MyListsEvent) {
        when (event) {
            MyListsEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is MyListsEvent.SaveUsername -> {
                _uiState.value = _uiState.value.copy(username = event.username)
                getLists()
            }
            is MyListsEvent.CreateList -> createList()
            is MyListsEvent.OnNameChanged -> {
                _uiState.value = _uiState.value.copy(nameList = event.name)
            }

            is MyListsEvent.GetLists -> getLists()
        }
    }

    private fun createList(){
        if (_uiState.value.nameList.isNotEmpty()){
            _uiState.update {
                it.copy(
                    loading = true
                )
            }
            viewModelScope.launch {
                createListUseCase(ListFavs(name = _uiState.value.nameList, username = _uiState.value.username))
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
                                            error = stringProvider.getString(R.string.list_created),
                                            loading = false,
                                            nameList = ""
                                        )
                                    }
                                    getLists()
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

        } else {
            _uiState.update {
                it.copy(
                    error = stringProvider.getString(R.string.error_empty_name)
                )
            }
        }
    }

    private fun getLists() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getListsUseCase(_uiState.value.username)
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