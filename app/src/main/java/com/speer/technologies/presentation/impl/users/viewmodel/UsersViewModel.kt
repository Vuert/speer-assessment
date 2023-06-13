package com.speer.technologies.presentation.impl.users.viewmodel

import com.speer.technologies.domain.users.repository.UsersRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.presentation.impl.users.model.UsersState
import com.speer.technologies.utils.extensions.common.EMPTY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

private const val DEBOUNCE_TIME_MS = 1000L

class UsersViewModel(
    presentationDataDelegate: PresentationDataDelegate,
    private val usersRepository: UsersRepository,
) : BaseViewModel(presentationDataDelegate) {

    val userName = MutableStateFlow(String.EMPTY)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _usersStateFlow = MutableStateFlow<UsersState>(UsersState.Empty)
    val users = _usersStateFlow.asStateFlow()

    init {
        scope.launch {
            userName
                .filter { it.isNotEmpty() }
                .debounce(DEBOUNCE_TIME_MS)
                .collectLatest(::findUsers)
        }
    }

    private fun findUsers(query: String) {
        launchSafe(finally = { _isLoading.value = false }) {
            _isLoading.value = true
            _usersStateFlow.value = UsersState.Loaded(usersRepository.findUsersByName(query))
        }
    }
}
