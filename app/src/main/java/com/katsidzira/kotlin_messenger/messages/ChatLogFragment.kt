package com.katsidzira.kotlin_messenger.messages

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.katsidzira.kotlin_messenger.R
import com.katsidzira.kotlin_messenger.model.ChatFromItem
import com.katsidzira.kotlin_messenger.model.ChatMessage
import com.katsidzira.kotlin_messenger.model.ChatToItem
import com.katsidzira.kotlin_messenger.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat_log.*


class ChatLogFragment : Fragment() {
    private var listener: OnChatLogListener? = null
    private var toUser: User? = null
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenForMessages()

        send_button.setOnClickListener {
            Log.d(TAG, "Attempt to send message to : ${toUser?.username}")
            performSendMessage()
        }

        recyclerview_chat.adapter = adapter
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("user-messages/$fromId/$toId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, "chat message: ${chatMessage.text}")
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessagesFragment.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }
                recyclerview_chat.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    private fun performSendMessage() {
        val text = entermessage_edit.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

//        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val fromRef = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()


        if (fromId == null) return

        val chatMessage = ChatMessage(fromRef.key!!, text, fromId, toId!!, System.currentTimeMillis()/1000)

        fromRef.setValue(chatMessage).addOnSuccessListener {
            entermessage_edit.text.clear()
            recyclerview_chat.scrollToPosition(adapter.itemCount - 1)
            Log.d(TAG, "Saved our chat message: ${fromRef.key}")
        }

        toRef.setValue(chatMessage)

        val latestMessagesFromRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessagesFromRef.setValue(chatMessage)

        val latestMessagesToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessagesToRef.setValue(chatMessage)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnChatLogListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnChatLogListener")
        }
        arguments?.getParcelable<User>("user key").let {
            toUser = it
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnChatLogListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic fun newInstance() = ChatLogFragment()
        const val TAG = "chat log fragment"
    }

}
