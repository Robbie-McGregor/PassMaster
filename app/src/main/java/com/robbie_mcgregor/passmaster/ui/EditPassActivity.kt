package com.robbie_mcgregor.passmaster.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.robbie_mcgregor.passmaster.Pass
import com.robbie_mcgregor.passmaster.PassDao
import com.robbie_mcgregor.passmaster.PassDatabase
import com.robbie_mcgregor.passmaster.databinding.ActivityEditPassBinding
import java.util.concurrent.Executors

class EditPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPassBinding
    private lateinit var pass: Pass
    private lateinit var passDao: PassDao
    private lateinit var database: PassDatabase
    private var databaseExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPassBinding.inflate(layoutInflater)
        pass = intent.getSerializableExtra("pass") as Pass

        database = PassDatabase.getDatabase(this)
        passDao = database.passDao()

        initializeDisplay()
        setupListeners()

        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.buttonCancel.setOnClickListener { finish() }
        binding.buttonSave.setOnClickListener { updateEntry() }
    }



    private fun updateEntry() {
        if (binding.editTextName.text?.isEmpty() == true){
            Toast.makeText(this, "Error. Name cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        pass.name = binding.editTextName.text.toString()
        pass.account = binding.editTextUsername.text.toString()
        pass.website = binding.editTextWebsite.text.toString()
        pass.password = binding.editTextPassword.text.toString()

        databaseExecutor.execute {
            passDao.updatePass(pass)
            runOnUiThread {
                Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun initializeDisplay() {
        binding.editTextName.setText(pass.name)
        binding.editTextPassword.setText(pass.password)
        binding.editTextUsername.setText(pass.account)
        binding.editTextWebsite.setText(pass.website)
    }
}