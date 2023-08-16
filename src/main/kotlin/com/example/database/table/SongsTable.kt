package com.example.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object SongsTable:IntIdTable("songTable") {
    val title=varchar("title",25)
    val artist=varchar("artist",25)
    val duration=varchar("duration",10)

}