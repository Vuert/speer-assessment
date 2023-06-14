package com.speer.technologies.view.impl.fragment.connections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.speer.technologies.databinding.LoadingIndicatorListItemBinding

class LoadingIndicatorAdapter :
    RecyclerView.Adapter<LoadingIndicatorAdapter.LoadingIndicatorViewHolder>() {

    var isLoading = false
        set(value) {
            if (value != field) {
                if (value) {
                    notifyItemInserted(0)
                } else {
                    notifyItemRemoved(0)
                }
            }
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingIndicatorViewHolder =
        LoadingIndicatorViewHolder(
            LoadingIndicatorListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    override fun getItemCount(): Int = if (isLoading) 1 else 0

    override fun onBindViewHolder(holder: LoadingIndicatorViewHolder, position: Int) {}

    inner class LoadingIndicatorViewHolder(
        binding: LoadingIndicatorListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
