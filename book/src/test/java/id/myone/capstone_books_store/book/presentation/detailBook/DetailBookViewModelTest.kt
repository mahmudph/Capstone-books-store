package id.myone.capstone_books_store.book.presentation.detailBook

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.myone.capstone_books_store.book.domain.usecase.GetBookFavoriteStatusUseCase
import id.myone.capstone_books_store.book.domain.usecase.GetDetailBookUseCase
import id.myone.capstone_books_store.book.domain.usecase.SetFavoriteBookUseCase
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.utils.Result
import id.myone.test_utility.utility.MainDispatcherRule
import id.myone.test_utility.utility.getOrAwaitValue
import id.myone.test_utility.utility.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailBookViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    @Mock
    lateinit var getDetailBookUseCase: GetDetailBookUseCase

    @Mock
    lateinit var getFavoriteBookStatusUseCase: GetBookFavoriteStatusUseCase

    @Mock
    lateinit var setFavoriteBookUseCase: SetFavoriteBookUseCase

    private lateinit var detailBookViewModel: DetailBookViewModel

    private val bookId = "1"
    private val detailBook = BookDetail(
        title = "title",
        subtitle = "subtitle",
        isbn10 = "isbn10",
        isbn13 = "isbn13",
        pages = "1",
        price = "$20",
        image = "http://api.storybook.io/mlkit/image.png",
        url = "http://api.storybook.io/mlkit",
        publisher = "publisher",
        authors = "author",
        desc = "desc",
        year = "2022",
        error = "none",
        pdf = null,
        rating = "10"
    )

    private suspend fun provideStubData(
        isFavoriteBook: Boolean,
        detailBookRes: Result<BookDetail>
    ) {

        `when`(getDetailBookUseCase(bookId)).thenReturn(
            flowOf(detailBookRes)
        )

        `when`(getFavoriteBookStatusUseCase(bookId)).thenReturn(isFavoriteBook)

        val actionType = if (isFavoriteBook) {
            "delete from"
        } else {
            "add into"
        }

        `when`(setFavoriteBookUseCase(bookId, detailBook, !isFavoriteBook)).thenReturn(
            Result.Success("success to $actionType favorite book list")
        )

        detailBookViewModel = DetailBookViewModel(
            getDetailBookUseCase,
            getFavoriteBookStatusUseCase,
            setFavoriteBookUseCase
        )
    }

    @Test
    fun `should get the book detail with successfully`() = runTest {
        // provide data
        provideStubData(false, Result.Success(detailBook))
        // act
        detailBookViewModel.loadBookDetail(bookId)

        val bookDetailResult = detailBookViewModel.bookDetails

        bookDetailResult.observeForTesting {
            verify(getDetailBookUseCase).invoke(bookId)

            Assert.assertTrue(bookDetailResult.value is Result.Success)
            Assert.assertEquals(bookDetailResult.value?.data, detailBook)
        }
    }


    @Test
    fun `should get message of error when no internet connection`() = runTest {
        // provide data
        provideStubData(false, Result.Error("no internet connection, please try again"))
        // act
        detailBookViewModel.loadBookDetail(bookId)

        val bookDetailResult = detailBookViewModel.bookDetails

        bookDetailResult.observeForTesting {
            verify(getDetailBookUseCase).invoke(bookId)
            // assert
            Assert.assertTrue(bookDetailResult.value is Result.Error)
            Assert.assertEquals(
                bookDetailResult.value?.message,
                "no internet connection, please try again"
            )
        }
    }

    @Test
    fun `should success to add book into favorite book list`() = runTest {
        // assign
        provideStubData(false, Result.Success(detailBook))
        // act
        val result = detailBookViewModel.setFavoriteBook(bookId, detailBook, true)

        result.observeForTesting {
            // verify
            verify(setFavoriteBookUseCase).invoke(bookId, detailBook, true)
            // assert
            Assert.assertTrue(result.value is Result.Success)
            Assert.assertEquals(result.value?.data, "success to add into favorite book list")
        }
    }

    @Test
    fun `should success to delete book from favorite book list`() = runTest {
        // assign
        provideStubData(true, Result.Success(detailBook))
        // act
        val result = detailBookViewModel.setFavoriteBook(bookId, detailBook, false)

        result.observeForTesting {
            // verify
            verify(setFavoriteBookUseCase).invoke(bookId, detailBook, false)
            // assert
            Assert.assertTrue(result.value is Result.Success)
            Assert.assertEquals(result.value?.data, "success to delete from favorite book list")
        }
    }

    @Test
    fun `should success to load book favorite status`() = runTest {
        // assign
        provideStubData(true, Result.Success(detailBook))
        // act
        val result = detailBookViewModel.getBookFavoriteStatus(bookId).getOrAwaitValue()
        // verify
        verify(getFavoriteBookStatusUseCase).invoke(bookId)
        // assert
        Assert.assertTrue(result)
    }
}