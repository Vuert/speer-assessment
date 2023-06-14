package com.speer.technologies.view.impl.common.userprofile

import android.view.View
import android.widget.TextView
import com.speer.technologies.R
import com.speer.technologies.databinding.LayoutUserInfoBinding
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.utils.view.ImageUtil

fun LayoutUserInfoBinding.show(
    user: User,
    onFollowersClickListener: ((User) -> Unit)? = null,
    onFollowingClickListener: ((User) -> Unit)? = null,
) {
    root.visibility = View.VISIBLE
    ImageUtil.displayAvatar(userAvatarImg, user.avatarUrl)
    nameTv.setTextOrGone(user.additionalInfo?.name)
    toolbar.title = user.username
    descriptionTv.setTextOrGone(user.additionalInfo?.description)

    user.additionalInfo?.followersCount.let { followersCount ->
        followersTv.setTextOrGone(
            followersCount?.let { root.context.getString(R.string.patter_followers, it) }
        )

        if (followersCount != null && followersCount > 0 && onFollowersClickListener != null) {
            followersTv.setOnClickListener {
                onFollowersClickListener.invoke(user)
            }
        }
    }

    user.additionalInfo?.followingCount.let { followingCount ->
        followingsTv.setTextOrGone(
            followingCount?.let { root.context.getString(R.string.patter_following, it) }
        )

        if (followingCount != null && followingCount > 0 && onFollowingClickListener != null) {
            followingsTv.setOnClickListener {
                onFollowingClickListener.invoke(user)
            }
        }
    }
}

fun LayoutUserInfoBinding.hide() {
    root.visibility = View.GONE
    followersTv.setOnClickListener(null)
    followingsTv.setOnClickListener(null)
}

private fun TextView.setTextOrGone(text: String?) {
    if (text.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        this.text = text
    }
}
