package com.robbie_mcgregor.passmaster.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robbie_mcgregor.passmaster.data.Pass
import com.robbie_mcgregor.passmaster.data.PassInterface
import com.robbie_mcgregor.passmaster.util.Routes
import com.robbie_mcgregor.passmaster.databinding.RowItemPassBinding

class PasswordRowItemAdapter(private val mList: ArrayList<Pass>, private val passInterface: PassInterface) :
    RecyclerView.Adapter<PasswordRowItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemPassBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]

        holder.bind(item)


    }


    inner class ViewHolder(val binding: RowItemPassBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pass: Pass) {
            binding.textViewName.text = pass.name
            binding.textViewAccount.text = pass.account

            binding.root.setOnClickListener { passInterface.navigate(route = Routes.VIEW_ITEM, parameters = pass) }
        }


    }

}





