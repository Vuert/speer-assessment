package com.speer.technologies.presentation.impl.searchusers.model

import com.speer.technologies.domain.user.model.User

sealed class UserState {

    object Empty : UserState()
    object NotFound : UserState()
    class Found(val user: User) : UserState()
}
