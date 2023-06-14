package com.speer.technologies.presentation.impl.users.model

import com.speer.technologies.domain.user.model.User

sealed class UsersState {

    object Empty : UsersState()
    object NotFound : UsersState()
    class Found(val user: User) : UsersState()
}
