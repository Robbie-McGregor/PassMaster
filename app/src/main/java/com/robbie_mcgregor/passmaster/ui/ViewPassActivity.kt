package com.robbie_mcgregor.passmaster.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.robbie_mcgregor.passmaster.Pass
import com.robbie_mcgregor.passmaster.PassDao
import com.robbie_mcgregor.passmaster.PassDatabase
import com.robbie_mcgregor.passmaster.R
import com.robbie_mcgregor.passmaster.databinding.ActivityViewPassBinding
import java.util.concurrent.Executors

class ViewPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPassBinding
    private lateinit var pass: Pass
    private lateinit var passDao: PassDao
    private lateinit var database: PassDatabase
    private var databaseExecutor = Executors.newSingleThreadExecutor()
    private var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPassBinding.inflate(layoutInflater)

        database = PassDatabase.getDatabase(this)
        passDao = database.passDao()

        id = intent.getLongExtra("id", -1)



        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        databaseExecutor.execute {
            pass = passDao.getPass(id)

            runOnUiThread {
                initializeDisplay()
            }

        }
    }

    private fun deleteEntry() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("""
            Are you sure you want to delete this item?
            
            ${pass.name}
            
            Warning - This is permanent.
        """.trimIndent())
        builder.setPositiveButton("Yes") { _, _ ->
            databaseExecutor.execute {
                passDao.deletePass(pass)
                finish()

                runOnUiThread {
                    Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun initializeDisplay() {
        binding.editTextName.setText(pass.name)
        binding.editTextPassword.setText(pass.password)
        binding.editTextUsername.setText(pass.account)
        binding.editTextWebsite.setText(pass.website)
        binding.buttonEdit.setOnClickListener { launchEditActivity() }
        binding.buttonDelete.setOnClickListener { deleteEntry() }
        binding.buttonClose.setOnClickListener { finish() }
    }

    private fun launchEditActivity() {
        val intent = Intent(this, EditPassActivity::class.java)
        intent.putExtra("pass", pass)
        startActivity(intent)
    }
}