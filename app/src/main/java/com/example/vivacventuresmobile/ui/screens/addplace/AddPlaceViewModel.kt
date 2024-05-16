package com.example.vivacventuresmobile.ui.screens.addplace

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.usecases.AddPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.UpdatePlaceUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class AddPlaceViewModel @Inject constructor(
    private val addPlaceUseCase: AddPlaceUseCase,
    private val updatePlaceUseCase: UpdatePlaceUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddPlaceState> by lazy {
        MutableStateFlow(AddPlaceState())
    }
    val uiState: MutableStateFlow<AddPlaceState> = _uiState

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

            is AddPlaceEvent.OnPicturesChange -> {
//                uploadImages(event.pictures)
//                if (uiState.value.uris.size >= 3) {
//                    _uiState.update { it.copy(error = "Solo se pueden subir 3 imágenes.") }
//                } else {
//                    _uiState.update { it.copy(uris = it.uris + event.pictures) }
//                }
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

            is AddPlaceEvent.AddUsername -> {
                _uiState.update { it.copy(place = it.place.copy(username = event.userName)) }
            }

            is AddPlaceEvent.AddUri -> {
                addUri(event.pictures)
            }

            is AddPlaceEvent.DeleteUri -> {
                deleteUri(event.num)
            }

            is AddPlaceEvent.DetailsCompleted -> {
                if (validatePlaceDetails()) {
                    _uiState.update { it.copy(cambioPantalla = 1) }
                }
            }

            is AddPlaceEvent.Vuelta -> {
                _uiState.update { it.copy(cambioPantalla = 0) }
            }

            is AddPlaceEvent.UpdatePlace -> {
                updatePlace()
            }

            is AddPlaceEvent.ChangeExists -> {
//                if (event.boolean.name != "") {
//                    _uiState.update {
//                        it.copy(exists = true,
//                            place = event.boolean)
//                    }
//                } else {
//                    _uiState.update { it.copy(exists = false) }
//                }
                if (event.boolean == "true") {
                    _uiState.update { it.copy(exists = true) }
                } else {
                    _uiState.update { it.copy(exists = false) }
                }
            }
        }
    }

    fun validatePlaceDetails(): Boolean {
        val place = uiState.value.place

        if (place.name.isEmpty() || place.description.isEmpty() || place.type.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Please fill all fields")
            return false
        }

        return true
    }

    private fun uploadImages(imageUris: List<Uri>) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.GERMAN)
        val imageUrls = mutableListOf<String>()

        for ((index, uri) in imageUris.withIndex()) {
//            if (uiState.value.place.images.size >= 3) {
//                _uiState.update { it.copy(error = "Solo se pueden subir 3 imágenes.") }
//            } else {
            val now = Date()
            val fileName: String = formatter.format(now)
            val storageReference =
                FirebaseStorage.getInstance()
                    .getReference("images/${uiState.value.place.username}" + "_" + "$fileName")

            storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    imageUrls.add(downloadUrl)
                    _uiState.update { it.copy(place = it.place.copy(images = it.place.images + imageUrls)) }

                    // Si es la última imagen, llama a saveVivacPlace.
                    if (index == imageUris.size - 1) {
                        if (uiState.value.exists.equals("true")) updateVivacPlace()
                        else saveVivacPlace()
                    }
                }.addOnFailureListener { e ->
                    _uiState.update {
                        it.copy(error = "Error al subir la imagen")
                    }
                }
            }
//            }
        }
    }


//    private suspend fun uploadImages(imageUris: List<Uri>) {
////        val user = AppPreferences.username
////        storageReference = FirebaseStorage.getInstance().getReference("images/$user.$fileName")
//
//        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.GERMAN)
//        val imageUrls = mutableListOf<String>()
//
////        val uploadTasks = mutableListOf<Task<Uri>>()
//        for (uri in imageUris) {
//            if (uiState.value.place.images.size >= 3) {
//                _uiState.update { it.copy(error = "Solo se pueden subir 3 imágenes.") }
//            } else {
//                val now = Date()
//                val fileName: String = formatter.format(now)
//                val storageReference =
//                    FirebaseStorage.getInstance().getReference("images/${uiState.value.place.username}"+ "_" +"$fileName")
//
//                storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
//                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        val downloadUrl = uri.toString()
//                        imageUrls.add(downloadUrl)
////                    _uiState.update { it.copy(place = it.place.copy(images = imageUrls)) }
//                        _uiState.update { it.copy(place = it.place.copy(images = it.place.images + imageUrls)) }
//
//                    }.addOnFailureListener { e ->
//                        _uiState.update {
//                            it.copy(error = "Error al subir la imagen")
//                        }
//                    }
//                }
//            }
//
////            val uploadTask = storageReference.putFile(uri).continueWithTask { task ->
////                if (!task.isSuccessful) {
////                    task.exception?.let {
////                        throw it
////                    }
////                }
////                storageReference.downloadUrl
////            }
////            uploadTasks.add(uploadTask)
////            Tasks.whenAllSuccess<Uri>(uploadTasks).addOnSuccessListener { urls ->
////                val imageUrls = urls.map { it.toString() }
////                _uiState.update { it.copy(place = it.place.copy(images = it.place.images + imageUrls)) }
////            }
//        }
//    }

/////// -----  ELIMINAR IMAGEN CON URL  -------  /////////

//    val imageUrl = it.place.images.last()
//
//
//    val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
//
//    storageReference.delete().addOnSuccessListener {
//        _uiState.update { it.copy(success = "La imagen se ha eliminado correctamente.") }
//    }.addOnFailureListener { e ->
//        _uiState.update { it.copy(error = "Ha ocurrido un error al eliminar la imagen.") }
//    }

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
        deleteImages(uiState.value.place.images)
        _uiState.update { it.copy(loading = true) }
        uploadImages(_uiState.value.uris)
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
        for (image in images) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image)
            storageReference.delete().addOnSuccessListener {
                _uiState.update { it.copy(error = "La imagen se ha eliminado correctamente.") }
            }.addOnFailureListener { e ->
                _uiState.update { it.copy(error = "Ha ocurrido un error al eliminar la imagen.") }
            }
        }
    }



    private fun deleteUri(num: Int) {
        _uiState.update { currentState ->
            val updatedUris = currentState.uris.toMutableList()
            if (num >= 0 && num < updatedUris.size) {
                updatedUris.removeAt(num)
            }
            currentState.copy(uris = updatedUris)
        }
    }

    private fun addUri(pictures: List<Uri>) {
        if (uiState.value.uris.size < 3) {
            for (uri in pictures) {
                _uiState.update { it.copy(uris = it.uris + uri) }
            }
        } else {
            _uiState.update { it.copy(error = "Solo se pueden subir 3 imágenes.") }
        }

    }


}