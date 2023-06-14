package com.speer.technologies.datasource.user.mapper

import com.speer.technologies.datasource.user.model.UserDto
import com.speer.technologies.domain.user.model.User

class UserDtoToUserMapper {

    fun map(userDto: UserDto): User = userDto.run {
        User(
            id = id.toString(),
            username = login,
            name = name,
            avatarUrl = avatarUrl,
            description = bio,
            followersCount = followers,
            followingCount = following,
        )
    }
}
