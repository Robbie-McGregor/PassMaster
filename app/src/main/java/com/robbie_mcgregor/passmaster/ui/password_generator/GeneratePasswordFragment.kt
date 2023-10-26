package com.robbie_mcgregor.passmaster.ui.password_generator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.robbie_mcgregor.passmaster.data.PassInterface
import com.robbie_mcgregor.passmaster.util.Routes
import com.robbie_mcgregor.passmaster.databinding.FragmentPasswordGeneratorBinding

class GeneratePasswordFragment(private val passInterface: PassInterface) : Fragment() {

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
        binding.buttonClipboard.setOnClickListener { passInterface.copyToClipboard(name = "Password", content = password) }
        binding.switchCapitalLetters.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.switchUnderscoreLetters.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.switchNumbers.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.switchSpecialCharacters.setOnCheckedChangeListener { _, _ -> getPassword() }
        binding.buttonSavePassword.setOnClickListener { passInterface.navigate(parameters = password, route = Routes.CREATE_NEW_ITEM) }
        seekbarListener()
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
