package com.example.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SongData(val songId:Int,val tittle:String?, val artist:String, val duration:String)
@Serializable
data class SongDatas(val songId:String,val tittle:String?, val artist:String, val duration:String)