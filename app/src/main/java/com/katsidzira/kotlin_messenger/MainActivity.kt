package com.katsidzira.kotlin_messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.katsidzira.kotlin_messenger.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private val TAG: String = "main"
    private var selectedPhotoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.registerButton.setOnClickListener {
            registerUser()
            // move to the next activity's main fragment
        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
            // move to next activity's login fragment
        }

        binding.selectPhotoButton.setOnClickListener {
            Log.d(TAG, "try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "photo was selected")
        }
        selectedPhotoUri = data!!.data
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
        val d: Drawable = BitmapDrawable(resources, bitmap)

        binding.selectPhotoButton.setBackgroundDrawable(d)
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

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d(TAG, "could not create user: ${it.message}")
                Toast.makeText(this, "Could not create user", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = storage.getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d(TAG, "successfully uploaded image: ${it.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                Log.d(TAG, "file location: $it")
                saveUserToFirebaseDatabse(it.toString())
            }
        }
    }

    private fun saveUserToFirebaseDatabse(profileImageUrl: String) {
        val uid = auth.uid ?: ""
        val ref = database.getReference("/users/$uid")

        val user = User(uid, username_edit.text.toString(), profileImageUrl)

        ref.setValue(user).addOnSuccessListener {
            Log.d(TAG, "finally saved user to db")
        }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)
