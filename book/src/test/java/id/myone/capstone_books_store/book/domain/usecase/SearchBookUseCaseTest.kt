package id.myone.capstone_books_store.book.domain.usecase

import app.cash.turbine.test
import id.myone.core.domain.entity.Book
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
class SearchBookUseCaseTest {

    @Mock lateinit var repository: Repository
    private lateinit var searchBookUseCase: SearchBookUseCase

    @Before
    fun setUp() {
        searchBookUseCase = SearchBookUseCase(repository)
    }

    private val pageSearch = "1"
    private val querySearch = "java complete for beginner"
    private val bookSearchList = listOf(
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
    fun `search book should success and give list of the book with no empty data`() = runTest {
        // arrange
        val searchFlowState = flowOf(Result.Success(bookSearchList))
        `when`(repository.searchBooks(querySearch, pageSearch)).thenReturn(searchFlowState)

        // act
        val searchResult = searchBookUseCase(querySearch, pageSearch)

        searchResult.test {
            // verify
            verify(repository).searchBooks(querySearch, pageSearch)

            val dataState = awaitItem()
            // assert
            Assert.assertTrue(dataState is Result.Success)
            Assert.assertEquals(dataState.data?.size, bookSearchList.size)
            Assert.assertEquals(dataState.data, bookSearchList)
            awaitComplete()
        }
    }

    @Test
    fun `search book should success and give empty list of the book data`() = runTest {
        // arrange
        val searchFlowState = flowOf<Result<List<Book>>>(Result.Success(emptyList()))
        `when`(repository.searchBooks(querySearch, pageSearch)).thenReturn(searchFlowState)

        // act
        val searchResult = searchBookUseCase(querySearch, pageSearch)

        searchResult.test {

            // verify
            verify(repository).searchBooks(querySearch, pageSearch)

            val dataState = awaitItem()
            // assert
            Assert.assertTrue(dataState is Result.Success)
            Assert.assertTrue(dataState.data!!.isEmpty())
            Assert.assertEquals(dataState.data?.size, 0)
            awaitComplete()
        }
    }

    @Test
    fun `search book should failure when no internet connection`() = runTest {
        // arrange
        val searchFlowState = flowOf<Result<List<Book>>>(Result.Error("no internet connection, please try again"))
        `when`(repository.searchBooks(querySearch, pageSearch)).thenReturn(searchFlowState)

        // act
        val searchResult = searchBookUseCase(querySearch, pageSearch)

        searchResult.test {

            // verify
            verify(repository).searchBooks(querySearch, pageSearch)

            val dataState = awaitItem()
            // assert
            Assert.assertTrue(dataState is Result.Error)
            Assert.assertNull(dataState.data)
            Assert.assertEquals(dataState.message, "no internet connection, please try again")
            awaitComplete()
        }
    }
}