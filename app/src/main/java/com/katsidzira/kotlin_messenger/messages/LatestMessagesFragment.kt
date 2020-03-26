package com.katsidzira.kotlin_messenger.messages

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.katsidzira.kotlin_messenger.R
import kotlinx.android.synthetic.main.fragment_latest_messages.*

class LatestMessagesFragment : Fragment() {
    private var listener: onLatestMessagesListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_latest_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        test_button.setOnClickListener {
            listener!!.chooseUserToMessage()
        }
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
        @JvmStatic fun newInstance() =
            LatestMessagesFragment()
    }


}
