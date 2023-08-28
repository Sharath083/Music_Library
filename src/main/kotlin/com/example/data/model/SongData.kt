package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SongData(val songId:Int,val tittle:String?, val artist:String, val duration:String)
