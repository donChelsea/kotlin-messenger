package com.katsidzira.kotlin_messenger


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.katsidzira.kotlin_messenger.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "login frag"
    private lateinit var callback: LoggedInListener


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logInUser()
    }


    interface LoggedInListener {
        fun onUserLoggedIn()
    }

    fun setOnLoggedInListener(callback: LoggedInListener) {
        this.callback = callback
    }

    fun logInUser() {
        val email = frag_email_edit.text
        val password = frag_password_edit.text

        login_button.setOnClickListener {
            Log.d(TAG, "email: $email, password: $password")
            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        Log.d(TAG, "successfully sign in user: $user")
                        callback.onUserLoggedIn()
                    } else {
                        Log.w(TAG, "failed: ", it.exception)
                        Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object {
        @JvmStatic fun newInstance() = LoginFragment()
    }
}
