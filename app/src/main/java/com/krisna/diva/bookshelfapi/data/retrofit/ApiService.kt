package com.krisna.diva.bookshelfapi.data.retrofit

import com.krisna.diva.bookshelfapi.data.response.AddBookResponse
import com.krisna.diva.bookshelfapi.data.response.BookResponse
import com.krisna.diva.bookshelfapi.data.response.DeleteBookResponse
import com.krisna.diva.bookshelfapi.data.response.EditBookResponse
import com.krisna.diva.bookshelfapi.data.response.GetBookResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("books")
    fun getAllBooks(): Call<BookResponse>

    @FormUrlEncoded
    @POST("books")
    fun addBook(
        @Field("name") name: String,
        @Field("year") year: Int,
        @Field("author") author: String,
        @Field("summary") summary: String,
        @Field("publisher") publisher: String,
        @Field("pageCount") pageCount: Int,
        @Field("readPage") readPage: Int,
        @Field("reading") reading: Boolean
    ): Call<AddBookResponse>

    @FormUrlEncoded
    @PUT("books/{id}")
    fun editBook(
        @Path("id") id: String,
        @Field("name") name: String,
        @Field("year") year: Int,
        @Field("author") author: String,
        @Field("summary") summary: String,
        @Field("publisher") publisher: String,
        @Field("pageCount") pageCount: Int,
        @Field("readPage") readPage: Int,
        @Field("reading") reading: Boolean
    ): Call<EditBookResponse>

    @DELETE("books/{id}")
    fun deleteBook(
        @Path("id") id: String
    ): Call<DeleteBookResponse>

    @GET("books/{id}")
    fun getBook(
        @Path("id") id: String
    ): Call<GetBookResponse>
}
