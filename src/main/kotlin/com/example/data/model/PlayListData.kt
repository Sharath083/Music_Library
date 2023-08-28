package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayListData(val userId:Int,val playListName:String,val songId:Int)
