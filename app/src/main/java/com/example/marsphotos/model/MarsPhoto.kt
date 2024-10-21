package com.example.marsphotos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarsPhoto(
    override val id: String,
    @SerialName(value = "img_src")
    override val imgSrc: String
) : Photo
