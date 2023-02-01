/**
 * Created by Mahmud on 01/02/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import id.myone.core.data.source.local.entity.BookEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class BookDaoTest {

    @get:Rule
    val instrument = InstantTaskExecutorRule()

    private lateinit var bookDao: BookDao
    private lateinit var bookDatabase: BookDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        bookDatabase = Room.inMemoryDatabaseBuilder(
            context,
            BookDatabase::class.java,
        ).allowMainThreadQueries().build()
        bookDao = bookDatabase.booksDao()
    }

    @After
    fun tearDown() {
        bookDatabase.close()
    }

    private val books = listOf(
        BookEntity(
            id = "1",
            title = "title",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )

    @Test
    fun testBulkInsertBooksShouldSuccess() = runTest {
        // assign
        bookDao.bulkInsert(books)

        // act
        val booksExpected = bookDao.getBooks().first()

        // assert
        Assert.assertTrue(booksExpected.isNotEmpty())
        Assert.assertEquals(booksExpected, books)
    }

    @Test
    fun testGetBookByIdShouldReturnValidData()  = runTest {
        // assign
        bookDao.bulkInsert(books)

        // act
        val book = bookDao.getBookById("1")
        // assert
        Assert.assertNotNull(book)
        Assert.assertEquals(book, books.first())
    }

    @Test
    fun testMultipleValueShouldReplaceData() = runTest {
        // assign
        bookDao.bulkInsert(books)
        bookDao.bulkInsert(books)
        // act
        val books = bookDao.getBooks().first()
        // assert
        Assert.assertTrue(books.isNotEmpty())
        Assert.assertEquals(books.size, 1)
    }
}