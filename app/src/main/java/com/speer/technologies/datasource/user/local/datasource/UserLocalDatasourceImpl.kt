package com.speer.technologies.datasource.user.local.datasource

import com.speer.technologies.data.user.datasource.UserLocalDataSource
import com.speer.technologies.datasource.user.local.dao.UserDao
import com.speer.technologies.datasource.user.local.mapper.UserDboToUserMapper
import com.speer.technologies.datasource.user.local.mapper.UserToUserDboMapper
import com.speer.technologies.domain.user.model.User

class UserLocalDatasourceImpl(
    private val userDao: UserDao,
) : UserLocalDataSource {

    override suspend fun getUserByUserName(username: String): User? {
        val dboUser = userDao.getByUsername(username)
        return dboUser?.let(UserDboToUserMapper()::map)
    }

    override suspend fun addOrUpdateUser(user: User) {
        userDao.upsert(UserToUserDboMapper().map(user))
    }

    override suspend fun removeUser(user: User) {
        userDao.deleteById(user.id)
    }
}
