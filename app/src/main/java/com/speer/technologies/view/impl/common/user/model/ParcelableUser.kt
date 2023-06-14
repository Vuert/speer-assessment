package com.speer.technologies.view.impl.common.user.model

import android.os.Parcelable
import com.speer.technologies.domain.user.model.User
import kotlinx.parcelize.Parcelize

/**
 * @see [User]
 */
@Parcelize
data class ParcelableUser(
    val id: String,
    val avatarUrl: String?,
    val username: String,
    val name: String?,
    val description: String?,
    val followersCount: Int?,
    val followingCount: Int?,
) : Parcelable
