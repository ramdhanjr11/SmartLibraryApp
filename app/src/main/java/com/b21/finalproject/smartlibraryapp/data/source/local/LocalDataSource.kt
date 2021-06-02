package com.b21.finalproject.smartlibraryapp.data.source.local

import androidx.sqlite.db.SupportSQLiteQuery
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BorrowBookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.FavoriteBookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.room.LibraryDao

class LocalDataSource private constructor(private val mLibraryDao: LibraryDao) {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(libraryDao: LibraryDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(libraryDao)
    }

    fun getAllBooks(sort: SupportSQLiteQuery): List<BookEntity> = mLibraryDao.getAllbooks(sort)

    fun getBookById(bookId: Int): BookEntity = mLibraryDao.getBookById(bookId)

    fun insertBorrowBook(borrowBookEntity: BorrowBookEntity) = mLibraryDao.insertBorrowBook(borrowBookEntity)

    fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity) = mLibraryDao.insertFavoriteBook(favoriteBookEntity)

    fun getAllFavoriteBook(userId: String, onAllFavoriteBooksCallback: LoadFavoriteBooksCallback) {
        val favoriteBookEntity = mLibraryDao.getAllFavoriteBook(userId)
        onAllFavoriteBooksCallback.onAllFavoriteBooksReceived(favoriteBookEntity)
    }

    fun getAllBorrowBook(userId: String, onAllBorrowBooksCallback: LoadBorrowBooksCallback) {
        val borrowBookEntity = mLibraryDao.getAllBorrowBook(userId)
        onAllBorrowBooksCallback.onAllBorrowBooksReceived(borrowBookEntity)
    }

    interface LoadFavoriteBooksCallback {
        fun onAllFavoriteBooksReceived(favoriteBookEntity: List<FavoriteBookEntity>)
    }

    interface LoadBorrowBooksCallback {
        fun onAllBorrowBooksReceived(borrowBookEntity: List<BorrowBookEntity>)
    }
}