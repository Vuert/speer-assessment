package com.speer.technologies.utils.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.speer.technologies.R

object ImageUtil {

    fun displayAvatar(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeholder: Int = R.drawable.ic_user_placeholder,
        @DrawableRes errorPlaceholder: Int = R.drawable.ic_user_placeholder,
    ) {
        Glide
            .with(imageView)
            .load(url)
            .circleCrop()
            .transition(withCrossFade())
            .error(placeholder)
            .placeholder(errorPlaceholder)
            .into(imageView)
    }
}
