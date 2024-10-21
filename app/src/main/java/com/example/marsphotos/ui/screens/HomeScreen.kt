package com.example.marsphotos.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marsphotos.R
import com.example.marsphotos.model.Photo
import com.example.marsphotos.ui.theme.MarsPhotosTheme

@Composable
fun HomeScreen(
    marsUiState: MarsUiState,
    picsumUiState: PicsumUiState,
    rollNewPhotos: () -> Unit,
    grayPicsumPhoto: () -> Unit,
    blurPicsumPhoto: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        if (picsumUiState is PicsumUiState.Loading || marsUiState is MarsUiState.Loading) {
            LoadingScreen(modifier = modifier.fillMaxSize())
        } else if (picsumUiState is PicsumUiState.Error || marsUiState is MarsUiState.Error) {
            ErrorScreen( modifier = modifier.fillMaxSize())
        } else if (picsumUiState is PicsumUiState.Success && marsUiState is MarsUiState.Success) {
            Spacer(modifier = modifier.height(100.dp))
            ResultScreen(picsumUiState.photos, picsumUiState.randomPhoto)
            ResultScreen(marsUiState.photos, marsUiState.randomPhoto)
            Row(modifier = modifier.fillMaxSize()) {
                Button(onClick = { rollNewPhotos() }) {
                    Text("Roll")
                }
                Button(onClick = { grayPicsumPhoto() }) {
                    Text("Gray")
                }
                Button(onClick = { blurPicsumPhoto() }) {
                    Text("Blur")
                }
            }
        }
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ResultScreen(photos: String, randomPhoto: Photo, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = photos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(randomPhoto.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "A photo",
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    MarsPhotosTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    MarsPhotosTheme {
        ErrorScreen()
    }
}
