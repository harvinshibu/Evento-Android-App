package com.harvin.evento.Company

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.harvin.evento.CmpFragment.AccountCmpFragment
import com.harvin.evento.CmpFragment.BookCmpFragment
import com.harvin.evento.CmpFragment.ChatCmpFragment
import com.harvin.evento.CmpFragment.HomeCmpFragment
import com.harvin.evento.R
import com.harvin.evento.databinding.ActivityCmpHomeBinding


class CmpHomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityCmpHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCmpHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(HomeCmpFragment())

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)

    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.cmp_fragment_container,fragment)
        transaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(HomeCmpFragment())
            R.id.nav_chats -> loadFragment(ChatCmpFragment())
            R.id.nav_book -> loadFragment(BookCmpFragment())
            R.id.nav_setting -> loadFragment(AccountCmpFragment())
        }
        return true
    }
}


