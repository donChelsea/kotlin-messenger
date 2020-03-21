package com.katsidzira.kotlin_messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

class NextActivity : AppCompatActivity(), LoginFragment.onLoggedInListener, LatestMessagesFragment.onLatestMessagesListener {
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

    override fun onUserLoggedIn() {
        Log.d(TAG, "going to profile")
        val lastestMessagesFragment = LatestMessagesFragment.newInstance()
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, lastestMessagesFragment)
        transaction.commit()
    }

    override fun createNewMessage() {
        TODO("Not yet implemented")
    }

    override fun viewChatLog() {
        TODO("Not yet implemented")
    }

//    override fun onAttachFragment(fragment: Fragment?) {
//        super.onAttachFragment(fragment)
//        if (fragment is LoginFragment) {
//            fragment.setOnLoggedInListener(this)
//        }
//    }

}
