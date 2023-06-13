package com.speer.technologies.presentation.stub.viewmodel

import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel

/**
 * Stub ViewModel, used for screens where ViewModel with any logic is not needed
 */
class EmptyViewModel(presentationDataDelegate: PresentationDataDelegate) :
    BaseViewModel(presentationDataDelegate)
