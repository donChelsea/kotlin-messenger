package com.katsidzira.kotlin_messenger.messages

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.katsidzira.kotlin_messenger.R
import com.katsidzira.kotlin_messenger.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_new_message.*
import kotlinx.android.synthetic.main.user_list_view.view.*

class NewMessageFragment : Fragment() {
    private var listener: OnNewMessageListener? = null
    private val TAG = "new message frag"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach {
                    Log.d(TAG, "new message: $it")
                    val user = it.getValue(User::class.java)
                    if (user != null)
                    adapter.add(
                        UserItem(
                            user
                        )
                    )
                }

                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    listener?.startMessageConvo(userItem)
                }

                recyclerview_new_message.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun startMessageConvo() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewMessageListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnNewMessageListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnNewMessageListener {
        fun startMessageConvo(userItem: UserItem)
    }

    companion object {
        @JvmStatic fun newInstance() = NewMessageFragment()
    }

}

class UserItem(val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_list_view
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.newmessage_username.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.newmessage_image)
    }

}
