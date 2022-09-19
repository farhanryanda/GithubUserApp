package com.farhanryanda.submission3githubuser.api

import com.farhanryanda.submission3githubuser.data.model.ResponseUser
import com.farhanryanda.submission3githubuser.data.model.ResponseUserDetail
import com.farhanryanda.submission3githubuser.data.model.UsersItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<ResponseUser>

    @GET("users/{login}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("login") login: String
    ): Call<ResponseUserDetail>

    @GET("users/{login}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("login") login: String
    ): Call<ArrayList<UsersItem>>

    @GET("users/{login}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("login") login: String
    ): Call<ArrayList<UsersItem>>
}