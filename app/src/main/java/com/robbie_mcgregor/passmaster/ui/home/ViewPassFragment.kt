package com.robbie_mcgregor.passmaster.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.robbie_mcgregor.passmaster.Pass
import com.robbie_mcgregor.passmaster.PassInterface
import com.robbie_mcgregor.passmaster.databinding.FragmentViewPassBinding


class ViewPassFragment(private val pass: Pass, private val passInterface: PassInterface) : Fragment() {
    private var _binding: FragmentViewPassBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        initializeDisplay()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPassBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initializeDisplay() {
        binding.editTextName.setText(pass.name)
        binding.editTextPassword.setText(pass.password)
        binding.editTextUsername.setText(pass.account)
        binding.editTextWebsite.setText(pass.website)
        binding.buttonEdit.setOnClickListener { passInterface.editPass(pass) }
        binding.buttonDelete.setOnClickListener { deleteEntry() }
        binding.buttonClose.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
    }

    private fun deleteEntry() {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setMessage("""
            Are you sure you want to delete this item?
            
            ${pass.name}
            
            Warning - This is permanent.
        """.trimIndent())
        builder.setPositiveButton("Yes") { _, _ ->
            passInterface.deletePass(pass)
        }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

}