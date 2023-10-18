package com.robbie_mcgregor.passmaster

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.robbie_mcgregor.passmaster.databinding.ActivityMainBinding
import com.robbie_mcgregor.passmaster.ui.home.EditPassFragment
import com.robbie_mcgregor.passmaster.ui.home.HomeFragment
import com.robbie_mcgregor.passmaster.ui.home.NewPassFragment
import com.robbie_mcgregor.passmaster.ui.home.ViewPassFragment
import com.robbie_mcgregor.passmaster.ui.passphrase_generator.GeneratePassphraseFragment
import com.robbie_mcgregor.passmaster.ui.password_generator.GeneratePasswordFragment
import java.lang.Exception
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), PassInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var passphraseFragment: GeneratePassphraseFragment
    private lateinit var passwordFragment: GeneratePasswordFragment

    private lateinit var passDao: PassDao
    private lateinit var database: PassDatabase
    private var databaseExecutor = Executors.newSingleThreadExecutor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFragment = HomeFragment(passInterface = this)
        passphraseFragment = GeneratePassphraseFragment(passInterface = this)
        passwordFragment = GeneratePasswordFragment(passInterface = this)

        database = PassDatabase.getDatabase(this)
        passDao = database.passDao()


        setCurrentFragment(homeFragment)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    setCurrentFragment(homeFragment)
                }

                R.id.navigation_passphrase_generator -> {
                    setCurrentFragment(passphraseFragment)
                }

                R.id.navigation_password_generator -> {
                    setCurrentFragment(passwordFragment)
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment): Boolean {
        return try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            true
        } catch (e: Exception) {
            false
        }
    }


    //    Interface Implementations:
    override fun viewPass(pass: Pass) {
        setCurrentFragment(ViewPassFragment(pass = pass, passInterface = this))
    }

    override fun deletePass(pass: Pass) {
        databaseExecutor.execute {
            passDao.deletePass(pass)
        }
        runOnUiThread {
            Toast.makeText(this, "DELETED PASS: ${pass.name}", Toast.LENGTH_SHORT).show()
            setCurrentFragment(homeFragment)
        }
    }

    override fun newPassFragmentCreation(password: String?) {
        setCurrentFragment(NewPassFragment(password = password, passInterface = this))
    }

    override fun newPassDatabaseCreation(pass: Pass) {
        databaseExecutor.execute { passDao.insertPass(pass) }
        runOnUiThread {
            Toast.makeText(this, "New Pass Added: ${pass.name}", Toast.LENGTH_SHORT).show()
            setCurrentFragment(homeFragment)
        }
    }

    override fun editPass(pass: Pass) {
        setCurrentFragment(EditPassFragment(pass = pass, passInterface = this))
    }

    override fun updatePass(pass: Pass) {
        databaseExecutor.execute {
            passDao.updatePass(pass)
            runOnUiThread {
                Toast.makeText(this, "Pass Updated: ${pass.name}", Toast.LENGTH_SHORT).show()
                setCurrentFragment(ViewPassFragment(pass = pass, passInterface = this))
            }

        }
    }
}