package id.myone.capstone_books_store.book.domain.usecase

import app.cash.turbine.test
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetDetailBookUseCaseTest {
    @Mock
    private lateinit var repository: Repository
    private lateinit var getDetailBookUseCase: GetDetailBookUseCase

    @Before
    fun setUp() {
        getDetailBookUseCase = GetDetailBookUseCase(repository)
    }

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

    @Test
    fun `should get detail book with successfully`() = runTest {
        // arrange
        val flowState = flowOf(Result.Loading(), Result.Success(detailBook))
        `when`(repository.getDetailBook(bookId)).thenReturn(flowState)

        // act
        val bookDetailResult = getDetailBookUseCase(bookId)

        bookDetailResult.test {
            // verify
            verify(repository).getDetailBook(bookId)

            val loadingState = awaitItem()
            val dataState = awaitItem()

            // assert
            Assert.assertTrue(loadingState is Result.Loading)
            Assert.assertTrue(dataState is Result.Success)

            Assert.assertEquals(dataState.data, detailBook)
            Assert.assertEquals(dataState.data?.title, detailBook.title)
            awaitComplete()
        }
    }

    @Test
    fun `should failure when get detail book with no internet connection`() = runTest {
        // arrange
        val flowState = flowOf<Result<BookDetail>>(Result.Loading(), Result.Error("no internet connection, please try again"))
        `when`(repository.getDetailBook(bookId)).thenReturn(flowState)

        // act
        val bookDetailResult = getDetailBookUseCase(bookId)

        bookDetailResult.test {
            // verify
            verify(repository).getDetailBook(bookId)

            val loadingState = awaitItem()
            val dataState = awaitItem()

            // assert
            Assert.assertTrue(loadingState is Result.Loading)
            Assert.assertTrue(dataState is Result.Error)
            Assert.assertNull(dataState.data)

            Assert.assertEquals(dataState.message, "no internet connection, please try again")
            awaitComplete()
        }
    }
}