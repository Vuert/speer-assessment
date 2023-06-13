package com.speer.technologies.presentation.impl.users.model

import com.speer.technologies.domain.users.model.User

sealed class UsersState {

    object Empty : UsersState()
    class Loaded(val list: List<User>) : UsersState()
}
