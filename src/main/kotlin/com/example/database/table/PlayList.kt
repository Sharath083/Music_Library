package com.example.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object PlayList:UUIDTable("playListTable") {
    val userId=reference("userId", Users)
    val playListName=varchar("playListName",20)
    val songId=reference("songId", Songs)
}
