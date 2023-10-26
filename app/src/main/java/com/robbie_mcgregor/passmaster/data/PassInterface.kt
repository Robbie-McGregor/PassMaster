package com.robbie_mcgregor.passmaster.data

import com.robbie_mcgregor.passmaster.data.Pass


interface PassInterface {
    fun deletePass(pass: Pass)
    fun saveIntoDatabase(pass: Pass, message: String)

    fun launchURL(url: String?)
    fun copyToClipboard(name: String, content: String?)

    fun navigate(route: String, parameters: Any? = null)
}