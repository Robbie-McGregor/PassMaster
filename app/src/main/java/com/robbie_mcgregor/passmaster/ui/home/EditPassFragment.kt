package com.robbie_mcgregor.passmaster.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.robbie_mcgregor.passmaster.Pass
import com.robbie_mcgregor.passmaster.PassInterface
import com.robbie_mcgregor.passmaster.databinding.FragmentEditPassBinding


class EditPassFragment(private val pass: Pass, private val passInterface: PassInterface) : Fragment() {
    private var _binding: FragmentEditPassBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditPassBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initializeDisplay()

        return root
    }

    private fun initializeDisplay() {
        binding.editTextName.setText(pass.name)
        binding.editTextPassword.setText(pass.password)
        binding.editTextUsername.setText(pass.account)
        binding.editTextWebsite.setText(pass.website)
        binding.buttonCancel.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.buttonSave.setOnClickListener { updateEntry() }
    }

    private fun updateEntry(){
        if (binding.editTextName.text?.isEmpty() == true){
            Toast.makeText(requireContext(), "Error. Name cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        pass.name = binding.editTextName.text.toString()
        pass.account = binding.editTextUsername.text.toString()
        pass.website = binding.editTextWebsite.text.toString()
        pass.password = binding.editTextPassword.text.toString()

        passInterface.updatePass(pass)
    }


}