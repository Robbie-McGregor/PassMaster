package com.robbie_mcgregor.passmaster.ui.password_generator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.robbie_mcgregor.passmaster.MainActivity
import com.robbie_mcgregor.passmaster.databinding.FragmentPasswordGeneratorBinding
import com.robbie_mcgregor.passmaster.ui.NewPassActivity

class GeneratePasswordFragment : Fragment() {

    private var _binding: FragmentPasswordGeneratorBinding? = null
    private lateinit var password: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentPasswordGeneratorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialize()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun initialize() {
        getPassword()
        binding.textViewLengthDisplay.text = binding.seekBarPasswordLength.progress.toString()

        setUIListeners()
    }

    private fun setUIListeners() {
        binding.buttonRefresh.setOnClickListener { getPassword() }
        binding.buttonClipboard.setOnClickListener { copyToClipboard() }
        binding.switchCapitalLetters.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.switchUnderscoreLetters.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.switchNumbers.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.switchSpecialCharacters.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.buttonSavePassword.setOnClickListener { savePassword() }
        seekbarListener()
    }

    private fun savePassword() {
        val intent = Intent(activity?.applicationContext, NewPassActivity::class.java)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    private fun copyToClipboard() {
        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("password", password)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(activity, "Copied To Clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun seekbarListener() {
        binding.seekBarPasswordLength.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textViewLengthDisplay.text =
                    binding.seekBarPasswordLength.progress.toString()
                getPassword()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun getPassword() {
        if(!binding.switchCapitalLetters.isChecked && !binding.switchUnderscoreLetters.isChecked && !binding.switchNumbers.isChecked && !binding.switchSpecialCharacters.isChecked){
            binding.switchCapitalLetters.isChecked = true
            return
        }
        password = Password(
            length = binding.seekBarPasswordLength.progress,
            includeUppercase = binding.switchCapitalLetters.isChecked,
            includeLowercase = binding.switchUnderscoreLetters.isChecked,
            includeDigits = binding.switchNumbers.isChecked,
            includeSpecialCharacters = binding.switchSpecialCharacters.isChecked
        ).generatePassword()
        binding.textViewPasswordDisplay.text = password
    }


}
