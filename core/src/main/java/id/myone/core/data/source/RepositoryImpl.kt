/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source

import id.myone.core.data.source.local.LocalDataSource
import id.myone.core.data.source.local.entity.FavoriteEntity
import id.myone.core.data.source.remote.RemoteDataSource
import id.myone.core.data.source.remote.network.ApiResponse
import id.myone.core.data.source.remote.response.BookModel
import id.myone.core.domain.entity.Book
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
import id.myone.core.utils.AppExecutors
import id.myone.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.SocketException

class RepositoryImpl(
    private val localDatasource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val appExecutors: AppExecutors
) : Repository {

    override fun getBooksList(): Flow<Result<List<Book>>> =
        object : NetworkBoundResource<List<Book>, List<BookModel>>(appExecutors) {

            override fun loadFromDB(): Flow<List<Book>> {
                return localDatasource.getBooks().map {
                    DataMapper.mapBookEntityToBookDomain(it)
                }
            }

            override fun shouldFetch(data: List<Book>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<BookModel>>> {
                return remoteDataSource.getBookList()
            }

            override suspend fun saveCallResult(data: List<BookModel>) {
                val bookEntityList = data.map { DataMapper.transformBookModelToEntity(it) }
                localDatasource.bulkInsertBook(bookEntityList)
            }

        }.asFlow()

    override fun getFavoriteBookList(): Flow<List<Book>> {
        return localDatasource.getFavoriteBooks().map {
            DataMapper.mapFavoriteBookEntityToBookDomain(it)
        }
    }

    override suspend fun searchBooks(query: String, page: String): Result<List<Book>> {
        return try {

            val response = remoteDataSource.searchBook(query, page)
            Result.Success(DataMapper.mapBookModelToBookDomain(response.books))

        } catch (e: SocketException) {
            Result.Error(message = noInternetConnectionError)
        } catch (e: Exception) {
            Result.Error(message = defaultErrorMessage)
        }
    }

    override suspend fun getDetailBook(id: String): Result<BookDetail> {
        return try {
            val response = remoteDataSource.getDetailBook(id)
            Result.Success(DataMapper.transformDetailBookToDetailBookDomain(response))
        } catch (e: SocketException) {
            Result.Error(message = noInternetConnectionError)
        } catch (e: Exception) {
            Result.Error(message = defaultErrorMessage)
        }
    }

    override suspend fun setFavoriteBook(book: FavoriteEntity): Result<String> {
        return try {
            localDatasource.setFavoriteBook(book)
            Result.Success(successAddToFavorite)
        } catch (e: Exception) {
            Result.Error(failedToAddFavorite)
        }
    }

    override suspend fun getIsInFavoriteBook(bookId: String): Boolean {
        return try {
            val book = localDatasource.getFavoriteBookById(bookId)
            return book?.favorite?.status ?: false
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteFavoriteBook(id: Int): Result<String> {
        return try {
            localDatasource.deleteFavoriteBook(id)
            Result.Success(successToDeleteFavoriteBook)
        } catch (e: Exception) {
            Result.Error(failedToDeleteFavoriteBook)
        }
    }

    companion object {
        private const val failedToDeleteFavoriteBook = "failed to delete favorite book"
        private const val successToDeleteFavoriteBook = "success to delete favorite book"

        private const val successAddToFavorite = "Success to set book to the list favorite books"
        private const val failedToAddFavorite = "failed to set book to the list favorite books"

        private const val defaultErrorMessage = "failed to process request, try again"
        private const val noInternetConnectionError =
            "no network available, please check your internet connection"
    }
}