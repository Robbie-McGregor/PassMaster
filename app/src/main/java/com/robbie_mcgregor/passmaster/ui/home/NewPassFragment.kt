package com.robbie_mcgregor.passmaster.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.robbie_mcgregor.passmaster.data.Pass
import com.robbie_mcgregor.passmaster.data.PassInterface
import com.robbie_mcgregor.passmaster.databinding.FragmentNewPassBinding


class NewPassFragment(private val password: String?, private val passInterface: PassInterface) : Fragment() {
    private var _binding: FragmentNewPassBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewPassBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initializeDisplay()
        return root
    }

    private fun initializeDisplay() {
        if (password != null) {
            binding.editTextPassword.setText(password)
        }
        binding.buttonSave.setOnClickListener { save() }
    }

    private fun save() {
        if (binding.editTextName.text?.isEmpty() == true) {
            Toast.makeText(requireContext(), "Error Saving. Name is Required", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val newPass = Pass(
            name = binding.editTextName.text.toString(),
            account = binding.editTextUsername.text.toString(),
            password = binding.editTextPassword.text.toString(),
            website = binding.editTextWebsite.text.toString()
        )
        passInterface.saveIntoDatabase(pass = newPass, message = "New Pass ${newPass.name} Added.")
    }
}