package com.speer.technologies.view.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.speer.technologies.R
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel

/**
 * Accumulates common methods for fragment and activity
 */
interface BaseScreen<VB : ViewBinding, VM : BaseViewModel> {

    /**
     * Invoked after binding creation
     */
    fun onViewBound(binding: VB, savedInstanceState: Bundle?) {
        // None
    }

    /**
     * Handles errors that come from a ViewModel
     */
    fun handleError(binding: VB, throwable: Throwable) {
        Snackbar.make(binding.root, R.string.error_occurred, Snackbar.LENGTH_LONG).show()
    }
}
