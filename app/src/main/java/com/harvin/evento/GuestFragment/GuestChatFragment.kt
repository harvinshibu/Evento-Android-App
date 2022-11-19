package com.harvin.evento.GuestFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harvin.evento.MainActivity
import com.harvin.evento.R
import com.harvin.evento.databinding.FragmentGuestChatBinding
import com.harvin.evento.databinding.FragmentGuestHomeBinding

class GuestChatFragment : Fragment() {
    private lateinit var binding:FragmentGuestChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentGuestChatBinding.inflate(inflater,container,false)

        binding.usrLoginChat.setOnClickListener {
            startActivity(Intent(activity,MainActivity::class.java))
        }

        return binding.root
    }


}