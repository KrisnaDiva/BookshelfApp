package com.krisna.diva.bookshelfapi.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.krisna.diva.bookshelfapi.data.response.AddBookResponse
import com.krisna.diva.bookshelfapi.data.retrofit.ApiConfig
import com.krisna.diva.bookshelfapi.databinding.ActivityAddBinding
import com.krisna.diva.bookshelfapi.utils.FormValidator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    companion object {
        private const val TAG = "AddActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
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
                addBook(
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



    private fun addBook(
        name: String,
        year: Int,
        author: String,
        summary: String,
        publisher: String,
        pageCount: Int,
        readPage: Int,
        reading: Boolean
    ) {
        if (readPage > pageCount){
            Toast.makeText(this@AddActivity, "read pages cannot be greater than total pages", Toast.LENGTH_SHORT).show()
            return
        }
        val client = ApiConfig.getApiService()
            .addBook(name, year, author, summary, publisher, pageCount, readPage, reading)
        client.enqueue(object : Callback<AddBookResponse> {
            override fun onResponse(
                call: Call<AddBookResponse>,
                response: Response<AddBookResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val returnIntent = Intent()
                        returnIntent.putExtra(MainActivity.RESULT, "Book added successfully")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
//                    val gson = Gson()
//                    val errorResponse: ErrorResponse =
//                        gson.fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
//                    val message = errorResponse.message
//                    Toast.makeText(this@AddActivity, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddBookResponse>, t: Throwable) {
//                Toast.makeText(this@AddActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}