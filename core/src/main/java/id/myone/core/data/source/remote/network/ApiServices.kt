/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.remote.network

import id.myone.core.data.source.remote.response.DetailBooksResponse
import id.myone.core.data.source.remote.response.ListBooksResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {
    @GET("/1.0/new")
    suspend fun getNewListBooks(): ListBooksResponse

    @GET("/1.0/books/:id")
    suspend fun getDetailBook(@Path("id") id: String): DetailBooksResponse

    @GET("/1.0/search/:query/:page")
    suspend fun searchBook(@Path("query") query: String, page: String): ListBooksResponse
}