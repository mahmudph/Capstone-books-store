/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.di

import id.myone.book.domain.usecase.GetBookListUseCase
import id.myone.book.domain.usecase.GetDetailBookUseCase
import id.myone.book.domain.usecase.SearchBookUseCase
import id.myone.book.presentation.book.BookViewModel
import id.myone.book.presentation.detailBook.DetailBookViewModel
import id.myone.book.presentation.search.SearchViewModel
import org.koin.dsl.module

val bookUseCaseModel = module {
    single { GetBookListUseCase(get()) }
    single { GetDetailBookUseCase(get()) }
    single { SearchBookUseCase(get()) }
}

val bookViewModelModule = module {
    single { BookViewModel(get()) }
    single { DetailBookViewModel(get()) }
    single { SearchViewModel(get()) }
}

