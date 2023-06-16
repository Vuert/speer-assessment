package com.speer.technologies.presentation.impl.datadelegate

import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.utils.logging.base.BaseLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * @see [PresentationDataDelegate]
 */
class DefaultPresentationDataDelegate(
    override val logger: BaseLogger,
    override val workDispatcher: CoroutineDispatcher,
    override val unexpectedExceptionHandler: CoroutineExceptionHandler,
) : PresentationDataDelegate
