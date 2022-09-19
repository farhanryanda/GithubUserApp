package com.farhanryanda.submission3githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite_user")
    fun getFavoritedUser(): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToFavorite(favoriteUser: FavoriteEntity)

    @Query("SELECT * FROM favorite_user WHERE id = :id")
    fun checkUser(id: Int) : Int

    @Query("DELETE FROM favorite_user WHERE id = :id")
    fun removeFromFavorite(id: Int) : Int
}