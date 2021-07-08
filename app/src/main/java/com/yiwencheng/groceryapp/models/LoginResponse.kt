package com.yiwencheng.groceryapp

import java.io.Serializable

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val mobile: String,
    val password: String
):Serializable{
    companion object{
        const val KEY_USER = "user"
    }
}
