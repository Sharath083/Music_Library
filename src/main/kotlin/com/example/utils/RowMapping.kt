package com.example.utils

import com.example.data.request.InputSong
import com.example.data.request.PlayListData
import com.example.data.response.SongData
import com.example.data.response.UserCheck
import com.example.database.table.PlayListTable
import com.example.database.table.SongsTable
import com.example.database.table.UserTable
import org.jetbrains.exposed.sql.ResultRow

class RowMapping {
    fun mapSong(row:ResultRow): InputSong {
        return InputSong(row[SongsTable.title],row[SongsTable.artist],row[SongsTable.duration])
    }

    fun mapSongData(row:ResultRow): SongData {
        return SongData(row[SongsTable.id].value,row[SongsTable.title],row[SongsTable.artist],row[SongsTable.duration])
    }
    fun resultRowRegisteredUser(row: ResultRow):UserCheck{
        return  UserCheck(row[UserTable.userName],row[UserTable.gmail],row[UserTable.password])

    }
    fun mapPlayListDetails(row:ResultRow): PlayListData {
        return PlayListData(row[PlayListTable.userId].value,row[PlayListTable.playListName],row[PlayListTable.songId].value)
    }
    fun mapUserId(row: ResultRow):Int{
        return row[UserTable.id].value
    }

}