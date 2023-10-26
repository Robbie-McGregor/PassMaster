package com.robbie_mcgregor.passmaster.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "passwords", )
data class Pass (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    var name: String,
    var account: String? = null,
    val dateModified: String? = null,
    var password: String? = null,
    var website: String? = null
)