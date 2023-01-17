/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store

import android.app.Application
import id.myone.book.di.bookUseCaseModel
import id.myone.book.di.bookViewModelModule
import id.myone.core.di.databaseModule
import id.myone.core.di.networkModule
import id.myone.core.di.repositoryModule
import id.myone.favorite.di.favoriteUseCaseModule
import id.myone.favorite.di.favoriteViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class ApplicationBase : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(applicationContext)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    bookUseCaseModel,
                    bookViewModelModule,
                    favoriteUseCaseModule,
                    favoriteViewModelModule,
                )
            )
        }
    }
}