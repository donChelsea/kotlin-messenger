package com.katsidzira.kotlin_messenger

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class LatestMessagesFragment : Fragment() {
    private var listener: onLatestMessagesListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_latest_messages, container, false)
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
        fun createNewMessage()

        fun viewChatLog()
    }

    companion object {
        @JvmStatic fun newInstance() = LatestMessagesFragment()
    }


}
