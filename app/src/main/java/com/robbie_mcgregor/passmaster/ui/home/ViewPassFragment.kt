package com.robbie_mcgregor.passmaster.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.robbie_mcgregor.passmaster.data.Pass
import com.robbie_mcgregor.passmaster.data.PassInterface
import com.robbie_mcgregor.passmaster.util.Routes
import com.robbie_mcgregor.passmaster.databinding.FragmentViewPassBinding


class ViewPassFragment(private val pass: Pass, private val passInterface: PassInterface) :
    Fragment() {
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
        binding.buttonEdit.setOnClickListener { passInterface.navigate(route = Routes.EDIT_ITEM, parameters = pass) }
        binding.buttonDelete.setOnClickListener { deleteEntry() }
        binding.buttonClose.setOnClickListener { passInterface.navigate(Routes.HOME) }
        binding.buttonCopyPassword.setOnClickListener {
            passInterface.copyToClipboard(
                name = "Password",
                content = pass.password
            )
        }
        binding.buttonCopyUsername.setOnClickListener {
            passInterface.copyToClipboard(
                name = "Username",
                content = pass.account
            )
        }
        binding.buttonLaunchWebsite.setOnClickListener { passInterface.launchURL(url = pass.website) }
    }

    private fun deleteEntry() {
        passInterface.deletePass(pass)
    }

}