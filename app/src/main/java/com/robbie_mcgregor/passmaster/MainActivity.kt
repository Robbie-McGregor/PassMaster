package com.robbie_mcgregor.passmaster

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.robbie_mcgregor.passmaster.data.Pass
import com.robbie_mcgregor.passmaster.data.PassDao
import com.robbie_mcgregor.passmaster.data.PassDatabase
import com.robbie_mcgregor.passmaster.databinding.ActivityMainBinding
import com.robbie_mcgregor.passmaster.ui.home.EditPassFragment
import com.robbie_mcgregor.passmaster.ui.home.HomeFragment
import com.robbie_mcgregor.passmaster.ui.home.NewPassFragment
import com.robbie_mcgregor.passmaster.ui.home.ViewPassFragment
import com.robbie_mcgregor.passmaster.ui.passphrase_generator.GeneratePassphraseFragment
import com.robbie_mcgregor.passmaster.ui.password_generator.GeneratePasswordFragment
import com.robbie_mcgregor.passmaster.data.PassInterface
import com.robbie_mcgregor.passmaster.util.Routes
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



        setupBottomNavigationListeners()
        navigate(route = Routes.HOME)


    }

    private fun setupBottomNavigationListeners() {
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    navigate(route = Routes.HOME)
                    true
                }

                R.id.navigation_passphrase_generator -> {
                    navigate(route = Routes.PASSPHRASE_GENERATOR)
                    true
                }

                R.id.navigation_password_generator -> {
                    navigate(route = Routes.PASSWORD_GENERATOR)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }




    //    Interface Implementations:


    override fun navigate(route: String, parameters: Any?) {

        var fragment: Fragment? = null

        when (route) {
            Routes.HOME -> {
                fragment = homeFragment
            }

            Routes.PASSWORD_GENERATOR -> {
                fragment = passwordFragment
            }

            Routes.PASSPHRASE_GENERATOR -> {
                fragment = passphraseFragment
            }

            Routes.VIEW_ITEM -> {
                val pass = parameters as Pass
                fragment = ViewPassFragment(pass = pass, passInterface = this)
            }

            Routes.CREATE_NEW_ITEM -> {
                var password = ""
                if (parameters != null) {
                    password = parameters as String
                }
                fragment = NewPassFragment(password = password, passInterface = this)
            }

            Routes.EDIT_ITEM -> {
                val pass = parameters as Pass
                fragment = EditPassFragment(pass = pass, passInterface = this)
            }

            Routes.BACK -> {
                supportFragmentManager.popBackStack()
            }
        }

        if (fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun deletePass(pass: Pass) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(
            """
            Are you sure you want to delete this item?
            
            ${pass.name}
            
            Warning - This is permanent.
        """.trimIndent()
        )
        builder.setPositiveButton("Yes") { _, _ ->
            databaseExecutor.execute {
                passDao.deletePass(pass)
            }
            runOnUiThread {
                Toast.makeText(this, "DELETED PASS: ${pass.name}", Toast.LENGTH_SHORT).show()
                navigate(Routes.HOME)
            }
        }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }



    override fun saveIntoDatabase(pass: Pass, message: String) {
        databaseExecutor.execute { passDao.savePass(pass) }
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            navigate(Routes.HOME)
        }
    }





    override fun launchURL(url: String?) {
//        new URL string that can be modified to add http:// if necessary
        var urlString = url

//        Exit function on no valid URL
        if (urlString == null || urlString == "") {
            Toast.makeText(this, "Website Is Empty, Cannot Launch", Toast.LENGTH_SHORT).show()
            return
        }

//        Make sure that URL starts with either http:// or https://
        if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
            urlString = "http://" + url;
        }

//Launch Browser
        val webpage: Uri = Uri.parse(urlString)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

    override fun copyToClipboard(name: String, content: String?) {
        if (content == null || content == "") {
            Toast.makeText(this, "$name Is Empty, Nothing To Copy", Toast.LENGTH_SHORT).show()
            return
        }

        val clipboard =
            this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(name, content)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied $name To Clipboard", Toast.LENGTH_SHORT).show()
    }
}
