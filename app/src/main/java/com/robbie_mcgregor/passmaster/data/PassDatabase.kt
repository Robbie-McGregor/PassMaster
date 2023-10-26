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
                        )
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    val dao = INSTANCE?.passDao()
                                    Executors.newSingleThreadExecutor().execute {
                                        dao?.savePass(
                                            Pass(
                                                name = "This is a sample entry",
                                                account = "Your username/email goes here",
                                                password = "Enter a password here",
                                                website = "Insert the website URL here."
                                            )
                                        )
                                    }

                                }

                            })
                            .build().also { INSTANCE = it }
                }
            }
            return INSTANCE!!
        }
    }
}






