package com.benone.mvvm2.adapter

import androidx.recyclerview.widget.DiffUtil
import com.benone.mvvm2.model.Article

class HomeAdapter(retryCallback:()->Unit):BasePagingAdapter<Article>(diffCallback,retryCallback) {

    private val TYPE_BANNER = 10

    companion object{
        val diffCallback = object :DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}