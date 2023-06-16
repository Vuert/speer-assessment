package com.speer.technologies.presentation.impl.connectionprofile.viewmodel

import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConnectionProfileViewModel(
    presentationDataDelegate: PresentationDataDelegate,
    private val userRepository: UserRepository,
) : BaseViewModel(presentationDataDelegate) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userStateFlow = MutableStateFlow<User?>(null)
    val user = _userStateFlow.asStateFlow()

    fun init(user: User) {
        _userStateFlow.value = user
        fetchConnectionInfo()
    }

    fun onRefresh() {
        fetchConnectionInfo()
    }

    private fun fetchConnectionInfo() {
        launchSafe(
            finally = {
                _isLoading.value = false
            },
        ) {
            val user = requireNotNull(_userStateFlow.value)
            _isLoading.value = true
            userRepository
                .getUserByUserName(user.username)
                .collect(_userStateFlow::value::set)
        }
    }
}
