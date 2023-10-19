package com.robbie_mcgregor.passmaster.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.robbie_mcgregor.passmaster.Pass
import com.robbie_mcgregor.passmaster.PassInterface
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
        binding.buttonEdit.setOnClickListener { passInterface.editPass(pass) }
        binding.buttonDelete.setOnClickListener { deleteEntry() }
        binding.buttonClose.setOnClickListener { passInterface.loadHomeScreen() }
        binding.buttonCopyPassword.setOnClickListener {
            copyToClipboard(
                name = "Password",
                content = pass.password
            )
        }
        binding.buttonCopyUsername.setOnClickListener {
            copyToClipboard(
                name = "Username",
                content = pass.account
            )
        }
        binding.buttonLaunchWebsite.setOnClickListener { passInterface.launchURL(url = pass.website) }
    }

    private fun deleteEntry() {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setMessage(
            """
            Are you sure you want to delete this item?
            
            ${pass.name}
            
            Warning - This is permanent.
        """.trimIndent()
        )
        builder.setPositiveButton("Yes") { _, _ ->
            passInterface.deletePass(pass)
        }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun copyToClipboard(name: String, content: String?) {
        if (content == null || content == "") {
            Toast.makeText(activity, "$name Is Empty, Nothing To Copy", Toast.LENGTH_SHORT).show()
            return
        }

        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(name, content)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(activity, "Copied $name To Clipboard", Toast.LENGTH_SHORT).show()
    }

}