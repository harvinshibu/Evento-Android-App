package com.harvin.evento.Guest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.harvin.evento.GuestFragment.GuestAccountFragment
import com.harvin.evento.GuestFragment.GuestBookFragment
import com.harvin.evento.GuestFragment.GuestChatFragment
import com.harvin.evento.GuestFragment.GuestHomeFragment
import com.harvin.evento.R
import com.harvin.evento.databinding.ActivityGuestHomeBinding

class GuestHomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityGuestHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGuestHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(GuestHomeFragment())

        binding.bottomNavigationGuest.setOnItemSelectedListener(this)
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.guest_fragment_container,fragment)
        transaction.commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(GuestHomeFragment())
            R.id.nav_chats -> loadFragment(GuestChatFragment())
            R.id.nav_book -> loadFragment(GuestBookFragment())
            R.id.nav_account -> loadFragment(GuestAccountFragment())
        }
        return true
    }
}