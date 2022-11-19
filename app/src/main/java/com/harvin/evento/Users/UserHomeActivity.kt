package com.harvin.evento.Users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.harvin.evento.R
import com.harvin.evento.UserFragment.UserAccountFragment
import com.harvin.evento.UserFragment.UserBookFragment
import com.harvin.evento.UserFragment.UserChatFragment
import com.harvin.evento.UserFragment.UserHomeFragment
import com.harvin.evento.databinding.ActivityUserHomeBinding

class UserHomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityUserHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(UserHomeFragment())

        binding.bottomNavigationUsr.setOnItemSelectedListener(this)
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.usr_fragment_container,fragment)
        transaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(UserHomeFragment())
            R.id.nav_chats -> loadFragment(UserChatFragment())
            R.id.nav_book -> loadFragment(UserBookFragment())
            R.id.nav_account -> loadFragment(UserAccountFragment())
        }
        return true
    }



}