package com.example.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object Users:UUIDTable("userTable") {
    val userName=varchar("name",20)
    val gmail=varchar("gmail",45)
    val password=varchar("password",10)
}