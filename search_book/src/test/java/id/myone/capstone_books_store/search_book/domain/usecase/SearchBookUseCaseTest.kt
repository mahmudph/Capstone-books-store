package id.myone.capstone_books_store.search_book.domain.usecase


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
    private val searchQuery = "java for absolutely beginner"
    private val books = listOf(
        Book(
            id = "10",
            title = "java absolutely beginner",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )

    @Test
    fun `search book should success and return list of related book`() = runTest {

        // assign
        val searchResultFlow = flowOf(Result.Success(books))
        `when`(repository.searchBooks(searchQuery, pageSearch)).thenReturn(searchResultFlow)

        // act
        val result = searchBookUseCase(searchQuery, pageSearch)

        result.test {
            // verify
            verify(repository).searchBooks(searchQuery, pageSearch)

            // assert
            val dataState = awaitItem()
            Assert.assertTrue(dataState is Result.Success)
            Assert.assertEquals(dataState.data?.size, books.size)
            Assert.assertEquals(dataState.data, books)
            awaitComplete()
        }
    }


    @Test
    fun `search book should error when no internet connection and return message error`() = runTest {

        // assign
        val searchResultFlow = flowOf<Result<List<Book>>>(Result.Error("no internet connection, please try again"))
        `when`(repository.searchBooks(searchQuery, pageSearch)).thenReturn(searchResultFlow)

        // act
        val result = searchBookUseCase(searchQuery, pageSearch)

        result.test {
            // verify
            verify(repository).searchBooks(searchQuery, pageSearch)

            // assert
            val dataState = awaitItem()
            Assert.assertTrue(dataState is Result.Error)
            Assert.assertNull(dataState.data)
            Assert.assertEquals(dataState.message, "no internet connection, please try again")
            awaitComplete()
        }
    }
}