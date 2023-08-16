package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class SongData(val songId:Int,val tittle:String?, val artist:String, val duration:String)
