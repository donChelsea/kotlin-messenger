package com.katsidzira.kotlin_messenger.messages

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.katsidzira.kotlin_messenger.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_chat_log.*


class ChatLogFragment : Fragment() {
    private var listener: OnChatLogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatItem())
        adapter.add(ChatItem())
        adapter.add(ChatItem())
        adapter.add(ChatItem())

        recyclerview_chat.adapter = adapter
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnChatLogListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnChatLogListener")
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
    }

}

class ChatItem: Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}
