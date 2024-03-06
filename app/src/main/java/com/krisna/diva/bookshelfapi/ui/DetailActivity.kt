package com.krisna.diva.bookshelfapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.krisna.diva.bookshelfapi.data.response.GetBookResponse
import com.krisna.diva.bookshelfapi.data.retrofit.ApiConfig
import com.krisna.diva.bookshelfapi.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val ID = "id"
        private const val TAG = "DetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBook()
    }

    private fun getBook() {
        val bookId = intent.getStringExtra(ID)
        if (bookId != null) {
            val client = ApiConfig.getApiService().getBook(bookId)
            client.enqueue(object : Callback<GetBookResponse> {
                override fun onResponse(
                    call: Call<GetBookResponse>,
                    response: Response<GetBookResponse>
                ) {
                    if (response.isSuccessful) {
                        val book = response.body()?.data?.book
                        if (book != null) {
                            binding.tvId.text = book.id
                            binding.tvName.text = book.name
                            binding.tvYear.text = book.year.toString()
                            binding.tvAuthor.text = book.author
                            binding.tvSummary.text = book.summary
                            binding.tvPublisher.text = book.publisher
                            binding.tvPageCount.text = book.pageCount.toString()
                            binding.tvReadPage.text = book.readPage.toString()
                            binding.tvFinished.text = book.finished.toString()
                            binding.tvReading.text = book.reading.toString()
                            binding.tvInsertedAt.text = book.insertedAt
                            binding.tvUpdatedAt.text = book.updatedAt
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GetBookResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
}