package com.example.service

import com.example.data.model.SuccessResponse
import com.example.utils.helperfunctions.HelperMethods
import io.ktor.http.*
class UserServices {

    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))
    }
}
