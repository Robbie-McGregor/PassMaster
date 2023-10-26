package com.robbie_mcgregor.passmaster.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors


@Database(entities = [Pass::class], version = PassDatabase.VERSION)
abstract class PassDatabase : RoomDatabase() {

    abstract fun passDao(): PassDao

    companion object {

        const val VERSION = 1

        private var INSTANCE: PassDatabase? = null
        fun getDatabase(context: Context): PassDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context,
                            PassDatabase::class.java,
                            "pass_database"
                        ).build()
                }
            }
            return INSTANCE!!
        }
    }
}






