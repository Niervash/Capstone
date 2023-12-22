package com.bangkit.huggingpet.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ListPetDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pets: List<ListPetDetail>)

    @Query("SELECT * FROM pets")
    fun getAllPets(): PagingSource<Int, ListPetDetail>

    @Query("SELECT * FROM pets")
    fun getAllListPets(): List<ListPetDetail>

    @Query("DELETE FROM pets")
    suspend fun deleteAll()
}