package com.speer.technologies.utils.view

import androidx.recyclerview.widget.DiffUtil
import java.util.Objects

class DefaultItemCallback<T : Any>(
    private val areItemsTheSame: (T, T) -> Boolean = Objects::equals,
    private val areContentsTheSame: (T, T) -> Boolean = Objects::equals,
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame.invoke(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame.invoke(oldItem, newItem)
}
