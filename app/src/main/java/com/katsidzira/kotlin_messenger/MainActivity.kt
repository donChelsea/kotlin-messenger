package com.katsidzira.kotlin_messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.katsidzira.kotlin_messenger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val name = binding.usernameEdit.text
        val email = binding.emailEdit.text
        val password = binding.passwordEdit.text

        binding.registerButton.setOnClickListener {
            Toast.makeText(this, "Registered: $name", Toast.LENGTH_SHORT).show()
        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
    }
}
