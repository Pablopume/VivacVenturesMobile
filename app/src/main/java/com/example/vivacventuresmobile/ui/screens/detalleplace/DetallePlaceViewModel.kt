package com.example.vivacventuresmobile.ui.screens.detalleplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.example.vivacventuresmobile.domain.usecases.AddFavouriteUseCase
import com.example.vivacventuresmobile.domain.usecases.AddReportUseCase
import com.example.vivacventuresmobile.domain.usecases.AddValorationUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteFavouriteUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteValorationUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteVivacPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.GetListByUserAndVivacPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.GetListsUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetallePlaceViewModel @Inject constructor(
    private val getVivacPlaceUseCase: GetVivacPlaceUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val deleteFavouriteUseCase: DeleteFavouriteUseCase,
    private val deletePlaceUseCase: DeleteVivacPlaceUseCase,
    private val addValorationUseCase: AddValorationUseCase,
    private val deleteValorationUseCase: DeleteValorationUseCase,
    private val addReportUseCase: AddReportUseCase,
    private val getListByUserAndVivacPlaceUseCase: GetListByUserAndVivacPlaceUseCase,
    private val getListsUseCase: GetListsUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {
    private val _uiState: MutableStateFlow<DetallePlaceState> by lazy {
        MutableStateFlow(DetallePlaceState())
    }

    val uiState: StateFlow<DetallePlaceState> = _uiState

    init {
        _uiState.value = DetallePlaceState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: DetallePlaceEvent) {
        when (event) {
            DetallePlaceEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is DetallePlaceEvent.GetDetalle -> getVivacPlace(event.id)
            is DetallePlaceEvent.AddFavourite -> addFavourite(event.id)
            is DetallePlaceEvent.DeleteFavourite -> deleteFavourite(event.id)
            is DetallePlaceEvent.SaveUsernameAndId -> {
                _uiState.value =
                    _uiState.value.copy(username = event.username)
                getVivacPlace(event.vivacId)
            }

            is DetallePlaceEvent.DeletePlace -> {
                deleteImages()
            }
            is DetallePlaceEvent.AddValoration -> {
                addValoration()
            }

            is DetallePlaceEvent.DeleteValoration -> {
                deleteValoration(event.id)
            }

            is DetallePlaceEvent.OnDescriptionReportChange -> {
                _uiState.value = _uiState.value.copy(descriptionReport = event.description)
            }

            is DetallePlaceEvent.OnReviewValorationChange -> {
                _uiState.value = _uiState.value.copy(reviewValoration = event.review)
            }

            is DetallePlaceEvent.OnScoreChange -> {
                _uiState.value = _uiState.value.copy(score = event.score)
            }

            is DetallePlaceEvent.AddReport -> addReport()
        }
    }

    private fun deleteImages() {
        val images = uiState.value.vivacPlace?.images ?: emptyList()

        if (images.isEmpty()) {
            deletePlace()
            return
        }

        val totalImages = images.size
        var imagesDeleted = 0

        for (image in images) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image)
            storageReference.delete().addOnSuccessListener {
                imagesDeleted++

                _uiState.update { it.copy(error = stringProvider.getString(R.string.image_deleted_correctly)) }

                if (imagesDeleted == totalImages) {
                    deletePlace()
                }
            }.addOnFailureListener {
                _uiState.update { it.copy(error = stringProvider.getString(R.string.error_deleting_image)) }
            }
        }
    }

    private fun addReport() {
        if (_uiState.value.descriptionReport.isNotEmpty() && _uiState.value.vivacPlace?.id != 0) {
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                addReportUseCase(
                    com.example.vivacventuresmobile.domain.modelo.Report(
                        0,
                        _uiState.value.username,
                        _uiState.value.vivacPlace?.id ?: 0,
                        _uiState.value.descriptionReport,
                    )
                )
                    .catch { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                loading = false
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
                                        error = stringProvider.getString(R.string.report_added),
                                        loading = false,
                                        descriptionReport = ""
                                    )
                                }
                                getVivacPlace(_uiState.value.vivacPlace?.id ?: 0)
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

    private fun addValoration() {
        if (_uiState.value.score != 0 && _uiState.value.reviewValoration.isNotEmpty() && _uiState.value.vivacPlace?.id != 0) {
            val valoration = Valoration(
                0,
                _uiState.value.username,
                _uiState.value.vivacPlace?.id ?: 0,
                _uiState.value.score,
                _uiState.value.reviewValoration,
                LocalDate.now()
            )
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                addValorationUseCase(valoration)
                    .catch { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                loading = false
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
                                        error = stringProvider.getString(R.string.valoration_added),
                                        loading = false,
                                        score = 0,
                                        reviewValoration = ""
                                    )
                                }
                                getVivacPlace(_uiState.value.vivacPlace?.id ?: 0)
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

    private fun deleteValoration(id: Int) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            deleteValorationUseCase(id)
                .catch { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
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
                                    error = stringProvider.getString(R.string.valoration_deleted),
                                    loading = false,
                                )
                            }
                            getVivacPlace(_uiState.value.vivacPlace?.id ?: 0)
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

    private fun getVivacPlace(id: Int) {
        if (_uiState.value.username.isNotEmpty() && _uiState.value.vivacPlace?.id != 0) {
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                getVivacPlaceUseCase(id, _uiState.value.username)
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
                                            vivacPlace = places,
                                            loading = false
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
        }
    }

    private fun getLists() {
        viewModelScope.launch {
            getListsUseCase(_uiState.value.username)
                .catch { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
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
                                    listsUser = result.data ?: emptyList(),
                                    loading = false
                                )
                            }
                            getListsByUserAndVivacPlace()
                        }

                        is NetworkResult.Loading -> {
                            //do nothing
                        }
                    }
                }
        }
    }

    private fun getListsByUserAndVivacPlace() {
        viewModelScope.launch {
            getListByUserAndVivacPlaceUseCase(_uiState.value.vivacPlace?.id ?: 0, _uiState.value.username)
                .catch { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
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
                                    listsVivacPlace = result.data ?: emptyList(),
                                    loading = false
                                )
                            }
                        }

                        is NetworkResult.Loading -> {
                            //do nothing
                        }
                    }
                }
        }
    }

    private fun addFavourite(listId: Int) {
        viewModelScope.launch {
            addFavouriteUseCase(listId, _uiState.value.vivacPlace?.id ?: 0)
                .catch { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
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
                                    error = stringProvider.getString(R.string.favourite_added_to_list),
                                    loading = false,
                                )
                            }
                            getVivacPlace(_uiState.value.vivacPlace?.id ?: 0)
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

    private fun deleteFavourite(listId: Int) {
        viewModelScope.launch {
            deleteFavouriteUseCase(
                listId,
                _uiState.value.vivacPlace?.id ?: 0
            )
                .catch { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
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
                                    error = stringProvider.getString(R.string.favourite_removed_from_list),
                                    loading = false,
                                )
                            }
                            getVivacPlace(_uiState.value.vivacPlace?.id ?: 0)
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

    private fun deletePlace() {
        if (_uiState.value.vivacPlace?.id != 0) {
            viewModelScope.launch {
                deletePlaceUseCase(_uiState.value.vivacPlace?.id ?: 0)
                    .catch { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                loading = false
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
                                        loading = false,
                                        error = stringProvider.getString(R.string.place_deleted),
                                        deleted = true
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
}