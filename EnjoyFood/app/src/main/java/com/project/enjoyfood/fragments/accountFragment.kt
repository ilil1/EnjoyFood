package com.project.enjoyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.project.enjoyfood.R
import com.project.enjoyfood.databinding.FragmentAccountBinding

class accountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding : FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }
}