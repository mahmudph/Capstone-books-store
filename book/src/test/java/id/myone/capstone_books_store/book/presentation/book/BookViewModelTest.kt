package id.myone.capstone_books_store.book.presentation.book


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.myone.capstone_books_store.book.domain.usecase.GetBookListUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import id.myone.test_utility.utility.MainDispatcherRule
import id.myone.test_utility.utility.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BookViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var getBookUseCase: GetBookListUseCase

    private val booksLists = listOf(
        Book(
            id = "1",
            title = "title",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )

    @Test
    fun `should get book list with success and not empty data`() = runTest {

        val bookListFlow = flowOf(Result.Loading(), Result.Success(booksLists))
        `when`(getBookUseCase()).thenReturn(bookListFlow)

        val bookViewModel = BookViewModel(getBookUseCase)

        // act
        val resultData = bookViewModel.bookListData

        resultData.observeForTesting {
            // verify
            verify(getBookUseCase).invoke()
            // assert
            Assert.assertTrue(resultData.value is Result.Success)
            Assert.assertEquals(resultData.value?.data, booksLists)
        }
    }

    @Test
    fun `should get book list with failure and not empty data when no internet connection`() =
        runTest {

            val bookListFlow = flowOf<Result<List<Book>>>(
                Result.Loading(),
                Result.Error("no internet connection, please try again")
            )

            `when`(getBookUseCase()).thenReturn(bookListFlow)

            val bookViewModel = BookViewModel(getBookUseCase)

            // act
            val resultData = bookViewModel.bookListData

            resultData.observeForTesting {
                // verify
                verify(getBookUseCase).invoke()
                // assert
                Assert.assertTrue(resultData.value is Result.Error)
                Assert.assertEquals(
                    resultData.value?.message,
                    "no internet connection, please try again"
                )
            }
        }
}