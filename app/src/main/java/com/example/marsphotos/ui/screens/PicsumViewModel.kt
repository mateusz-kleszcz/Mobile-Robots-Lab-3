package com.example.marsphotos.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.PicsumPhoto
import com.example.marsphotos.network.PicsumApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface PicsumUiState {
    data class Success(val photos: String, var randomPhoto : PicsumPhoto) : PicsumUiState
    object Error : PicsumUiState
    object Loading : PicsumUiState
}

class PicsumViewModel : ViewModel() {
    var picsumUiState: PicsumUiState by mutableStateOf(PicsumUiState.Loading)
        private set

    private var listResult: List<PicsumPhoto> = emptyList()

    private var currPhoto: PicsumPhoto? = null

    private var isGray = false
    private var isBlur = false

    init {
        getPicsumPhotos()
    }

    private fun getPicsumPhotos() {
        viewModelScope.launch {
            picsumUiState = PicsumUiState.Loading
            try {
                listResult = PicsumApi.retrofitService.getPhotos()
                rollPicsumPhoto()
            } catch (e: IOException) {
                PicsumUiState.Error
            } catch (e: HttpException) {
                PicsumUiState.Error
            }
        }
    }

    fun rollPicsumPhoto() {
        currPhoto = listResult.random()
        updatePicsumState()
    }

    fun grayPicsumPhoto() {
        isGray = !isGray
        updatePicsumState()
    }

    fun blurPicsumPhoto() {
        isBlur = !isBlur
        updatePicsumState()
    }

    private fun updatePicsumState() {
        if (currPhoto != null) {
            picsumUiState = PicsumUiState.Success(
                "Success: ${listResult.size} Picsum photos retrieved",
                    getPhotoWithEffects(currPhoto!!)
            )
        }
    }

    private fun getPhotoWithEffects(photo: PicsumPhoto): PicsumPhoto {
        if (isGray && isBlur) {
            return photo.copy(imgSrc = photo.imgSrc + "?grayscale&blur")
        }
        else if (isGray) {
            return photo.copy(imgSrc = photo.imgSrc + "?grayscale")
        }
        else if (isBlur) {
            return photo.copy(imgSrc = photo.imgSrc + "?blur")
        }
        return photo
    }
}
