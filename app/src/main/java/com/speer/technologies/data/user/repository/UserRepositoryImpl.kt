package com.speer.technologies.data.user.repository

import com.speer.technologies.data.user.datasource.UserLocalDataSource
import com.speer.technologies.data.user.datasource.UserRemoteDatasource
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userRemoteDatasource: UserRemoteDatasource,
    private val userLocalDataSource: UserLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {

    override suspend fun getUserByUserName(username: String): Flow<User?> =
        withContext(ioDispatcher) {
            flow {
                val localUser = userLocalDataSource.getUserByUserName(username)
                localUser?.let { emit(it) }

                val remoteUser = userRemoteDatasource.getUserByUserName(username)
                if (remoteUser == null || remoteUser != localUser) {
                    emit(remoteUser)
                }

                // User is deleted on server, remove him from cache
                if (remoteUser == null && localUser != null) {
                    userLocalDataSource.removeUser(localUser)
                }

                // Update cache
                if (remoteUser != null && remoteUser != localUser) {
                    userLocalDataSource.addOrUpdateUser(remoteUser)
                }
            }
        }

    override suspend fun getFollowers(user: User, page: Int, pageSize: Int): List<User> =
        withContext(ioDispatcher) {
            userRemoteDatasource.getFollowers(user, page, pageSize)
        }

    override suspend fun getFollowing(user: User, page: Int, pageSize: Int): List<User> =
        withContext(ioDispatcher) {
            userRemoteDatasource.getFollowing(user, page, pageSize)
        }
}
