package com.speer.technologies.presentation.impl.searchusers.viewmodel

import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.presentation.impl.searchusers.model.UserState
import com.speer.technologies.utils.extensions.common.EMPTY
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val DEBOUNCE_TIME_MS = 1000L

@OptIn(FlowPreview::class)
class SearchUserViewModel(
    presentationDataDelegate: PresentationDataDelegate,
    private val userRepository: UserRepository,
) : BaseViewModel(presentationDataDelegate) {

    val userName = MutableStateFlow(String.EMPTY)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userStateFlow = MutableStateFlow<UserState>(UserState.Empty)
    val user = _userStateFlow.asStateFlow()

    private var searchJob: Job? = null

    init {
        scope.launch {
            userName
                .debounce(DEBOUNCE_TIME_MS)
                .onEach {
                    searchJob?.cancel()
                    searchJob = null
                }
                .filter { it.isNotEmpty() }
                .collectLatest(::getUser)
        }
    }

    private fun getUser(query: String) {
        searchJob = launchSafe(
            finally = {
                _isLoading.value = false
            },
        ) {
            _isLoading.value = true
            userRepository
                .getUserByUserName(query)
                .map {
                    if (it != null) {
                        UserState.Found(it)
                    } else {
                        UserState.NotFound
                    }
                }
                .collect(_userStateFlow::value::set)
        }
    }
}
