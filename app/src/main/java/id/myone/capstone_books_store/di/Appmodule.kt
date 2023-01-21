/**
 * Created by Mahmud on 21/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.di

import id.myone.capstone_books_store.ui.splashscreen.SplashscreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appViewModelModule = module {
    viewModel { SplashscreenViewModel(get()) }
}