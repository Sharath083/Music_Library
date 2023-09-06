package com.example.utils.helperfunctions


import com.example.entity.PlayListEntity
import com.example.entity.SongsEntity
import com.example.entity.UserEntity
import com.example.model.InputSong
import com.example.model.PlayListDatas
import com.example.model.SongDatas
import com.example.model.UserCheck
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class Mapping {


    fun mapSong(row:SongsEntity): InputSong {
        return InputSong(row.artist,row.title,row.duration)
    }

    fun mapSongData(row:SongsEntity): SongDatas {
        return SongDatas(row.id.value.toString(),row.title,row.artist,row.duration)
    }
    fun mapRegisteredUser(row: UserEntity): UserCheck {
        return  UserCheck(row.userName,row.gmail,row.password)
    }
    fun mapPlayListDetails(row:PlayListEntity): PlayListDatas {
        return PlayListDatas(row.userId.id.value.toString(),row.playListName,row.songId.id.value.toString())
    }
    fun mapUserId(row: EntityID<UUID>):UUID{
        return row.value
    }

}