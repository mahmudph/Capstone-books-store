/**
 * Created by Mahmud on 21/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.onboarding.di

import id.myone.capstone_books_store.onboarding.presenter.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


val onBoardingViewModule = module {
    viewModel { OnBoardingViewModel(get()) }
}

fun provideModuleDependencies() {
    loadKoinModules(
        listOf(
            onBoardingViewModule
        )
    )
}