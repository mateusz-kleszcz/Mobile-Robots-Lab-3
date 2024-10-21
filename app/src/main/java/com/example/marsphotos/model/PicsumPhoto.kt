package com.example.marsphotos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PicsumPhoto(
    override val id: String,
    @SerialName(value = "download_url")
    override val imgSrc: String
) : Photo
