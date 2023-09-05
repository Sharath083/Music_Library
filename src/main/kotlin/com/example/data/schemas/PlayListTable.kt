package com.example.data.schemas

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.dao.id.UUIDTable

object PlayListTable:Table("playListTable") {
    val userId=reference("userId", UserTable)
    val playListName=varchar("playListName",20)
    val songId=reference("songId", SongsTable)
}
