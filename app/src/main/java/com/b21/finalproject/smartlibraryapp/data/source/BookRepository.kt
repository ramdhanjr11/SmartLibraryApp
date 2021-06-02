package com.b21.finalproject.smartlibraryapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.b21.finalproject.smartlibraryapp.data.source.local.LocalDataSource
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BorrowBookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.FavoriteBookEntity
import com.b21.finalproject.smartlibraryapp.utils.SortUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BookRepository private constructor(private val localDataSource: LocalDataSource) : BookDataSource{

    companion object {
        @Volatile
        private var INSTANCE: BookRepository? = null

        fun getInstance(mLocalDataSource: LocalDataSource): BookRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BookRepository(mLocalDataSource).apply { INSTANCE = this }
            }
    }

    override fun getAllBooks(sort: String): LiveData<List<BookEntity>> {
        val query = SortUtils.getSortedQuery(sort)
        val result = MutableLiveData<List<BookEntity>>()
        GlobalScope.async(Dispatchers.IO) {
            result.postValue(localDataSource.getAllBooks(query))
        }
        return result
    }

    override fun getBookByQuery(text: String): LiveData<List<BookEntity>> {
        val query = SortUtils.getBookByQuery(text)
        val result = MutableLiveData<List<BookEntity>>()
        GlobalScope.async(Dispatchers.IO) {
            result.postValue(localDataSource.getAllBooks(query))
        }
        return result
    }

    override fun getBookByTitle(text: String): List<BookEntity> {
        val query = SortUtils.getBookByQuery(text)
        val result = ArrayList<BookEntity>()
        GlobalScope.async(Dispatchers.IO) {
            result.addAll(localDataSource.getAllBooks(query))
        }
        return result
    }

    override fun getRecommendedBooks(sort: String): LiveData<List<BookEntity>> {
        val query = SortUtils.getSortedQuery(sort)
        val result = MutableLiveData<List<BookEntity>>()
        GlobalScope.async(Dispatchers.IO) {
            result.postValue(localDataSource.getAllBooks(query))
        }
        return result
    }

    override fun getBookById(bookId: Int): LiveData<BookEntity> {
        val result = MutableLiveData<BookEntity>()
        GlobalScope.async(Dispatchers.IO) {
            result.postValue(localDataSource.getBookById(bookId))
        }
        return result
    }

    override fun insertBorrowBook(borrowBookEntity: BorrowBookEntity) {
        localDataSource.insertBorrowBook(borrowBookEntity)
    }

    override fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity) {
        localDataSource.insertFavoriteBook(favoriteBookEntity)
    }

    override fun getAllFavoriteBook(userId: String): LiveData<List<BookEntity>> {
        val favoriteBooks = MutableLiveData<List<BookEntity>>()
        localDataSource.getAllFavoriteBook(userId, object : LocalDataSource.LoadFavoriteBooksCallback {
            override fun onAllFavoriteBooksReceived(favoriteBookEntity: List<FavoriteBookEntity>) {
                val bookEntities = ArrayList<BookEntity>()
                for (favoriteBook in favoriteBookEntity) {
                    val bookById = localDataSource.getBookById(favoriteBook.bookId.toInt())
                    val bookEntity = BookEntity(
                        bookById.bookId,
                        bookById.ISBN,
                        bookById.book_title,
                        bookById.book_author,
                        bookById.year_publication,
                        bookById.publisher,
                        bookById.imageUrl_s,
                        bookById.imageUrl_m,
                        bookById.imageUrl_l,
                        bookById.rating
                    )
                    bookEntities.add(bookEntity)
                }
                favoriteBooks.postValue(bookEntities)
            }
        })
        return favoriteBooks
    }

    override fun getAllBorrowBook(userId: String): LiveData<List<BookEntity>> {
       val borrowBooks = MutableLiveData<List<BookEntity>>()
        localDataSource.getAllBorrowBook(userId, object : LocalDataSource.LoadBorrowBooksCallback {
            override fun onAllBorrowBooksReceived(borrowBookEntity: List<BorrowBookEntity>) {
                val bookEntities = ArrayList<BookEntity>()
                for (borrowBook in borrowBookEntity) {
                    val bookById = localDataSource.getBookById(borrowBook.bookId.toInt())
                    val bookEntity = BookEntity(
                        bookById.bookId,
                        bookById.ISBN,
                        bookById.book_title,
                        bookById.book_author,
                        bookById.year_publication,
                        bookById.publisher,
                        bookById.imageUrl_s,
                        bookById.imageUrl_m,
                        bookById.imageUrl_l,
                        bookById.rating
                    )
                    bookEntities.add(bookEntity)
                }
                borrowBooks.postValue(bookEntities)
            }
        })
        return borrowBooks
    }
}
