package com.speer.technologies.utils.extensions.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.speer.technologies.utils.extensions.lifecycle.launchOnAnyLifecycleEvent
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecycleCreate
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecycleDestroy
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecycleEvent
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecyclePause
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecycleResume
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecycleStart
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecycleStop
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

inline fun LifecycleOwner.repeatOnStarted(
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = lifecycle.repeatOnStarted(block)

inline fun LifecycleOwner.launchOnLifecycleCreate(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleCreate(block)

inline fun LifecycleOwner.launchOnLifecycleStart(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStart(block)

inline fun LifecycleOwner.launchOnLifecycleResume(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleResume(block)

inline fun LifecycleOwner.launchOnLifecyclePause(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecyclePause(block)

inline fun LifecycleOwner.launchOnLifecycleStop(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStop(block)

inline fun LifecycleOwner.launchOnLifecycleDestroy(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleDestroy(block)

inline fun LifecycleOwner.launchOnAnyLifecycleEvent(
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnAnyLifecycleEvent(block)

inline fun LifecycleOwner.launchOnLifecycleEvent(
    event: Lifecycle.Event,
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnLifecycleEvent(event, block)
