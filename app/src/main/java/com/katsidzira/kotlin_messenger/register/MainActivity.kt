package com.katsidzira.kotlin_messenger.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.katsidzira.kotlin_messenger.LatestMessagesFragment
import com.katsidzira.kotlin_messenger.R
import com.katsidzira.kotlin_messenger.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(),
    LoginFragment.onLoggedInListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private val TAG: String = "main"
    private var selectedPhotoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        register_button.setOnClickListener {
            registerNewUser()
            // move to profile fragment
        }

       select_photo_button.setOnClickListener {
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

        select_photo_image.setImageBitmap(bitmap)

        select_photo_button.alpha = 0f
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

        val user = User(
            uid,
            email_edit.text.toString(),
            profileImageUrl
        )

        ref.setValue(user).addOnSuccessListener {
            Log.d(TAG, "finally saved user to db")
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is LoginFragment) {
            fragment.setOnLoggedInListener(this)
        }
    }

    override fun onUserLoggedIn() {
        Log.d(TAG, "going to profile")
        val lastestMessagesFragment =
            LatestMessagesFragment.newInstance()
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, lastestMessagesFragment)
        transaction.commit()
    }

    override fun registerNewUser() {
        val name = username_edit.text.toString()
        val email = email_edit.text.toString()
        val password = password_edit.text.toString()

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

}

class User(val uid: String, val username: String, val profileImageUrl: String)
