package com.b21.finalproject.smartlibraryapp.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.RatingEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.UserEntity

@Database(entities = [BookEntity::class, RatingEntity::class, UserEntity::class],
            version = 1,
            exportSchema = false)
abstract class LibraryDatabase : RoomDatabase(){
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getInstance(context: Context): LibraryDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "Libraries.db"
                ).build().apply {
                    INSTANCE = this
                }
            }
    }
}