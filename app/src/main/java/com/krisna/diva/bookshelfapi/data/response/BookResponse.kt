package com.krisna.diva.bookshelfapi.data.response

import com.google.gson.annotations.SerializedName

data class BookResponse(
	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class BooksItem(
	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("publisher")
	val publisher: String,

	@field:SerializedName("id")
	val id: String
)

data class Data(
	@field:SerializedName("books")
	val books: List<BooksItem>
)
//
data class AddBookResponse(
	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: BookID
)
data class BookID(
	@field:SerializedName("bookId")
	val bookId: String
)
//
data class DeleteBookResponse(
	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("message")
	val message: String,
)
//
data class GetBookResponse(

	@field:SerializedName("data")
	val data: BookData,

	@field:SerializedName("status")
	val status: String
)

data class BookData(
	@field:SerializedName("book")
	val book: BookDetails
)

data class BookDetails(
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("year")
	val year: Int,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("summary")
	val summary: String,

	@field:SerializedName("publisher")
	val publisher: String,

	@field:SerializedName("pageCount")
	val pageCount: Int,

	@field:SerializedName("readPage")
	val readPage: Int,

	@field:SerializedName("finished")
	val finished: Boolean,

	@field:SerializedName("reading")
	val reading: Boolean,

	@field:SerializedName("insertedAt")
	val insertedAt: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
//
data class EditBookResponse(
	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("message")
	val message: String,
)
//
data class ErrorResponse(
	@field:SerializedName("status")
    val status: String,

	@field:SerializedName("message")
    val message: String
)
