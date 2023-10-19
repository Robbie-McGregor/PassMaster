package com.robbie_mcgregor.passmaster


interface PassInterface {
    fun viewPass(pass: Pass)
    fun deletePass(pass: Pass)
    fun newPassFragmentCreation(password: String?)
    fun newPassDatabaseCreation(pass: Pass)
    fun editPass(pass: Pass)
    fun updatePass(pass: Pass)
    fun loadHomeScreen()
    fun launchURL(url: String?)
}