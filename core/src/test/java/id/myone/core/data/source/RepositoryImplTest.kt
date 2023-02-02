package id.myone.core.data.source

import app.cash.turbine.test
import id.myone.core.data.source.local.LocalDataSource
import id.myone.core.data.source.local.entity.BookEntity
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import id.myone.core.data.source.remote.RemoteDataSource
import id.myone.core.data.source.remote.network.ApiResponse
import id.myone.core.data.source.remote.response.BookModel
import id.myone.core.data.source.remote.response.DetailBooksResponse
import id.myone.core.data.source.remote.response.ListBooksResponse
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import id.myone.core.utils.AppExecutors
import id.myone.core.utils.DataMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryImplTest {

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var appExecutors: AppExecutors

    private lateinit var repositoryImpl: RepositoryImpl

    @Before
    fun setUp() {
        repositoryImpl = RepositoryImpl(
            localDatasource = localDataSource,
            remoteDataSource = remoteDataSource,
            appExecutors = appExecutors,
        )
    }

    private val theBookId = "123"
    private val searchQuery = "java for beginner"
    private val pageNumber = "1"

    private val bookEntityList = listOf(
        BookEntity(
            id = "1",
            title = "title",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )

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

    private val book = Book(
        id = "10",
        title = "title",
        subtitle = "subtitle",
        price = "$20",
        image = "http://test.image/abc.png",
        url = "http://test.image/book"
    )


    private val bookListResponse = ListBooksResponse(
        books = bookList,
        error = "",
        total = bookList.size.toString()
    )


    private val favoriteBookEntity = listOf(
        FavoriteBookEntity(
            id = "1",
            title = "title",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
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

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @Test
    fun `should get book list from the network when local database is empty`() = runTest {

        val localBookList = MutableStateFlow<List<BookEntity>>(emptyList())
        val localBookListAfterNetworkRequest = MutableStateFlow(bookEntityList)

        val apiBookResponse = ApiResponse.Success(bookListResponse.books)
        val remoteBookListResponse = MutableStateFlow<ApiResponse<List<BookModel>>>(
            apiBookResponse
        )

        // arrange
        `when`(localDataSource.getBooks()).thenReturn(
            localBookList,
            localBookListAfterNetworkRequest
        )
        `when`(remoteDataSource.getBookList()).thenReturn(remoteBookListResponse)
        `when`(localDataSource.bulkInsertBook(anyList())).thenAnswer { }

        // act
        val dataBookList = repositoryImpl.getBooksList()

        dataBookList.test {

            // verify
            verify(remoteDataSource).getBookList()
            verify(localDataSource, atLeastOnce()).getBooks()
            verify(localDataSource).bulkInsertBook(anyList())

            val loadingState = awaitItem()
            val successState = awaitItem()

            // assert
            assertTrue(loadingState is Result.Loading)
            assertTrue(successState is Result.Success)
            assertEquals((successState as Result.Success).data?.size, bookList.size)
        }
    }

    @Test
    fun `should get from local database when local database is not empty`() = runTest {

        // arrange
        val localBookList = MutableStateFlow(bookEntityList)
        `when`(localDataSource.getBooks()).thenReturn(localBookList)

        // act
        repositoryImpl.getBooksList().test {

            // verify
            verify(localDataSource, times(2)).getBooks()

            // assert

            val loadingState = awaitItem()
            val successState = awaitItem()

            assertTrue(loadingState is Result.Loading)
            assertTrue(successState is Result.Success)

            assertEquals(successState.data?.size, bookEntityList.size)
        }
    }

    @Test
    fun `should get favorite book with success and not empty data`() = runTest {
        // arrange
        val favoriteBook = MutableStateFlow(favoriteBookEntity)

        `when`(localDataSource.getFavoriteBooks()).thenReturn(favoriteBook)

        // act
        repositoryImpl.getFavoriteBookList().test {
            // verify
            val favoriteBookResult = awaitItem()

            // assert
            assertTrue(favoriteBookResult.isNotEmpty())
            assertEquals(favoriteBookResult.size, favoriteBookEntity.size)
        }
    }


    @Test
    fun `should add book into favorite book list successfully`() = runTest {
        // arrange
        `when`(localDataSource.insertFavoriteBook(any())).thenAnswer { }
        // act
        val result = repositoryImpl.insertFavoriteBook(book)
        // verify
        verify(localDataSource).insertFavoriteBook(DataMapper.transformBookToFavoriteBookEntity(book))

        // assert
        assertTrue(result is Result.Success)
        assertEquals(result.data, "Success to add to list favorite book")
    }

    @Test
    fun `should failure when add book into favorite book list `() = runTest {
        // arrange
        `when`(localDataSource.insertFavoriteBook(any())).thenAnswer { throw Exception() }
        // act
        val result = repositoryImpl.insertFavoriteBook(book)

        // verify
        verify(localDataSource).insertFavoriteBook(DataMapper.transformBookToFavoriteBookEntity(book))

        // assert
        assertTrue(result is Result.Error)
        assertEquals(result.message, "failed to add book into list favorite book")
    }

    @Test
    fun `should success to delete book from list of favorite book`() = runTest {
        // arrange
        `when`(localDataSource.deleteFavoriteBook(theBookId)).thenAnswer { }

        // act
        val result = repositoryImpl.deleteFavoriteBook(theBookId)

        // verify
        verify(localDataSource).deleteFavoriteBook(theBookId)

        // assert
        assertTrue(result is Result.Success)
        assertEquals(result.data, "Success to delete to list favorite book")

    }

    @Test
    fun `should failure to delete book from list of favorite book`() = runTest {

        // arrange
        `when`(localDataSource.deleteFavoriteBook(theBookId)).thenAnswer { throw Exception() }

        // act
        val result = repositoryImpl.deleteFavoriteBook(theBookId)

        // verify
        verify(localDataSource).deleteFavoriteBook(theBookId)

        // assert
        assertTrue(result is Result.Error)
        assertEquals(result.message, "failed to delete book into list favorite book")

    }

    @Test
    fun `should success to search book by query of the book title and return not empty data`(): Unit =
        runBlocking {
            // arrange
            `when`(remoteDataSource.searchBook(searchQuery, pageNumber)).thenReturn(
                ApiResponse.Success(bookListResponse)
            )

            // act
            val result = repositoryImpl.searchBooks(searchQuery, pageNumber).first()

            // verify
            verify(remoteDataSource).searchBook(searchQuery, pageNumber)

            // assert
            assertTrue(result is Result.Success)
            assertEquals(result.data?.size, bookList.size)


            result.data?.first()?.let {
                val expectedBookList = bookList.first()
                assertEquals(it.title, expectedBookList.title)
                assertEquals(it.id, expectedBookList.isbn13)
                assertEquals(it.url, expectedBookList.url)
                assertEquals(it.price, expectedBookList.price)
                assertEquals(it.subtitle, expectedBookList.subtitle)
            }
        }

    @Test
    fun `should success to search book by query of the book title and return empty data`() =
        runTest {
            // arrange
            `when`(remoteDataSource.searchBook(searchQuery, pageNumber)).thenReturn(
                ApiResponse.Empty
            )

            // act
            val result = repositoryImpl.searchBooks(searchQuery, pageNumber).first()

            // verify
            verify(remoteDataSource).searchBook(searchQuery, pageNumber)

            // assert
            assertTrue(result is Result.Success)
            assertTrue(result.data?.isEmpty()!!)

        }


    @Test
    fun `should failure to search book by query of the book title when no internet connection`() =
        runTest {
            // arrange
            `when`(remoteDataSource.searchBook(searchQuery, pageNumber)).thenReturn(
                ApiResponse.Error(errorMessage = "failed to connect service, please try again")
            )

            // act
            val result = repositoryImpl.searchBooks(searchQuery, pageNumber).first()

            // verify
            verify(remoteDataSource).searchBook(searchQuery, pageNumber)

            // assert
            assertTrue(result is Result.Error)
            assertEquals(result.message, "failed to connect service, please try again")


        }

    @Test
    fun `should get detail book with successfully`() = runTest {
        // arrange
        `when`(remoteDataSource.getDetailBook(theBookId)).thenReturn(
            ApiResponse.Success(detailBookResponse)
        )


        // act
        val result = repositoryImpl.getDetailBook(theBookId)

        result.test {

            // verify
            verify(remoteDataSource).getDetailBook(theBookId)

            // assert
            val loadingState = awaitItem()
            val dataState = awaitItem()

            assertTrue(loadingState is Result.Loading)
            assertTrue(dataState is Result.Success)

            assertEquals(dataState.data?.url, detailBookResponse.url)
            assertEquals(dataState.data?.title, detailBookResponse.title)
            assertEquals(dataState.data?.subtitle, detailBookResponse.subtitle)
            assertEquals(dataState.data?.price, detailBookResponse.price)
            assertEquals(dataState.data?.image, detailBookResponse.image)
            assertEquals(dataState.data?.authors, detailBookResponse.authors)
            awaitComplete()
        }
    }

    @Test
    fun `get detail book should failure when no internet connection`() = runTest {

        // arrange
        `when`(remoteDataSource.getDetailBook(theBookId)).thenReturn(
            ApiResponse.Error("no internet connection please try again")
        )

        // act
        val result = repositoryImpl.getDetailBook(theBookId)

        result.test {

            // verify
            verify(remoteDataSource).getDetailBook(theBookId)

            val loadingState = awaitItem()
            val dataState = awaitItem()

            assertTrue(loadingState is Result.Loading)

            assertTrue(dataState is Result.Error)

            assertEquals(dataState.message, "no internet connection please try again")
            awaitComplete()
        }
    }

    @Test
    fun `book should exist list of the favorite book list`() = runTest {
        // arrange
        `when`(localDataSource.getFavoriteBookById(theBookId)).thenReturn(favoriteBookEntity.first())
        // act
        val result = repositoryImpl.getIsInFavoriteBook(theBookId)
        // verify
        verify(localDataSource).getFavoriteBookById(theBookId)
        // assert
        assertTrue(result)
    }

    @Test
    fun `book should not exist list of the favorite book list`() = runTest {
        // arrange
        `when`(localDataSource.getFavoriteBookById(theBookId)).thenReturn(null)
        // act
        val result = repositoryImpl.getIsInFavoriteBook(theBookId)
        // verify
        verify(localDataSource).getFavoriteBookById(theBookId)
        // assert
        assertFalse(result)
    }

    @Test
    fun `should success to get book from local database`() = runTest {
        // arrange
        val bookEntity = bookEntityList.first()
        `when`(localDataSource.getBookById(theBookId)).thenReturn(bookEntity)
        // act
        val book = repositoryImpl.getBookById(theBookId)
        // verify
        verify(localDataSource).getBookById(theBookId)
        // assert
        assertNotNull(book)
        assertEquals(book?.id, bookEntity.id)
        assertEquals(book?.title, bookEntity.title)
        assertEquals(book?.subtitle, bookEntity.subtitle)
        assertEquals(book?.price, bookEntity.price)
        assertEquals(book?.image, bookEntity.image)
        assertEquals(book?.url, bookEntity.url)
    }
}