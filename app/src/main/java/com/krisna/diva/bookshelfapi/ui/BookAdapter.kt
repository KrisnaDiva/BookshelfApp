package com.krisna.diva.bookshelfapi.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.krisna.diva.bookshelfapi.data.response.BooksItem
import com.krisna.diva.bookshelfapi.data.response.DeleteBookResponse
import com.krisna.diva.bookshelfapi.data.retrofit.ApiConfig
import com.krisna.diva.bookshelfapi.databinding.ItemBookBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookAdapter(private val startForResult: ActivityResultLauncher<Intent>) :
    ListAdapter<BooksItem, BookAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)

    }

    inner class MyViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BooksItem) {
            binding.tvName.text = book.name
            binding.tvPublisher.text = book.publisher

            binding.btnDelete.setOnClickListener {
                deleteBook(book, adapterPosition, binding.root.context)
            }

            binding.btnEdit.setOnClickListener {
                val moveWithDataIntent = Intent(binding.root.context, EditActivity::class.java)
                moveWithDataIntent.putExtra(EditActivity.ID, book.id)
                startForResult.launch(moveWithDataIntent)
            }

            binding.root.setOnClickListener {
                val moveWithDataIntent = Intent(binding.root.context, DetailActivity::class.java)
                moveWithDataIntent.putExtra(DetailActivity.ID, book.id)
                binding.root.context.startActivity(moveWithDataIntent)
            }
        }
    }

    companion object {
        private const val TAG = "BookAdapter"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BooksItem>() {
            override fun areItemsTheSame(oldItem: BooksItem, newItem: BooksItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BooksItem, newItem: BooksItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun deleteBook(book: BooksItem, position: Int, context: Context) {
        val client = ApiConfig.getApiService().deleteBook(book.id)
        client.enqueue(object : Callback<DeleteBookResponse> {
            override fun onResponse(
                call: Call<DeleteBookResponse>,
                response: Response<DeleteBookResponse>
            ) {
                if (response.isSuccessful) {
                    val currentList = currentList.toMutableList()
                    currentList.removeAt(position)
                    submitList(currentList)
                    Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DeleteBookResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
