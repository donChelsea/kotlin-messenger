package com.katsidzira.kotlin_messenger.messages

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.katsidzira.kotlin_messenger.R
import com.katsidzira.kotlin_messenger.model.User
import kotlinx.android.synthetic.main.fragment_latest_messages.*

class LatestMessagesFragment : Fragment() {
    private var listener: onLatestMessagesListener? = null
    val TAG = "latest messages"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_latest_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchCurrentUser()

        test_button.setOnClickListener {
            listener!!.chooseUserToMessage()
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d(TAG, "current user: ${currentUser!!.username}")
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is onLatestMessagesListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement onLatestMessagesListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface onLatestMessagesListener {
        fun chooseUserToMessage()
    }

    companion object {
        @JvmStatic fun newInstance() = LatestMessagesFragment()
        var currentUser: User? = null
    }


}
