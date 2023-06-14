package com.speer.technologies.presentation.impl.connectionprofile.viewmodel

import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.utils.common.Wrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConnectionProfileViewModel(
    presentationDataDelegate: PresentationDataDelegate,
    private val userRepository: UserRepository,
) : BaseViewModel(presentationDataDelegate) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userStateFlow = MutableStateFlow<Wrapper<User?>>(Wrapper(null))
    val user = _userStateFlow.asStateFlow()

    fun init(user: User) {
        _userStateFlow.value = Wrapper(user)
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
            val user = requireNotNull(_userStateFlow.value.value)
            _isLoading.value = true
            _userStateFlow.value = Wrapper(userRepository.getUserByUserName(user.username))
        }
    }
}
