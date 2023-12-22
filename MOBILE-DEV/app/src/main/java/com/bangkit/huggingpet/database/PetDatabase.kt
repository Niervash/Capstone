package com.bangkit.huggingpet.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities =[ListPetDetail::class, RemoteKeys::class],
    version = 2,
    exportSchema =false
)
abstract class PetDatabase : RoomDatabase() {

    abstract fun getListPetDetailDao(): ListPetDetailDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: PetDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): PetDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PetDatabase::class.java, "pet_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}