/**
 * Created by Mahmud on 22/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.search_book.di

import id.myone.capstone_books_store.search_book.domain.usecase.SearchBookUseCase
import id.myone.capstone_books_store.search_book.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


val searchModule = module {
    single { SearchBookUseCase(get()) }
    viewModel { SearchViewModel(get()) }
}


fun provideModuleDependencies() {
    loadKoinModules(
        listOf(
            searchModule,
        )
    )
}
