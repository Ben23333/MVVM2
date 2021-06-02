package com.benone.mvvm2.base.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.benone.mvvm2.R
import com.benone.mvvm2.base.RequestState

abstract class BasePagingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val retryCallback: () -> Unit
) : PagedListAdapter<T, BasePagingAdapter.ViewHolder>(diffCallback) {

    private val TYPE_ITEM = 0
    private val TYPE_FOOTER = 1
    private var requestState: RequestState<Any>? = null
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return when(viewType){
            TYPE_ITEM->ViewHolder(LayoutInflater.from(context).inflate(getItemLayout(),parent,false))
            TYPE_FOOTER->FooterViewHolder.create(parent,retryCallback)
        }
    }

    open class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val map = mutableMapOf<Int, View>()

        fun getView(id: Int): View? {
            var view = map[id]
            if (view == null) {
                view = this.view.findViewById(id)
                map[id] = view
            }
            return view
        }

        fun setText(id: Int, string: String?) {
            val textView = getView(id)
            if (textView is TextView) {
                textView.text = string
            }
        }

        fun toVisibility(id: Int, constraint: Boolean) {
            getView(id)?.visibility = if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    class FooterViewHolder(view:View,private val retryCallback: () -> Unit):ViewHolder(view){

        init{
            getView(R.id.retry_button)?.setOnClickListener {
                retryCallback()
            }
        }

        fun bindTo(requestState:RequestState<Any>?){
            toVisibility(R.id.progress_bar,requestState!!.isLoading())
            toVisibility(R.id.retry_button,requestState.isERROR())
            toVisibility(R.id.msg,requestState.isLoading())
            setText(R.id.msg,"加载中")
        }
    }

    abstract fun getItemLayout(): Int
}