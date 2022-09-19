package com.farhanryanda.submission3githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 2)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: UsersDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): UsersDatabase {
            if (INSTANCE == null) {
                synchronized(UsersDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UsersDatabase::class.java, "favorite_db")
                        .build()
                }
            }
            return INSTANCE as UsersDatabase
        }
    }
}