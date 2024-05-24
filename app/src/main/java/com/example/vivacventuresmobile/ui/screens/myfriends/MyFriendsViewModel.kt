package com.example.vivacventuresmobile.ui.screens.myfriends

import androidx.lifecycle.ViewModel
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesByUsernameUseCase
import com.example.vivacventuresmobile.ui.screens.myplaces.MyFriendsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MyFriendsViewModel @Inject constructor(
    private val getVivacPlacesUseCase: GetVivacPlacesByUsernameUseCase,
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
            }

            is MyFriendsEvent.GetFriends -> {}
            else -> {}
        }
    }
}