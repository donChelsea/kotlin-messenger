package com.katsidzira.kotlin_messenger.model

import com.katsidzira.kotlin_messenger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatFromItem(val text: String, val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_from_text.text = text
        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(viewHolder.itemView.chat_from_image)
    }
}

class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_to_text.text = text
        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(viewHolder.itemView.chat_to_image)
    }
}