package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class InputSong(val tittle:String?, val artist:String?, val duration:String?)
