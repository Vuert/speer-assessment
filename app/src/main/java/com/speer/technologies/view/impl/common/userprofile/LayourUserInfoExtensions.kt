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
    val resources = root.context.resources

    root.visibility = View.VISIBLE

    ImageUtil.displayAvatar(userAvatarImg, user.avatarUrl)
    nameTv.setTextOrGone(user.additionalInfo?.name)
    userNameTv.text = user.username
    collapsingToolbar.title = user.username
    descriptionTv.text = user.additionalInfo?.description
        ?: resources.getString(R.string.no_description)

    user.additionalInfo?.followersCount.let {
        followersTv.text = resources.getString(R.string.patter_followers, it ?: 0)

        followersTv.setOnClickListener(null)
        if (it != null && it > 0 && onFollowersClickListener != null) {
            followersTv.setOnClickListener {
                onFollowersClickListener.invoke(user)
            }
        }
    }

    user.additionalInfo?.followingCount.let {
        followingsTv.text = resources.getString(R.string.patter_following, it ?: 0)

        followingsTv.setOnClickListener(null)
        if (it != null && it > 0 && onFollowingClickListener != null) {
            followingsTv.setOnClickListener {
                onFollowingClickListener.invoke(user)
            }
        }
    }
}

fun LayoutUserInfoBinding.hide() {
    followersTv.setOnClickListener(null)
    followingsTv.setOnClickListener(null)
    root.visibility = View.GONE
}

private fun TextView.setTextOrGone(text: String?) {
    if (text.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        this.text = text
    }
}
