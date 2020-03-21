package com.katsidzira.kotlin_messenger

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_new_message.*

class NewMessageFragment : Fragment() {
    private var listener: OnNewMessageListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(UserItem())

        recyclerview_new_message.adapter = adapter

        // select user to start new message convo
        // startMessageConvo()
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
        fun sendMessage()
    }

    companion object {
        @JvmStatic fun newInstance() = NewMessageFragment()
    }

}

class UserItem: Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_list_view
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

}
