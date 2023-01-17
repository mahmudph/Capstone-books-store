/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.favorite.di

import id.myone.favorite.domain.usecase.GetBookFavoriteStatusUseCase
import id.myone.favorite.domain.usecase.GetFavoriteBookUseCase
import id.myone.favorite.domain.usecase.SetFavoriteBookUseCase
import id.myone.favorite.presentation.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val favoriteViewModelModule = module {
    viewModel { FavoriteViewModel(get()) }
}

val favoriteUseCaseModule = module {
    single { GetFavoriteBookUseCase(get()) }
    single { GetBookFavoriteStatusUseCase(get()) }
    single { SetFavoriteBookUseCase(get()) }
}