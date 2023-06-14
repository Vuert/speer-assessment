package com.speer.technologies.presentation.impl.searchUsers.viewmodel

import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.presentation.impl.searchUsers.model.UserState
import com.speer.technologies.utils.extensions.common.EMPTY
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

private const val DEBOUNCE_TIME_MS = 1000L

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
                .filter { it.isNotEmpty() }
                .debounce(DEBOUNCE_TIME_MS)
                .collectLatest(::getUser)
        }
    }

    private fun getUser(query: String) {
        searchJob?.cancel()
        searchJob = launchSafe(
            finally = {
                _isLoading.value = false
            },
        ) {
            _isLoading.value = true
            _userStateFlow.value = userRepository
                .getUserByUserName(query)
                ?.let(UserState::Found)
                ?: UserState.NotFound
        }
    }
}
