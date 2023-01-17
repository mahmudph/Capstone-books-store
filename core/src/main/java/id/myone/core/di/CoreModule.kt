/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.di

import androidx.room.Room
import id.myone.core.data.source.RepositoryImpl
import id.myone.core.data.source.local.LocalDataSource
import id.myone.core.data.source.local.room.BookDatabase
import id.myone.core.data.source.remote.RemoteDataSource
import id.myone.core.data.source.remote.network.ApiConfig
import id.myone.core.domain.repository.Repository
import id.myone.core.utils.AppExecutors
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    factory { get<BookDatabase>().booksDao() }
    factory { get<BookDatabase>().favoriteBookDao() }
    single {
        Room.databaseBuilder(
            androidContext(), BookDatabase::class.java, "db_book_store.db",
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        ApiConfig.provideApiService()
    }
}

val repositoryModule = module {
    single { LocalDataSource(get(), get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<Repository> { RepositoryImpl(get(), get(), get()) }
}
