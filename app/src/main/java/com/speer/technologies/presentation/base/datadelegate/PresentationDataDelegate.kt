package com.speer.technologies.presentation.base.datadelegate

import com.speer.technologies.utils.logging.base.BaseLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Accumulates common dependencies for all [BaseViewModel]s
 *
 * @property workDispatcher default dispatcher to do computation work
 * @property unexpectedExceptionHandler - basically, handles unexpected errors
 * that might occur during [BaseViewModel.launchSafe] execution
 */
interface PresentationDataDelegate {

    val logger: BaseLogger

    val workDispatcher: CoroutineDispatcher

    val unexpectedExceptionHandler: CoroutineExceptionHandler
}
