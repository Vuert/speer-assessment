package com.speer.technologies.presentation.impl.connections.viewmodel

import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.presentation.impl.connections.model.FetchMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ConnectionsViewModel(
    presentationDataDelegate: PresentationDataDelegate,
    private val userRepository: UserRepository,
) : BaseViewModel(presentationDataDelegate) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _fetchModeStateFlow = MutableStateFlow(FetchMode.FOLLOWERS)
    val fetchMode = _fetchModeStateFlow.asStateFlow()

    private val _userStateFlow = MutableStateFlow(UserWrapper())
    val user = _userStateFlow.asSharedFlow()

    private val _connectionsStateFlow = MutableStateFlow<List<User>>(listOf())
    val connections = _connectionsStateFlow.asStateFlow()

    fun init(user: User, fetchMode: FetchMode) {
        _userStateFlow.value = UserWrapper(user)
        _fetchModeStateFlow.value = fetchMode
        fetchConnections()
    }

    fun onRefresh() {
        fetchConnections()
    }

    private fun fetchConnections() {
        launchSafe(
            finally = {
                _isLoading.value = false
            },
        ) {
            val user = requireNotNull(_userStateFlow.value.user)
            _isLoading.value = true
            _connectionsStateFlow.value = getFetchConnectionsFun().invoke(user)
        }
    }

    private fun getFetchConnectionsFun(): suspend (User) -> List<User> =
        when (fetchMode.value) {
            FetchMode.FOLLOWING -> userRepository::getFollowing
            FetchMode.FOLLOWERS -> userRepository::getFollowers
        }

    class UserWrapper(val user: User? = null)
}
