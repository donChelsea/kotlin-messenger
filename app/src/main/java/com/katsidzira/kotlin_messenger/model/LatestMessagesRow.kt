package com.katsidzira.kotlin_messenger.model

import com.katsidzira.kotlin_messenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.latestmessages_username_text
        viewHolder.itemView.latestmessages_preview_text.text = chatMessage.text
    }

}

val latestMessagesMap = HashMap<String, ChatMessage>()
