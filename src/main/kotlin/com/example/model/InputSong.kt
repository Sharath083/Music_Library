package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class InputSong(val song:String?, val artist:String?, val duration:String?)
