package com.katsidzira.kotlin_messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

class NextActivity : AppCompatActivity(), LoginFragment.LoggedInListener {
    private val TAG: String = "next activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        val loginFragment = LoginFragment.newInstance()
        replaceFragment(loginFragment)

    }

    fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, fragment!!)

        // add other fragments with call to replace
        transaction.commit()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is LoginFragment) {
            fragment.setOnLoggedInListener(this)
        }
    }

    override fun onUserLoggedIn() {
        //create and go to priofile frag
        Log.d(TAG, "going to prfile")
    }


}
