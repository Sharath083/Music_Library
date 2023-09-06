package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayListData(val userId:Int,val playListName:String,val songId:Int)
@Serializable
data class PlayListDatas(val userId:String,val playListName:String,val songId:String)