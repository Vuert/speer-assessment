package com.speer.technologies.presentation.impl.connections.viewmodel

import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.presentation.impl.connections.model.FetchMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val PAGE_SIZE = 30

class ConnectionsViewModel(
    presentationDataDelegate: PresentationDataDelegate,
    private val userRepository: UserRepository,
) : BaseViewModel(presentationDataDelegate) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _noMoreItems = MutableStateFlow(false)
    val noMoreItems = _noMoreItems.asStateFlow()

    private val _fetchModeStateFlow = MutableStateFlow(FetchMode.FOLLOWERS)
    val fetchMode = _fetchModeStateFlow.asStateFlow()

    private val _userStateFlow = MutableStateFlow<User?>(null)
    val user = _userStateFlow.asSharedFlow()

    private val _connectionsStateFlow = MutableStateFlow<List<User>>(listOf())
    val connections: StateFlow<List<User>> = _connectionsStateFlow.asStateFlow()

    fun init(user: User, fetchMode: FetchMode) {
        _userStateFlow.value = user
        _fetchModeStateFlow.value = fetchMode
        loadNextPage()
    }

    fun onRefresh() {
        refreshAll()
    }

    fun onListBottomReached() {
        loadNextPage()
    }

    private fun loadNextPage() {
        loadPage(page = getNextPage()) { newPage ->
            _connectionsStateFlow.update { currentList ->
                currentList.toMutableList().also { it.addAll(newPage) }
            }
        }
    }

    private fun refreshAll() {
        loadPage(page = 0, _connectionsStateFlow::value::set)
    }

    private inline fun loadPage(page: Int, crossinline block: (List<User>) -> Unit) {
        launchSafe(
            finally = {
                _isLoading.value = false
            },
        ) {
            _isLoading.value = true

            val user = requireNotNull(_userStateFlow.value)

            val newPage = getFetchConnectionsFun().invoke(user, page, PAGE_SIZE)

            _noMoreItems.value = newPage.size < PAGE_SIZE

            block.invoke(newPage)
        }
    }


    private fun getNextPage(): Int =
        ceil(_connectionsStateFlow.value.size.toDouble() / PAGE_SIZE).roundToInt() + 1

    private fun getFetchConnectionsFun(): suspend (User, Int, Int) -> List<User> =
        when (fetchMode.value) {
            FetchMode.FOLLOWING -> userRepository::getFollowing
            FetchMode.FOLLOWERS -> userRepository::getFollowers
        }
}
