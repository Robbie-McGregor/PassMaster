package com.robbie_mcgregor.passmaster.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.robbie_mcgregor.passmaster.Pass
import com.robbie_mcgregor.passmaster.PassDao
import com.robbie_mcgregor.passmaster.PassDatabase
import com.robbie_mcgregor.passmaster.databinding.ActivityNewPassBinding
import java.util.concurrent.Executors

class NewPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPassBinding
    private var password: String = ""
    private lateinit var passDao: PassDao
    private lateinit var database: PassDatabase
    private val databaseExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = PassDatabase.getDatabase(this)
        passDao = database.passDao()

        initialize()
    }

    private fun initialize() {
        if (intent.getStringExtra("password") != null) {
            password = intent.getStringExtra("password").toString()
            binding.editTextPassword.setText(password)
        }
        binding.buttonSave.setOnClickListener { save() }
    }

    private fun save() {
        if (binding.editTextName.text?.isEmpty() == true) {
            Toast.makeText(applicationContext, "Error Saving. Name is Required", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val newPass = Pass(
            name = binding.editTextName.text.toString(),
            account = binding.editTextUsername.text.toString(),
            password = binding.editTextPassword.text.toString(),
            website = binding.editTextWebsite.text.toString()
        )

        var newPassId: Long
        databaseExecutor.execute {
            newPassId = passDao.insertPass(newPass)

            runOnUiThread {
                Toast.makeText(this, "New Pass Added", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}