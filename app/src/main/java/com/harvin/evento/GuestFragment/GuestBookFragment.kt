package com.harvin.evento.GuestFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harvin.evento.MainActivity
import com.harvin.evento.R
import com.harvin.evento.databinding.FragmentGuestBookBinding
import com.harvin.evento.databinding.FragmentGuestChatBinding


class GuestBookFragment : Fragment() {

    private lateinit var binding: FragmentGuestBookBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentGuestBookBinding.inflate(inflater,container,false)

        binding.usrLoginBook.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }

        return binding.root
    }

}