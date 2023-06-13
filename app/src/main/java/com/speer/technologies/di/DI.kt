package com.speer.technologies.di

import com.speer.technologies.presentation.base.datadelegate.PresentationDataDelegate
import com.speer.technologies.presentation.impl.datadelegate.DefaultPresentationDataDelegate
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
        getPresentationModule(),
    )

    private fun getCommonModule(): Module = module {
        single<BaseLogger> { StandardLogger(LogcatLogDestination()) }
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
    }
}
