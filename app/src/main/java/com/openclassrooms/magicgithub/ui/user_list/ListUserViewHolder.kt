package com.openclassrooms.magicgithub.ui.user_list

import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.magicgithub.R
import com.openclassrooms.magicgithub.databinding.ItemListUserBinding
import com.openclassrooms.magicgithub.model.User

class ListUserViewHolder(private val binding: ItemListUserBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, callback: UserListAdapter.Listener) {
        binding.apply {
            // Set background based on active state
            root.setBackgroundColor(if (user.isActive)
                Color.WHITE else
                Color.parseColor("#FFCDD2")  // Light red
            )

            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(itemListUserAvatar)

            itemListUserUsername.text = user.login
            itemListUserDeleteButton.setOnClickListener { callback.onClickDelete(user) }
        }
    }
}