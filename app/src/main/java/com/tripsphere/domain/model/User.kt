package com.tripsphere.domain.model

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val password: String = ""
)
