/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.presentation.detailBook

import androidx.lifecycle.*
import id.myone.capstone_books_store.book.domain.usecase.GetBookFavoriteStatusUseCase
import id.myone.capstone_books_store.book.domain.usecase.GetDetailBookUseCase
import id.myone.capstone_books_store.book.domain.usecase.SetFavoriteBookUseCase
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.launch


class DetailBookViewModel(
    private val getDetailBookUseCase: GetDetailBookUseCase,
    private val getFavoriteBookStatusUseCase: GetBookFavoriteStatusUseCase,
    private val setFavoriteBookUseCase: SetFavoriteBookUseCase,
) : ViewModel() {
    private var _bookDetail = MutableLiveData<Result<BookDetail>>()
    val bookDetails: LiveData<Result<BookDetail>> = _bookDetail

    fun loadBookDetail(bookId: String) {
        viewModelScope.launch {
            val data = getDetailBookUseCase(bookId)
            data.collect { _bookDetail.postValue(it) }
        }
    }

    fun getBookFavoriteStatus(bookId: String) = liveData {
        val result = getFavoriteBookStatusUseCase(bookId)
        emit(result)
    }

    fun setFavoriteBook(bookId: String, book: BookDetail, value: Boolean) = liveData {
        val result = setFavoriteBookUseCase(bookId, book, value)
        emit(result)
    }

}