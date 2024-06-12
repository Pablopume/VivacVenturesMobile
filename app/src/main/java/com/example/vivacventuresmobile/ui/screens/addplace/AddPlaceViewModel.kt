package com.example.vivacventuresmobile.ui.screens.addplace

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.usecases.AddPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.UpdatePlaceUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.Logger
import javax.inject.Inject


@HiltViewModel
class AddPlaceViewModel @Inject constructor(
    private val addPlaceUseCase: AddPlaceUseCase,
    private val updatePlaceUseCase: UpdatePlaceUseCase,
    private val getVivacPlaceUseCase: GetVivacPlaceUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddPlaceState> by lazy {
        MutableStateFlow(AddPlaceState())
    }
    val uiState: StateFlow<AddPlaceState> = _uiState

    init {
        _uiState.value = AddPlaceState(
            error = null,
            loading = false,
        )
    }

    fun handleEvent(event: AddPlaceEvent) {
        when (event) {
            AddPlaceEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is AddPlaceEvent.AddPlace -> addPlace()
            is AddPlaceEvent.OnNameChange -> {
                _uiState.update { it.copy(place = it.place.copy(name = event.placeName)) }
            }

            is AddPlaceEvent.OnDescriptionChange -> {
                _uiState.update { it.copy(place = it.place.copy(description = event.placeDescription)) }
            }

            is AddPlaceEvent.OnTypeChange -> {
                _uiState.update { it.copy(place = it.place.copy(type = event.type)) }
            }

            is AddPlaceEvent.OnCapacityChange -> {
                _uiState.update { it.copy(place = it.place.copy(capacity = event.capacity)) }
            }

            is AddPlaceEvent.OnDateChange -> {
                _uiState.update { it.copy(place = it.place.copy(date = event.date)) }
            }

            is AddPlaceEvent.OnPriceChange -> {
                _uiState.update { it.copy(place = it.place.copy(price = event.price.toDouble())) }
            }

            is AddPlaceEvent.OnLocationChange -> {
                _uiState.update {
                    it.copy(
                        place = it.place.copy(
                            lat = event.location.latitude,
                            lon = event.location.longitude
                        )
                    )
                }
            }

            is AddPlaceEvent.AddUsername -> {
                val usernameEE = event.userName
                _uiState.update { it.copy(place = it.place.copy(username = usernameEE)) }
                if (uiState.value.place.username != "") {
                    rellenarPlace(event.int)
                }

            }

            is AddPlaceEvent.AddUri -> {
                addUri(event.pictures)
            }

            is AddPlaceEvent.DeleteUri -> {
                deleteUri(event.stringimage, event.imagen)
            }

            is AddPlaceEvent.DetailsCompleted -> {
                if (validatePlaceDetails()) {
                    _uiState.update { it.copy(cambioPantalla = 1) }
                }
            }

            is AddPlaceEvent.VueltaLocation -> {
                _uiState.update { it.copy(cambioPantalla = 0) }
            }

            is AddPlaceEvent.LocationCompleted -> {
                _uiState.update { it.copy(cambioPantalla = 2) }
            }

            is AddPlaceEvent.Vuelta -> {
                _uiState.update { it.copy(cambioPantalla = 1) }
            }

            is AddPlaceEvent.UpdatePlace -> {
                updatePlace()
            }

            is AddPlaceEvent.ChangeExists -> {
                rellenarPlace(event.int)
            }
        }
    }

    private fun rellenarPlace(int: Int) {
        if (int != 0) {
            viewModelScope.launch {
                getVivacPlaceUseCase(int, uiState.value.place.username)
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
                            is NetworkResult.Success -> {
                                val place = result.data
                                if (place != null) {
                                    _uiState.update {
                                        it.copy(
                                            error = null,
                                            loading = false,
                                            place = place,
                                            exists = true
                                        )
                                    }
                                }
                            }

                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = result.message,
                                        loading = false,
                                        exists = true
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
        } else {
            _uiState.update { it.copy(exists = false) }
        }

    }

    fun validatePlaceDetails(): Boolean {
        val place = uiState.value.place

        if (place.name.isEmpty() || place.description.isEmpty() || place.type.isEmpty()) {
            _uiState.value =
                _uiState.value.copy(error = stringProvider.getString(R.string.fill_all_fields))
            return false
        }

        return true
    }

    private fun uploadImages(imageUris: List<Uri>) {
        if (uiState.value.exists && imageUris.isEmpty()) {
            updateVivacPlace()
            return
        }
        if (!uiState.value.exists && imageUris.isEmpty()) {
            saveVivacPlace()
            return
        }

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.GERMAN)
        val imageUrls = mutableListOf<String>()
        val uploadTasks = mutableListOf<Task<Uri>>()

        for (uri in imageUris) {
            val now = Date()
            val fileName: String = formatter.format(now)
            val storageReference =
                FirebaseStorage.getInstance()
                    .getReference("images/${uiState.value.place.username}_${fileName}_${System.currentTimeMillis()}")

            val uploadTask = storageReference.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    storageReference.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    imageUrls.add(downloadUrl)
                    _uiState.update { it.copy(place = it.place.copy(images = it.place.images + downloadUrl)) }
                }
                .addOnFailureListener {
                    _uiState.update {
                        it.copy(error = stringProvider.getString(R.string.error_uploading_image))
                    }
                }

            uploadTasks.add(uploadTask)
        }

        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener {
            Logger.getLogger("Victor").info("Guardar " + uiState.value.place.images)
            if (uiState.value.exists) updateVivacPlace()
            else saveVivacPlace()
        }
    }

    private fun saveVivacPlace() {
        viewModelScope.launch {
            addPlaceUseCase(_uiState.value.place)
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
                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    error = null,
                                    loading = false,
                                    addPlaceDone = true
                                )
                            }
                        }

                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message,
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

    private fun addPlace() {
        _uiState.update { it.copy(loading = true) }
        uploadImages(_uiState.value.uris)
    }

    private fun updatePlace() {
        _uiState.update { it.copy(loading = true) }
        if (_uiState.value.imagesToDelete.isNotEmpty()) {
            deleteImages(_uiState.value.imagesToDelete)
        } else {
            uploadImages(_uiState.value.uris)
        }
    }

    private fun updateVivacPlace() {
        viewModelScope.launch {
            updatePlaceUseCase(_uiState.value.place)
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
                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    error = null,
                                    loading = false,
                                    updatePlaceDone = true
                                )
                            }
                        }

                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message,
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

    private fun deleteImages(images: List<String>) {
        val totalImages = images.size
        var imagesDeleted = 0

        for (image in images) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image)
            storageReference.delete().addOnSuccessListener {
                imagesDeleted++

                if (imagesDeleted == totalImages) {
                    uploadImages(_uiState.value.uris)
                }
            }.addOnFailureListener {
                _uiState.update { it.copy(error = stringProvider.getString(R.string.error_deleting_image)) }
            }
        }
    }

    private fun deleteUri(uriOrImageUrl: String, isImageStored: Boolean) {
        if (uriOrImageUrl != "https://firebasestorage.googleapis.com/v0/b/vivacventures-b3fae.appspot.com/o/images%2FAddImage2.png?alt=media&token=445adaff-d0d1-4ddd-8d41-aa293b632f5f"){
            if (isImageStored) {
                _uiState.update {
                    val updatedImages = it.place.images.toMutableList().apply { remove(uriOrImageUrl) }
                    it.copy(
                        place = it.place.copy(images = updatedImages),
                        imagesToDelete = it.imagesToDelete + uriOrImageUrl
                    )
                }
            } else {
                _uiState.update { currentState ->
                    val updatedUris = currentState.uris.toMutableList()
                    val num = updatedUris.indexOfFirst { it.toString() == uriOrImageUrl }
                    updatedUris.removeAt(num)
                    currentState.copy(uris = updatedUris)
                }
            }
        }
    }

    private fun addUri(pictures: List<Uri>) {

        Logger.getLogger("Victor").info("Victor Add uri" + uiState.value.uris)

        if (uiState.value.uris.size + uiState.value.place.images.size < 3) {
            for (uri in pictures) {
                _uiState.update { it.copy(uris = it.uris + uri) }
            }
        } else {
            _uiState.update { it.copy(error = stringProvider.getString(R.string.only_three_images)) }
        }

    }


}