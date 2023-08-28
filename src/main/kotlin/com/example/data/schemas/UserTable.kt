package com.example.data.schemas

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable:IntIdTable("userTable") {
    val userName=varchar("name",20)
    val gmail=varchar("gmail",45)
    val password=varchar("password",10)
}