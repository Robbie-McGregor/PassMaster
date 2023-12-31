package com.robbie_mcgregor.passmaster.ui.passphrase_generator

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.robbie_mcgregor.passmaster.data.PassInterface
import com.robbie_mcgregor.passmaster.util.Routes
import com.robbie_mcgregor.passmaster.databinding.FragmentPassphraseGeneratorBinding

class GeneratePassphraseFragment(private val passInterface: PassInterface) : Fragment() {

    private var _binding: FragmentPassphraseGeneratorBinding? = null
    private lateinit var passphrase: Passphrase

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentPassphraseGeneratorBinding.inflate(inflater, container, false)
        val root: View = binding.root


        setUIListeners()
        initialize()

        return root
    }

    private fun initialize() {
//        Set text of seekbar length
        binding.textViewLengthDisplay.text = binding.seekBarPasswordLength.progress.toString()
//        Initialize passphrase class
        passphrase = Passphrase(
            length = binding.seekBarPasswordLength.progress,
            capitalize = binding.switchCapitalize.isChecked,
            insertNumber = binding.switchIncludeNumber.isChecked,
            separator = binding.editTextSeparator.text.toString()
        )
//        Generate new passphrase
        passphrase.generatePassphrase()
//        Update display
        updatePassphraseDisplay()
    }


    private fun setUIListeners() {
//        Refresh button generates a new passphrase
        binding.buttonRefresh.setOnClickListener {
            passphrase.generatePassphrase()
            updatePassphraseDisplay()
        }
//        Copy button copies to clipboard
        binding.buttonClipboard.setOnClickListener { passInterface.copyToClipboard(name = "Passphrase", content = passphrase.getPassphrase()) }
//        capitalize switch toggles capitalize and updates display
        binding.switchCapitalize.setOnCheckedChangeListener { _, _ ->
            passphrase.setCapitalized(binding.switchCapitalize.isChecked)
            updatePassphraseDisplay()
        }
//        include number switch toggles number and updates display
        binding.switchIncludeNumber.setOnCheckedChangeListener { _, _ ->
            passphrase.setInsertNumber(binding.switchIncludeNumber.isChecked)
            updatePassphraseDisplay()
        }
//        save button to implement once database is added
        binding.buttonSavePassphrase.setOnClickListener { passInterface.navigate(parameters = passphrase.getPassphrase(), route = Routes.CREATE_NEW_ITEM) }
//        when separator text is changed modify and update display
        binding.editTextSeparator.addTextChangedListener {
            passphrase.setSeparator(binding.editTextSeparator.text.toString())
            updatePassphraseDisplay()
        }
//        Delayed scroll to bottom of screen when edittext focuses - otherwise when the keyboard pops up it hides the textview and user has to scroll down
        binding.editTextSeparator.setOnFocusChangeListener { _, _ ->
            Handler().postDelayed({
                binding.scrollview.fullScroll(View.FOCUS_DOWN)
                binding.editTextSeparator.setSelection(binding.editTextSeparator.length())
            }, 200)
        }
//        set up seekbar functionality
        seekbarListener()
    }


    //    Update password length on seekbar change. Also update display text to show length
    private fun seekbarListener() {
        binding.seekBarPasswordLength.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textViewLengthDisplay.text =
                    binding.seekBarPasswordLength.progress.toString()
                passphrase.setLength(binding.seekBarPasswordLength.progress)
                passphrase.generatePassphrase()
                updatePassphraseDisplay()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun updatePassphraseDisplay() {
        binding.textViewPasswordDisplay.text = passphrase.getPassphrase()

    }


}