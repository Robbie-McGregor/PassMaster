package com.robbie_mcgregor.passmaster.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robbie_mcgregor.passmaster.data.Pass
import com.robbie_mcgregor.passmaster.data.PassDao
import com.robbie_mcgregor.passmaster.data.PassDatabase
import com.robbie_mcgregor.passmaster.data.PassInterface
import com.robbie_mcgregor.passmaster.util.Routes
import com.robbie_mcgregor.passmaster.databinding.FragmentHomeBinding
import java.util.concurrent.Executors

class HomeFragment(private val passInterface: PassInterface) : Fragment() {

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

            if (mList.isEmpty()) {
//                When the list is empty, show this instead
                binding.emptyList.visibility = View.VISIBLE
            } else {
//                Otherwise populate list
                binding.emptyList.visibility = View.GONE
                requireActivity().runOnUiThread {
                    passwordRowItemAdapter =
                        PasswordRowItemAdapter(mList, passInterface = passInterface)
                    binding.recyclerViewPasswords.adapter = passwordRowItemAdapter
                }
            }


        }
    }

    // Create a view to add instead of the recycler view when the list is empty.


    private fun initialize() {
        passDatabase = PassDatabase.getDatabase(requireContext())
        passDao = passDatabase.passDao()

        binding.buttonAddNew.setOnClickListener { passInterface.navigate(Routes.CREATE_NEW_ITEM) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}