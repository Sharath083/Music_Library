package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OutputList<T>(val data:T,val status: String)