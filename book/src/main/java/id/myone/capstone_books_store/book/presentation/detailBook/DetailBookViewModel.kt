/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.presentation.detailBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import id.myone.capstone_books_store.book.domain.usecase.GetBookFavoriteStatusUseCase
import id.myone.capstone_books_store.book.domain.usecase.GetDetailBookUseCase
import id.myone.capstone_books_store.book.domain.usecase.SetFavoriteBookUseCase
import id.myone.core.domain.entity.BookDetail

class DetailBookViewModel(
    private val getDetailBookUseCase: GetDetailBookUseCase,
    private val getFavoriteBookStatusUseCase: GetBookFavoriteStatusUseCase,
    private val setFavoriteBookUseCase: SetFavoriteBookUseCase,
) : ViewModel() {

    fun loadBookDetail(bookId: String) = getDetailBookUseCase(bookId).asLiveData()

    fun getBookFavoriteStatus(bookId: String) = liveData {
        val result = getFavoriteBookStatusUseCase(bookId)
        emit(result)
    }

    fun setFavoriteBook(bookId: String, book: BookDetail) = liveData {
        val result = setFavoriteBookUseCase(bookId, book)
        emit(result)
    }

}