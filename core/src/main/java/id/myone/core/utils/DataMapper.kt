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
    fun mapBookModelToBookDomain(bookModelList: List<BookModel>) =
        bookModelList.map {
            Book(
                id = it.isbn13,
                title = it.title,
                subtitle = it.subtitle,
                price = it.price,
                url = it.url,
                image = it.image,
            )
        }

    fun mapBookEntityToBookDomain(bookEntityList: List<BookEntity>) = bookEntityList.map {
        transformBookEntityToBook(it)
    }

    fun mapFavoriteBookEntityToBookEntity(favoriteBookEntityList: List<FavoriteBookEntity>): List<Book> {
        return favoriteBookEntityList.map {
            transformFavoriteBookEntityToBook(it)
        }
    }


    private fun transformPdfModelToDomain(pdfModel: PdfModel): Pdf {
        return Pdf(
            chapter2 = pdfModel.chapter2,
            chapter5 = pdfModel.chapter5,
        )
    }

    fun transformDetailBookToDetailBookDomain(bookDetail: DetailBooksResponse): BookDetail {

        val pdfDomain =
            if (bookDetail.pdf != null) transformPdfModelToDomain(bookDetail.pdf) else null

        return BookDetail(
            authors = bookDetail.authors,
            desc = bookDetail.desc,
            error = bookDetail.error,
            image = bookDetail.image,
            isbn10 = bookDetail.isbn10,
            isbn13 = bookDetail.isbn13,
            pages = bookDetail.pages,
            pdf = pdfDomain,
            price = bookDetail.price,
            publisher = bookDetail.publisher,
            rating = bookDetail.rating,
            subtitle = bookDetail.subtitle,
            title = bookDetail.title,
            url = bookDetail.url,
            year = bookDetail.year
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


    fun transformFromBookDetailToBook(
        bookDetail: BookDetail,
        bookId: String
    ): Book {
        return Book(
            id = bookId,
            title = bookDetail.title,
            subtitle = bookDetail.subtitle,
            image = bookDetail.image,
            price = bookDetail.price,
            url = bookDetail.url,
        )
    }

    fun transformBookEntityToBook(bookEntity: BookEntity): Book {
        return Book(
            id = bookEntity.id,
            title = bookEntity.title,
            subtitle = bookEntity.subtitle,
            price = bookEntity.price,
            url = bookEntity.url,
            image = bookEntity.image,
        )
    }

    fun transformBookToFavoriteBookEntity(book: Book): FavoriteBookEntity {
        return FavoriteBookEntity(
            id = book.id,
            title = book.title,
            subtitle = book.subtitle,
            price = book.price,
            url = book.url,
            image = book.image,
        )
    }

    private fun transformFavoriteBookEntityToBook(favoriteBookEntity: FavoriteBookEntity): Book = Book(
        id = favoriteBookEntity.id,
        title = favoriteBookEntity.title,
        subtitle = favoriteBookEntity.subtitle,
        price = favoriteBookEntity.price,
        url = favoriteBookEntity.url,
        image = favoriteBookEntity.image,
    )
}