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
        circleCrop: Boolean = false,
        @DrawableRes placeholder: Int = R.drawable.baseline_person,
        @DrawableRes errorPlaceholder: Int = R.drawable.baseline_person,
    ) {
        Glide
            .with(imageView)
            .load(url)
            .run { if (circleCrop) circleCrop() else this }
            .transition(withCrossFade())
            .error(placeholder)
            .placeholder(errorPlaceholder)
            .into(imageView)
    }
}
