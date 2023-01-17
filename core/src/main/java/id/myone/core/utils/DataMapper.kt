package id.myone.core.utils

import id.myone.core.data.source.local.entity.BookEntity
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import id.myone.core.data.source.remote.response.BookModel
import id.myone.core.data.source.remote.response.DetailBooksResponse
import id.myone.core.data.source.remote.response.PdfModel
import id.myone.core.domain.entity.Book
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.entity.Pdf

object DataMapper {
    fun mapBookModelToBookDomain(bookModelList: List<BookModel>) = bookModelList.map {
        Book(
            id = it.isbn13,
            title = it.title,
            subtitle = it.subtitle,
            price = it.price,
            url = it.url,
            image = it.image
        )
    }

    fun mapBookEntityToBookDomain(bookEntityList: List<BookEntity>) = bookEntityList.map {
        Book(
            id = it.id,
            title = it.title,
            subtitle = it.subtitle,
            price = it.price,
            url = it.url,
            image = it.image,
        )
    }

    fun mapFavoriteBookEntityToBookDomain(favoriteBookList: List<FavoriteBookEntity>) =
        favoriteBookList.map {
            Book(
                id = it.book.id,
                title = it.book.title,
                subtitle = it.book.subtitle,
                price = it.book.price,
                url = it.book.url,
                image = it.book.image,
            )
        }

    private fun transformPdfModelToDomain(pdfModel: PdfModel): Pdf {
        return Pdf(
            chapter2 = pdfModel.chapter2,
            chapter5 = pdfModel.chapter5,
        )
    }

    fun transformDetailBookToDetailBookDomain(bookDetail: DetailBooksResponse): BookDetail {
        return BookDetail(
            authors = bookDetail.authors,
            desc = bookDetail.desc,
            error = bookDetail.error,
            image = bookDetail.image,
            isbn10 = bookDetail.isbn10,
            isbn13 = bookDetail.isbn13,
            pages = bookDetail.pages,
            pdf = transformPdfModelToDomain(bookDetail.pdf),
            price = bookDetail.price,
            publisher = bookDetail.publisher,
            rating = bookDetail.rating,
            subtitle = bookDetail.subtitle,
            title = bookDetail.title,
            url = bookDetail.url,
            year = bookDetail.year
        )
    }

    fun transformBookDomainToEntity(book: Book): BookEntity {
        return BookEntity(
            id = book.id,
            title = book.title,
            subtitle = book.subtitle,
            image = book.image,
            price = book.price,
            url = book.url,
        )
    }

    fun transformBookModelToEntity(book: BookModel): BookEntity {
        return BookEntity(
            id = book.isbn13,
            title = book.title,
            subtitle = book.subtitle,
            image = book.image,
            price = book.price,
            url = book.url,
        )
    }
}