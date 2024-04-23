package com.example.vivacventuresmobile.ui.screens.addplace

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.usecases.AddPlaceUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
//    private val AppPreferences: AppPreferences
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddPlaceState> by lazy {
        MutableStateFlow(AddPlaceState())
    }
    val uiState: MutableStateFlow<AddPlaceState> = _uiState

    init {
        _uiState.value = AddPlaceState(
            error = null,
            loading = false
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
                val imageUrls = uploadImages(event.pictures)

                _uiState.update { it.copy(place = it.place.copy(images = imageUrls)) }
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
        }
    }
    private fun uploadImages(imageUris: List<Uri>): List<String> {
//        val user = AppPreferences.username

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.GERMAN)
        val imageUrls = mutableListOf<String>()
//            storageReference = FirebaseStorage.getInstance().getReference("images/$user.$fileName")

//        val uploadTasks = mutableListOf<Task<Uri>>()
        for (uri in imageUris) {
            val now = Date()
            val fileName: String = formatter.format(now)
            val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

            storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    imageUrls.add(downloadUrl)
//                    _uiState.update { it.copy(place = it.place.copy(images = imageUrls)) }
                    _uiState.update { it.copy(place = it.place.copy(images = it.place.images + imageUrls)) }

                } .addOnFailureListener { e ->
                    _uiState.update { it.copy(error = "Imagen Error") }
                    // Aquí puedes manejar el error
                }
            }
//            val uploadTask = storageReference.putFile(uri).continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    task.exception?.let {
//                        throw it
//                    }
//                }
//                storageReference.downloadUrl
//            }
//            uploadTasks.add(uploadTask)
        }

//        Tasks.whenAllSuccess<Uri>(uploadTasks).addOnSuccessListener { urls ->
//            val imageUrls = urls.map { it.toString() }
//            _uiState.update { it.copy(place = it.place.copy(images = imageUrls)) }
//        }


        return imageUrls
    }

//    storageReference!!.putFile(uri).addOnSuccessListener {
//        storageReference!!.getDownloadUrl().addOnSuccessListener { uri ->
//            imageUrls.add(uri.toString())
//        }
//    }

    private fun addPlace() {
        if (_uiState.value.place.name.isEmpty() || _uiState.value.place.description.isEmpty() || _uiState.value.place.type.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Please fill all fields")
            return
        } else {
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
    }
}