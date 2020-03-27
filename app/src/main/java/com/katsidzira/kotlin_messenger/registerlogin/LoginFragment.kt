package com.katsidzira.kotlin_messenger.registerlogin


import android.content.Context
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
import com.katsidzira.kotlin_messenger.R
import com.katsidzira.kotlin_messenger.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "login frag"
    private var loggedInListener: onLoggedInListener? = null


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_login, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_text.setOnClickListener {
            loggedInListener!!.registerNewUser()
        }

        logInUser()
    }


    interface onLoggedInListener {
        fun onUserLoggedIn()

        fun registerNewUser()
    }

    fun setOnLoggedInListener(callback: onLoggedInListener) {
        this.loggedInListener = callback
    }

    fun logInUser() {
        val email = frag_email_edit.text
        val password = frag_password_edit.text

        login_button.setOnClickListener {
            if (email.isEmpty() or password.isEmpty()) {
                Toast.makeText(context, "Missing or incorrect fields", Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "email: $email, password: $password")
            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        Log.d(TAG, "successfully sign in user: $user")
                        loggedInListener!!.onUserLoggedIn()
                    } else {
                        Log.w(TAG, "failed: ", it.exception)
                        Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is onLoggedInListener) {
            loggedInListener = context
        } else {
            throw RuntimeException("$context must implement onLoggedInListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        loggedInListener = null
    }

    companion object {
        @JvmStatic fun newInstance() =
            LoginFragment()
    }
}
