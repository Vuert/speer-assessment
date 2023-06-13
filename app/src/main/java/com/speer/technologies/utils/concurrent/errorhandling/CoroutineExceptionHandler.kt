package com.speer.technologies.utils.concurrent.errorhandling

import com.speer.technologies.utils.logging.base.BaseLogger
import kotlinx.coroutines.CoroutineExceptionHandler

fun CoroutineExceptionHandler(logger: BaseLogger): CoroutineExceptionHandler =
    CoroutineExceptionHandler { _, throwable ->
        logger.error("CoroutineExceptionHandler", "Unexpected error", throwable)
    }
