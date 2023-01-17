/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.remote

import id.myone.core.data.source.remote.network.ApiResponse
import id.myone.core.data.source.remote.network.ApiServices
import id.myone.core.data.source.remote.response.BookModel
import id.myone.core.data.source.remote.response.DetailBooksResponse
import id.myone.core.data.source.remote.response.ListBooksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiServices: ApiServices) {
    suspend fun getBookList(): Flow<ApiResponse<List<BookModel>>> = flow {
        try {
            val bookList = apiServices.getNewListBooks().books
            if (bookList.isNotEmpty()) emit(ApiResponse.Success(bookList))
            else emit(ApiResponse.Empty)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailBook(id: String): DetailBooksResponse {
        return apiServices.getDetailBook(id)
    }

    suspend fun searchBook(query: String, page: String): ListBooksResponse {
        return apiServices.searchBook(query, page)
    }
}