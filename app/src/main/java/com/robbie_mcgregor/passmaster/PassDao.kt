package com.robbie_mcgregor.passmaster

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PassDao {

    @Insert
    fun insertPass(pass: Pass): Long

    @Delete
    fun deletePass(pass: Pass)

    @Update
    fun updatePass(pass: Pass)

    @Query("SELECT * FROM passwords WHERE id=:id")
    fun getPass(id: Long): Pass

    @Query("SELECT * from passwords")
    fun getAllPasses():List<Pass>
}
