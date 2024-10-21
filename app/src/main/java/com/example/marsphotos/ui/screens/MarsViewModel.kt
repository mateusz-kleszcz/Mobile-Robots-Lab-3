package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.network.MarsApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MarsUiState {
    data class Success(val photos: String, val randomPhoto : MarsPhoto) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

class MarsViewModel : ViewModel() {
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    var listResult: List<MarsPhoto> = emptyList()

    init {
        getMarsPhotos()
    }

    fun getMarsPhotos() {
        viewModelScope.launch {
            marsUiState = MarsUiState.Loading
            marsUiState = try {
                listResult = MarsApi.retrofitService.getPhotos()
                MarsUiState.Success(
                    "Success: ${listResult.size} Mars photos retrieved",
                    listResult.random()
                )
            } catch (e: IOException) {
                MarsUiState.Error
            } catch (e: HttpException) {
                MarsUiState.Error
            }
        }
    }

    fun rollMarsPhoto() {
        if (marsUiState is MarsUiState.Success) {
            marsUiState = MarsUiState.Success(
                "Success: ${listResult.size} Mars photos retrieved",
                listResult.random()
            )
        }
    }
}
