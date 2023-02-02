package id.myone.core.data.source.remote

import id.myone.core.data.source.remote.network.ApiResponse
import id.myone.core.data.source.remote.network.ApiServices
import id.myone.core.data.source.remote.response.BookModel
import id.myone.core.data.source.remote.response.DetailBooksResponse
import id.myone.core.data.source.remote.response.ListBooksResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.net.SocketException


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {
    @Mock
    lateinit var apiServices: ApiServices
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSource(apiServices)
    }

    private val theBookId = "1"
    private val pageSearch = "1"
    private val titleQuery = "java for beginner"

    private val bookList = listOf(
        BookModel(
            isbn13 = "10",
            title = "title",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )

    private val bookListResponse = ListBooksResponse(
        books = bookList,
        error = "",
        total = bookList.size.toString()
    )

    private val detailBookResponse = DetailBooksResponse(
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
    fun `should success to get list of the book`() = runTest {
        // arrange
        `when`(apiServices.getNewListBooks()).thenReturn(bookListResponse)

        // act
        val bookList = remoteDataSource.getBookList().first()

        // verify
        verify(apiServices).getNewListBooks()

        // assert
        assertTrue(bookList is ApiResponse.Success)
        assertEquals((bookList as ApiResponse.Success).data, bookListResponse.books)
        assertEquals(bookList.data.size, bookListResponse.books.size)
    }

    @Test
    fun `should failure to get list of the book when no internet connection`() = runTest {
        // arrange
        `when`(apiServices.getNewListBooks()).thenAnswer { throw SocketException() }

        // act
        val bookList = remoteDataSource.getBookList().first()

        // verify
        verify(apiServices).getNewListBooks()

        // assert
        assertTrue(bookList is ApiResponse.Error)
        assertEquals(
            (bookList as ApiResponse.Error).errorMessage,
            "failed to connect service, please try again"
        )
    }

    @Test
    fun `should get detail of book with success`() = runTest {
        // arrange
        `when`(apiServices.getDetailBook(theBookId)).thenReturn(detailBookResponse)

        // act
        val detailBook = remoteDataSource.getDetailBook(theBookId)

        // verify
        verify(apiServices).getDetailBook(theBookId)

        // assert
        assertTrue(detailBook is ApiResponse.Success)
        assertEquals((detailBook as ApiResponse.Success).data, detailBookResponse)
    }

    @Test
    fun `should get detail of the book with error when no internet connection`() = runTest {
        // arrange
        `when`(apiServices.getDetailBook(theBookId)).thenAnswer { throw SocketException() }

        // act
        val detailBook = remoteDataSource.getDetailBook(theBookId)

        // verify
        verify(apiServices).getDetailBook(theBookId)

        // assert
        assertTrue(detailBook is ApiResponse.Error)
        assertEquals(
            (detailBook as ApiResponse.Error).errorMessage,
            "failed to connect service, please try again"
        )
    }

    @Test
    fun `should success to search book by the title query`() = runTest {
        // arrange
        `when`(apiServices.searchBook(titleQuery, pageSearch)).thenReturn(bookListResponse)

        // act
        val bookList = remoteDataSource.searchBook(titleQuery, pageSearch)

        // verify
        verify(apiServices).searchBook(titleQuery, pageSearch)

        // assert
        assertTrue(bookList is ApiResponse.Success)
        assertEquals((bookList as ApiResponse.Success).data, bookListResponse)
    }

    @Test
    fun `should failure to search book when no internet connection`() = runTest {
        // arrange
        `when`(
            apiServices.searchBook(
                titleQuery,
                pageSearch
            )
        ).thenAnswer { throw  SocketException() }

        // act
        val bookList = remoteDataSource.searchBook(titleQuery, pageSearch)

        // verify
        verify(apiServices).searchBook(titleQuery, pageSearch)

        // assert
        assertTrue(bookList is ApiResponse.Error)
        assertEquals(
            (bookList as ApiResponse.Error).errorMessage,
            "failed to connect service, please try again"
        )
    }
}