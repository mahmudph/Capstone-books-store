/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.favorite.di


import id.myone.capstone_books_store.favorite.domain.usecase.GetFavoriteBookUseCase
import id.myone.capstone_books_store.favorite.presentation.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


val favoriteViewModelModule = module {
    viewModel { FavoriteViewModel(get()) }
}

val favoriteUseCaseModule = module {
    single { GetFavoriteBookUseCase(get()) }
}

fun provideModuleDependencies() {
    loadKoinModules(
        listOf(
            favoriteUseCaseModule,
            favoriteViewModelModule
        )
    )
}