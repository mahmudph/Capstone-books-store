/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.myone.core.R
import id.myone.core.databinding.ItemGridBookBinding
import id.myone.core.domain.entity.Book

class BookListAdapter : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    private var onClickItemBookList: OnClickItemBookList? = null
    private var listBooks = ArrayList<Book>()

    interface OnClickItemBookList {
        fun onPressItem(bookId: String)
    }

    fun setOnClickListener(callback: OnClickItemBookList) {
        onClickItemBookList = callback
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(books: List<Book>) {
        listBooks.clear()
        listBooks.addAll(books)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemGridBookBinding.bind(itemView)

        fun bind(book: Book) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(book.image)
                    .fitCenter()
                    .into(ivItemImage)

                title.text = book.title
                price.text = book.price
            }

            itemView.setOnClickListener {
                onClickItemBookList?.onPressItem(book.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_grid_book, parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = listBooks[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int = listBooks.size
}