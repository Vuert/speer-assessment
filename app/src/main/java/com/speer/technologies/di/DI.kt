package com.speer.technologies.di

import androidx.room.Room
import com.speer.technologies.BuildConfig
import com.speer.technologies.data.user.datasource.UserLocalDataSource
import com.speer.technologies.data.user.datasource.UserRemoteDatasource
import com.speer.technologies.data.user.repository.UserRepositoryImpl
import com.speer.technologies.datasource.common.db.AppDatabase
import com.speer.technologies.datasource.user.remote.service.UserService
import com.speer.technologies.datasource.user.remote.datasource.UserRemoteDatasourceImpl
import com.speer.technologies.datasource.user.local.datasource.UserLocalDatasourceImpl
import com.speer.technologies.datasource.common.web.GitHubApiRequestInterceptor
import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.impl.connectionprofile.viewmodel.ConnectionProfileViewModel
import com.speer.technologies.presentation.impl.connections.viewmodel.ConnectionsViewModel
import com.speer.technologies.presentation.impl.datadelegate.DefaultPresentationDataDelegate
import com.speer.technologies.presentation.impl.searchusers.viewmodel.SearchUserViewModel
import com.speer.technologies.presentation.stub.viewmodel.EmptyViewModel
import com.speer.technologies.utils.concurrent.errorhandling.CoroutineExceptionHandler
import com.speer.technologies.utils.logging.base.BaseLogger
import com.speer.technologies.utils.logging.impl.standard.StandardLogger
import com.speer.technologies.utils.logging.impl.standard.logdestination.impl.LogcatLogDestination
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DI {

    fun getModules(): List<Module> = listOf(
        getCommonModule(),
        getApiModule(),
        getDatasourceModule(),
        getDataModule(),
        getPresentationModule(),
    )

    private fun getCommonModule(): Module = module {
        single<BaseLogger> { StandardLogger(LogcatLogDestination()) }
    }

    private fun getApiModule(): Module = module {
        // Retrofit
        single {
            Retrofit
                .Builder()
                .baseUrl(BuildConfig.GITHUB_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(GitHubApiRequestInterceptor())
                        .build()
                )
                .build()
        }

        single { get<Retrofit>().create(UserService::class.java) }

        // Room
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                AppDatabase.NAME,
            ).build()
        }

        single { get<AppDatabase>().userDao() }
    }

    private fun getDatasourceModule(): Module = module {
        single<UserLocalDataSource> {
            UserLocalDatasourceImpl(userDao = get())
        }

        single<UserRemoteDatasource> {
            UserRemoteDatasourceImpl(userService = get())
        }
    }

    private fun getDataModule(): Module = module {
        single<UserRepository> {
            UserRepositoryImpl(
                userRemoteDatasource = get(),
                userLocalDataSource = get(),
            )
        }
    }

    private fun getPresentationModule(): Module = module {
        single<PresentationDataDelegate> {
            val logger = get<BaseLogger>()
            DefaultPresentationDataDelegate(
                logger = logger,
                workDispatcher = Dispatchers.Default,
                unexpectedExceptionHandler = CoroutineExceptionHandler(logger),
            )
        }
        viewModel { EmptyViewModel(presentationDataDelegate = get()) }

        viewModel {
            SearchUserViewModel(
                presentationDataDelegate = get(),
                userRepository = get(),
            )
        }

        viewModel {
            ConnectionsViewModel(
                presentationDataDelegate = get(),
                userRepository = get(),
            )
        }

        viewModel {
            ConnectionProfileViewModel(
                presentationDataDelegate = get(),
                userRepository = get(),
            )
        }
    }
}
