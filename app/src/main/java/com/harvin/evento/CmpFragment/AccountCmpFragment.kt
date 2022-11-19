package com.harvin.evento.CmpFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.harvin.evento.AboutUsActivity
import com.harvin.evento.Company.CmpEditProfileActivity
import com.harvin.evento.Company.CmpHomeActivity
import com.harvin.evento.MainActivity
import com.harvin.evento.databinding.FragmentAccountCmpBinding

class AccountCmpFragment : Fragment() {
    private lateinit var binding:FragmentAccountCmpBinding
 override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
     binding= FragmentAccountCmpBinding.inflate(inflater,container,false)

     binding.logoutCmp.setOnClickListener {
         FirebaseAuth.getInstance().signOut()
         val i = Intent(activity, MainActivity::class.java)
         i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
         startActivity(i)
     }
     binding.viewCmpProfile.setOnClickListener {
         startActivity(Intent(activity, CmpEditProfileActivity::class.java))
     }

     binding.textView23.setOnClickListener {
         startActivity(Intent(activity, AboutUsActivity::class.java))
     }

     return binding.root
    }

}