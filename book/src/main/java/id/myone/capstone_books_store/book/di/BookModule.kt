/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.di

import id.myone.capstone_books_store.book.presentation.book.BookViewModel
import id.myone.capstone_books_store.book.presentation.detailBook.DetailBookViewModel
import id.myone.capstone_books_store.book.domain.usecase.*
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val bookUseCaseModel = module {
    single { GetBookListUseCase(get()) }
    single { GetDetailBookUseCase(get()) }
    single { SearchBookUseCase(get()) }
    single { GetBookFavoriteStatusUseCase(get()) }
    single { SetFavoriteBookUseCase(get()) }
}

val bookViewModelModule = module {
    single { BookViewModel(get()) }
    single { DetailBookViewModel(get(), get(), get()) }
}


fun provideModuleDependencies() {
    loadKoinModules(
        listOf(
            bookViewModelModule,
            bookUseCaseModel
        )
    )
}
