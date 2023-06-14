package com.speer.technologies.datasource.user.util

import com.speer.technologies.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_ACCEPT = "Accept"
private const val HEADER_API_VERSION = "X-GitHub-Api-Version"
private const val HEADER_AUTHORIZATION = "Authorization"

class GitHubApiRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader(HEADER_ACCEPT, BuildConfig.GITHUB_API_ACCEPT)
            .addHeader(HEADER_API_VERSION, BuildConfig.GITHUB_API_VERSION)
            .addHeader(HEADER_AUTHORIZATION, BuildConfig.GITHUB_API_KEY)
            .build()

        return chain.proceed(newRequest)
    }
}
