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
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteBookDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var favoriteBookDao: FavoriteBookDao
    private lateinit var bookDatabase: BookDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        bookDatabase = Room.inMemoryDatabaseBuilder(
            context,
            BookDatabase::class.java
        ).allowMainThreadQueries().build()
        favoriteBookDao = bookDatabase.favoriteBookDao()
    }

    @After
    fun tearDown() {
        bookDatabase.close()
    }

    private val favoriteBookList = listOf(
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
    fun testGetFavoriteBooksShouldSuccess() = runTest {
        // assign
        favoriteBookList.forEach { favoriteBookDao.insertFavorite(it) }
        // act
        val favoriteBooks = favoriteBookDao.getFavoriteBooks().first()
        // assert
        Assert.assertTrue(favoriteBooks.isNotEmpty())
        Assert.assertEquals(favoriteBooks.size, favoriteBookList.size)
    }

    @Test
    fun testInsertFavorite() = runTest {
        // assign
        favoriteBookDao.insertFavorite(favoriteBookList.first())
        // act
        val favoriteBooks = favoriteBookDao.getFavoriteBooks().first()
        // assert
        Assert.assertTrue(favoriteBooks.isNotEmpty())
        Assert.assertEquals(favoriteBooks.size, 1)
    }

    @Test
    fun testDeleteFavorite() = runTest {
        // assign
        favoriteBookDao.insertFavorite(favoriteBookList.first())

        // verify
        val favoriteBooks = favoriteBookDao.getFavoriteBooks().first()

        // assert
        Assert.assertTrue(favoriteBooks.isNotEmpty())
        Assert.assertEquals(favoriteBooks.size, 1)

        // act delete
        favoriteBookDao.deleteFavorite("1")
        val favoriteBooksSeconds = favoriteBookDao.getFavoriteBooks().first()

        // assert
        Assert.assertTrue(favoriteBooksSeconds.isEmpty())
        Assert.assertEquals(favoriteBooksSeconds.size, 0)

    }

    @Test
    fun testGetFavoriteBookById() = runTest {
        // assign
        favoriteBookDao.insertFavorite(favoriteBookList.first())
        // assert
        val favoriteBook = favoriteBookDao.getFavoriteBookById("1")
        // verify
        Assert.assertNotNull(favoriteBook)
        Assert.assertEquals(favoriteBook, favoriteBookList.first())
    }
}