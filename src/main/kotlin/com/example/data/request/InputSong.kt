package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class InputSong(val tittle:String?, val artist:String?, val duration:String?)
