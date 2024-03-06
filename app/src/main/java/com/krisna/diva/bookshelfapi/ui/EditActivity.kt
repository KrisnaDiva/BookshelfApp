package com.krisna.diva.bookshelfapi.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.krisna.diva.bookshelfapi.data.response.EditBookResponse
import com.krisna.diva.bookshelfapi.data.response.GetBookResponse
import com.krisna.diva.bookshelfapi.data.retrofit.ApiConfig
import com.krisna.diva.bookshelfapi.databinding.ActivityEditBinding
import com.krisna.diva.bookshelfapi.utils.FormValidator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    companion object {
        const val ID = "id"
        private const val TAG = "EditActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBook()
        binding.btnEdit.setOnClickListener {
            if (FormValidator.validateEditTexts(
                    binding.etName,
                    binding.etYear,
                    binding.etAuthor,
                    binding.etSummary,
                    binding.etPublisher,
                    binding.etPageCount,
                    binding.etReadPage
                )
            ) {
                editBook(
                    binding.etName.text.toString(),
                    binding.etYear.text.toString().toInt(),
                    binding.etAuthor.text.toString(),
                    binding.etSummary.text.toString(),
                    binding.etPublisher.text.toString(),
                    binding.etPageCount.text.toString().toInt(),
                    binding.etReadPage.text.toString().toInt(),
                    binding.cbReading.isChecked
                )
            }
        }
    }


    private fun getBook() {
        val bookId = intent.getStringExtra(DetailActivity.ID)
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
                            binding.etName.setText(book.name)
                            binding.etYear.setText(book.year.toString())
                            binding.etAuthor.setText(book.author)
                            binding.etSummary.setText(book.summary)
                            binding.etPublisher.setText(book.publisher)
                            binding.etPageCount.setText(book.pageCount.toString())
                            binding.etReadPage.setText(book.readPage.toString())
                            binding.cbReading.isChecked = book.reading
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

    private fun editBook(
        name: String,
        year: Int,
        author: String,
        summary: String,
        publisher: String,
        pageCount: Int,
        readPage: Int,
        reading: Boolean
    ) {
        if (readPage > pageCount) {
            Toast.makeText(
                this@EditActivity,
                "read pages cannot be greater than total pages",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val bookId = intent.getStringExtra(DetailActivity.ID)
        if (bookId != null) {
            val client = ApiConfig.getApiService()
                .editBook(
                    bookId,
                    name,
                    year,
                    author,
                    summary,
                    publisher,
                    pageCount,
                    readPage,
                    reading
                )
            client.enqueue(object : Callback<EditBookResponse> {
                override fun onResponse(
                    call: Call<EditBookResponse>,
                    response: Response<EditBookResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        val returnIntent = Intent()
                        returnIntent.putExtra(MainActivity.RESULT, "Book edited successfully")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EditBookResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

}