package com.example.data.schemas

import org.jetbrains.exposed.dao.id.IntIdTable

object SongsTable:IntIdTable("songTable") {
    val title=varchar("title",25)
    val artist=varchar("artist",25)
    val duration=varchar("duration",10)

}