package com.krisna.diva.bookshelfapi.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.krisna.diva.bookshelfapi.data.response.BookResponse
import com.krisna.diva.bookshelfapi.data.response.BooksItem
import com.krisna.diva.bookshelfapi.data.retrofit.ApiConfig
import com.krisna.diva.bookshelfapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        const val RESULT = "result"
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                getAllBooks()
                val message = it.data?.getStringExtra(RESULT)
                if (message != null) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide() //Ini menyembunyikan ActionBar jika ada.

        val layoutManager = LinearLayoutManager(this)
        binding.rvBook.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)// menambahkan pembatas antara item di RecyclerView.
        binding.rvBook.addItemDecoration(itemDecoration)

        getAllBooks()
//        membuka addActivity
        binding.btnCreate.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startForResult.launch(intent)
        }
    }


    private fun getAllBooks() {
        showLoading(true)
        val client = ApiConfig.getApiService().getAllBooks()
        client.enqueue(object : Callback<BookResponse> {
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setReviewData(responseBody.data.books)
                    }
                } else {
//                    val message = response.errorBody()?.string()
//                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                showLoading(false)
//                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setReviewData(bookList: List<BooksItem>) {
        val adapter = BookAdapter(startForResult)
        adapter.submitList(bookList)
        binding.rvBook.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}