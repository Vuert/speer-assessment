package com.speer.technologies.utils.view

import android.content.res.Resources
import com.speer.technologies.R
import com.speer.technologies.domain.common.exception.NoNetworkException

class ThrowableToErrorMessageMapper(
    private val resources: Resources,
) {

    fun map(throwable: Throwable): String =
        when (throwable) {
            is NoNetworkException -> resources.getString(R.string.no_internet)
            else -> resources.getString(R.string.error_occurred)
        }
}
