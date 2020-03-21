package com.katsidzira.kotlin_messenger

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.katsidzira.kotlin_messenger.register.LoginFragment
import com.katsidzira.kotlin_messenger.register.MainActivity

class NextActivity : AppCompatActivity(),
    LoginFragment.onLoggedInListener,
    LatestMessagesFragment.onLatestMessagesListener,
    NewMessageFragment.OnNewMessageListener {

    private val TAG: String = "next activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        supportActionBar!!.title = "Messages"

        if (FirebaseAuth.getInstance().uid != null) {
            onUserLoggedIn()
        } else {
            goToLogInScreen()
        }
    }

    private fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, fragment!!)

        // add other fragments with call to replace
        transaction.commit()
    }

    private fun goToLogInScreen() {
        val loginFragment = LoginFragment.newInstance()
        replaceFragment(loginFragment)
    }

    override fun onUserLoggedIn() {
        Log.d(TAG, "going to profile")
        val lastestMessagesFragment = LatestMessagesFragment.newInstance()
        replaceFragment(lastestMessagesFragment)
    }

    override fun registerNewUser() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun startNewMessage() {
        // go to new message screen
        val newMessageFragment = NewMessageFragment.newInstance()
        replaceFragment(newMessageFragment)
    }

    override fun sendMessage() {
        // begin new message
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {

            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                goToLogInScreen()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is LoginFragment) {
            fragment.setOnLoggedInListener(this)
        }
    }

}
