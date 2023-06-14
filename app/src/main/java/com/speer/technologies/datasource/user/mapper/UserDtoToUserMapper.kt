package com.speer.technologies.datasource.user.mapper

import com.speer.technologies.datasource.user.model.UserDto
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.model.UserInfo

class UserDtoToUserMapper {

    fun map(userDto: UserDto): User = userDto.run {

        val additionalInfo = if (
            name.isNullOrEmpty() &&
            bio.isNullOrEmpty() &&
            followers == null &&
            following == null
        ) {
            null
        } else {
            UserInfo(
                name = name,
                description = bio,
                followersCount = followers,
                followingCount = following,
            )
        }

        User(
            id = id.toString(),
            username = login,
            avatarUrl = avatarUrl,
            additionalInfo = additionalInfo,
        )
    }
}
