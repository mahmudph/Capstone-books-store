/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.myone.capstone_books_store.book.domain.usecase.GetBookListUseCase

class BookViewModel(getBookListUseCase: GetBookListUseCase) : ViewModel() {
    val bookList = getBookListUseCase().asLiveData()
}