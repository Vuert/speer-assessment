package com.speer.technologies.di

import com.speer.technologies.data.user.datasource.UserRemoteDatasource
import com.speer.technologies.data.user.repository.UserRepositoryImpl
import com.speer.technologies.datasource.users.datasource.UserRemoteDatasourceImpl
import com.speer.technologies.domain.user.repository.UserRepository
import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.impl.datadelegate.DefaultPresentationDataDelegate
import com.speer.technologies.presentation.impl.users.viewmodel.UsersViewModel
import com.speer.technologies.presentation.stub.viewmodel.EmptyViewModel
import com.speer.technologies.utils.concurrent.errorhandling.CoroutineExceptionHandler
import com.speer.technologies.utils.logging.base.BaseLogger
import com.speer.technologies.utils.logging.impl.standard.StandardLogger
import com.speer.technologies.utils.logging.impl.standard.logdestination.impl.LogcatLogDestination
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object DI {

    fun getModules(): List<Module> = listOf(
        getCommonModule(),
        getDatasourceModule(),
        getDataModule(),
        getPresentationModule(),
    )

    private fun getCommonModule(): Module = module {
        single<BaseLogger> { StandardLogger(LogcatLogDestination()) }
    }

    private fun getDatasourceModule(): Module = module {
        single<UserRemoteDatasource> { UserRemoteDatasourceImpl() }
    }

    private fun getDataModule(): Module = module {
        single<UserRepository> {
            UserRepositoryImpl(userRemoteDatasource = get())
        }
    }

    private fun getPresentationModule(): Module = module {
        single<PresentationDataDelegate> {
            val logger = get<BaseLogger>()
            DefaultPresentationDataDelegate(
                logger = logger,
                unexpectedExceptionHandler = CoroutineExceptionHandler(logger),
            )
        }
        viewModel { EmptyViewModel(presentationDataDelegate = get()) }

        viewModel {
            UsersViewModel(
                presentationDataDelegate = get(),
                userRepository = get(),
            )
        }
    }
}
