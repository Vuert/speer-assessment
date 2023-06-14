package com.speer.technologies.utils.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.speer.technologies.R

object ImageUtil {

    fun displayAvatar(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeholder: Int = R.drawable.baseline_person,
        @DrawableRes errorPlaceholder: Int = R.drawable.baseline_person,
    ) {
        Glide
            .with(imageView)
            .load(url)
            .circleCrop()
            .error(placeholder)
            .placeholder(errorPlaceholder)
            .into(imageView)
    }
}
