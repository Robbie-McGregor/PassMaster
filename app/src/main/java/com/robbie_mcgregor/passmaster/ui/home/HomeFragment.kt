package com.robbie_mcgregor.passmaster.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.robbie_mcgregor.passmaster.Pass
import com.robbie_mcgregor.passmaster.PassDao
import com.robbie_mcgregor.passmaster.PassDatabase
import com.robbie_mcgregor.passmaster.PassInterface
import com.robbie_mcgregor.passmaster.R
import com.robbie_mcgregor.passmaster.databinding.FragmentHomeBinding
import com.robbie_mcgregor.passmaster.ui.EditPassActivity
import com.robbie_mcgregor.passmaster.ui.NewPassActivity
import com.robbie_mcgregor.passmaster.ui.ViewPassActivity
import java.util.concurrent.Executors

class HomeFragment : Fragment(), PassInterface {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var passwordRowItemAdapter: PasswordRowItemAdapter
    private var mList = ArrayList<Pass>()
    private lateinit var passDao: PassDao
    private lateinit var passDatabase: PassDatabase
    private var databaseExecutor = Executors.newFixedThreadPool(4)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        initialize()



        return root
    }

    override fun onResume() {
        super.onResume()
        databaseExecutor.execute {

            mList = passDao.getAllPasses() as ArrayList<Pass>

            requireActivity().runOnUiThread {
                passwordRowItemAdapter = PasswordRowItemAdapter(mList, this)
                binding.recyclerViewPasswords.adapter = passwordRowItemAdapter
            }
        }
    }

    private fun initialize() {
        passDatabase = PassDatabase.getDatabase(requireContext())
        passDao = passDatabase.passDao()

        binding.buttonAddNew.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, NewPassActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun viewPass(pass: Pass) {
        val intent = Intent(requireActivity().applicationContext, ViewPassActivity::class.java)
        intent.putExtra("id", pass.id)
        startActivity(intent)
    }
}