package com.example.entity

import com.example.data.schemas.SongsTable
import com.example.database.table.Songs
import org.jetbrains.exposed.dao.id.EntityID

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.*

class SongsEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SongsEntity>(Songs)
    var title by Songs.title
    var artist by Songs.artist
    var duration by Songs.duration
}
