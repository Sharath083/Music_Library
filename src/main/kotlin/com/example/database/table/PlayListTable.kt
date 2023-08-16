package com.example.database.table

import org.jetbrains.exposed.sql.Table

object PlayListTable:Table("playListTable") {
    val userId=reference("userId", UserTable)
    val playListName=varchar("playListName",20)
    val songId=reference("id", SongsTable)
}