package com.example.entity

import com.example.data.schemas.PlayListTable
import com.example.database.table.PlayList
import com.example.database.table.Songs
import com.example.database.table.Users
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PlayListEntity(id: EntityID<UUID>) : UUIDEntity(id){
    companion object : UUIDEntityClass<PlayListEntity>(PlayList)
    var userId by UserEntity referencedOn Users.id
    var playListName by PlayList.playListName
    var songId by SongsEntity referencedOn Songs.id
}