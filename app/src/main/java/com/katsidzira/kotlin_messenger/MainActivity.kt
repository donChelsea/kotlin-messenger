package com.katsidzira.kotlin_messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.katsidzira.kotlin_messenger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val TAG: String = "main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            registerUser()
            // move to the next activity's main fragment
        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
            // move to next activity's login fragment
        }
    }

    private fun registerUser() {
        val name = binding.usernameEdit.text.toString()
        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Missing or incorrect fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d(TAG, "success: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d(TAG, "could not create user: ${it.message}")
                Toast.makeText(this, "Could not create user", Toast.LENGTH_SHORT).show()

            }
    }
}
