package com.harvin.evento.GuestFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harvin.evento.AboutUsActivity
import com.harvin.evento.MainActivity
import com.harvin.evento.R
import com.harvin.evento.databinding.FragmentGuestAccountBinding
import com.harvin.evento.databinding.FragmentGuestBookBinding
import com.harvin.evento.databinding.FragmentGuestChatBinding


class GuestAccountFragment : Fragment() {

    private lateinit var binding: FragmentGuestAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentGuestAccountBinding.inflate(inflater,container,false)

        binding.guestLogAcc.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }

        binding.textView23.setOnClickListener {
            startActivity(Intent(activity, AboutUsActivity::class.java))
        }

        return binding.root
    }

}