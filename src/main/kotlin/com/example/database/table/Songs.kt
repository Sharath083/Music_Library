package com.example.database.table


import org.jetbrains.exposed.dao.id.UUIDTable

object Songs: UUIDTable("songTable") {
    val title=varchar("title",25)
    val artist=varchar("artist",25)
    val duration=varchar("duration",10)

}