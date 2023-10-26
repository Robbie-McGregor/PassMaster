package com.robbie_mcgregor.passmaster.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePass(pass: Pass): Long

    @Delete
    fun deletePass(pass: Pass)

    @Query("SELECT * FROM passwords WHERE id=:id")
    fun getPass(id: Long): Pass

    @Query("SELECT * from passwords")
    fun getAllPasses(): List<Pass>
}
