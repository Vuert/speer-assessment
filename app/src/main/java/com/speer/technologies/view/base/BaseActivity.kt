package com.speer.technologies.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.utils.extensions.common.unsafeLazy
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.utils.extensions.view.createViewModel
import com.speer.technologies.utils.extensions.view.inflateBinding

/**
 * Base class of all activities
 */
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> :
    AppCompatActivity(),
    BaseScreen<VB, VM> {

    protected val binding: VB by unsafeLazy { inflateBinding(layoutInflater) }

    protected val viewModel: VM by unsafeLazy { createViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.let {
            setContentView(it.root)
            listenViewModelErrors(it)
            onViewBound(it, savedInstanceState)
        }
    }

    private fun listenViewModelErrors(binding: VB) {
        repeatOnStarted {
            viewModel.errorFlow.collect { handleError(binding, it) }
        }
    }
}
