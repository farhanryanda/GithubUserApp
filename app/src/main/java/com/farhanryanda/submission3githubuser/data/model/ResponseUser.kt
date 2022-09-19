package com.farhanryanda.submission3githubuser.data.model

import com.google.gson.annotations.SerializedName

data class ResponseUser(

    @field:SerializedName("items")
    val items: ArrayList<UsersItem>
)

data class UsersItem(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("login")
    val login: String
)
