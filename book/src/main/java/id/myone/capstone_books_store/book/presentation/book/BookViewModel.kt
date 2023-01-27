/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.presentation.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.myone.capstone_books_store.book.domain.usecase.GetBookListUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result

class BookViewModel(getBookListUseCase: GetBookListUseCase) : ViewModel() {
    private val _bookList = getBookListUseCase().asLiveData()
    val bookListData : LiveData<Result<List<Book>>> = _bookList
}