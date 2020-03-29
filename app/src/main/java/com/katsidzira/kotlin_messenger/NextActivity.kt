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
import com.katsidzira.kotlin_messenger.messages.ChatLogFragment
import com.katsidzira.kotlin_messenger.messages.LatestMessagesFragment
import com.katsidzira.kotlin_messenger.messages.NewMessageFragment
import com.katsidzira.kotlin_messenger.messages.UserItem
import com.katsidzira.kotlin_messenger.model.User
import com.katsidzira.kotlin_messenger.registerlogin.LoginFragment
import com.katsidzira.kotlin_messenger.registerlogin.MainActivity

class NextActivity : AppCompatActivity(),
    LoginFragment.onLoggedInListener,
    LatestMessagesFragment.OnLatestMessagesListener,
    NewMessageFragment.OnNewMessageListener,
    ChatLogFragment.OnChatLogListener{

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
            .addToBackStack("next")
            .replace(R.id.frag_container, fragment!!)
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

    override fun chooseUserToMessage(chatPartnerUser: User?) {
        // go to new message screen
        val chatLogFragment = ChatLogFragment.newInstance()
        chatLogFragment.apply {
            arguments = Bundle().apply {
                putParcelable("user key", chatPartnerUser)
            }
        }
        replaceFragment(chatLogFragment)
    }

    override fun startMessageConvo(userItem: UserItem) {
        val USER_KEY = "user key"
        // go to chat log to begin new message
        val chatLogFragment = ChatLogFragment.newInstance()
        chatLogFragment.apply {
            arguments = Bundle().apply {
                putParcelable(USER_KEY, userItem.user)
            }
        }
        val transaction = supportFragmentManager
            .beginTransaction()
            .addToBackStack("next")
            .replace(R.id.frag_container, chatLogFragment)

        transaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val newMessagesFragment = NewMessageFragment.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                    .addToBackStack("new message")
                    .replace(R.id.frag_container, newMessagesFragment)
                transaction.commit()
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

    override fun onFragmentInteraction(uri: Uri) {
        // from chat log fragment
    }

}
