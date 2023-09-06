package com.example.utils.helperfunctions

import org.koin.core.component.KoinComponent

class HelperMethods: KoinComponent {
    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))
    }
}