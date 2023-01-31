package id.myone.core.data.source.local

import id.myone.core.data.source.local.entity.BookEntity
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import id.myone.core.data.source.local.room.BookDao
import id.myone.core.data.source.local.room.FavoriteBookDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceTest {

    @Mock
    lateinit var bookDao: BookDao

    @Mock
    lateinit var favoriteBookDao: FavoriteBookDao
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        localDataSource = LocalDataSource(bookDao, favoriteBookDao)
    }

    private val theBookId = "10"

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

    @Test
    fun `get book list should not empty data book entity`() =
        runTest {
            // arrange
            val mutableStateFlow = MutableStateFlow(value = bookEntityList)
            `when`(bookDao.getBooks()).thenReturn(mutableStateFlow)

            // act
            val bookList = localDataSource.getBooks().first()

            // verify
            verify(bookDao).getBooks()

            // assert
            assertEquals(bookList, bookEntityList)
            assertTrue(bookList.isNotEmpty())
        }

    @Test
    fun `should get favorite book with not empty data`() = runTest {
        // arrange
        val mutableStateFlow = MutableStateFlow(value = favoriteBookEntity)
        `when`(favoriteBookDao.getFavoriteBooks()).thenReturn(mutableStateFlow)

        // act
        val favoriteBookList = localDataSource.getFavoriteBooks().first()

        // verify
        verify(favoriteBookDao).getFavoriteBooks()
       // assert
        assertEquals(favoriteBookList, favoriteBookEntity)
        assertTrue(favoriteBookList.isNotEmpty())
    }

    @Test
    fun `get favorite book by specific id should not null`() = runTest {
        // arrange
        `when`(favoriteBookDao.getFavoriteBookById(theBookId)).thenReturn(favoriteBookEntity.first())

        // act
        val favorite = localDataSource.getFavoriteBookById(theBookId)

        // verify
        verify(favoriteBookDao).getFavoriteBookById(theBookId)
        // assert
        assertEquals(favorite, favoriteBookEntity.first())
    }

    @Test
    fun `get book by specific id should not null`() = runTest {

        // arrange
        `when`(bookDao.getBookById(theBookId)).thenReturn(bookEntityList.first())

        // act
        val book = localDataSource.getBookById(theBookId)

        // verify
        verify(bookDao).getBookById(theBookId)
        // assert
        assertEquals(book, bookEntityList.first())
    }

    @Test
    fun `add favorite book should success`() = runTest {
        // arrange
        `when`(favoriteBookDao.insertFavorite(favoriteBookEntity.first())).thenAnswer {}

        // act
        localDataSource.insertFavoriteBook(favoriteBookEntity.first())

        // verify
        verify(favoriteBookDao).insertFavorite(favoriteBookEntity.first())

    }

    @Test
    fun `add bulk book should success`() = runTest {
        // arrange
        `when`(bookDao.bulkInsert(bookEntityList)).thenAnswer {  }
        // act
        localDataSource.bulkInsertBook(bookEntityList)
        // verify
        verify(bookDao).bulkInsert(bookEntityList)
    }
}