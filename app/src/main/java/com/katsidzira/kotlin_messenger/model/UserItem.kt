package com.katsidzira.kotlin_messenger.model

import com.katsidzira.kotlin_messenger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_list_view.view.*

class UserItem(val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_list_view
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.newmessage_username.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.newmessage_image)
    }

}