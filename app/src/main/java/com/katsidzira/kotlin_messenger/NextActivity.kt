package com.katsidzira.kotlin_messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        val loginFragment = LoginFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frag_container, loginFragment)
            .addToBackStack("login")
            .commit()
    }
}
