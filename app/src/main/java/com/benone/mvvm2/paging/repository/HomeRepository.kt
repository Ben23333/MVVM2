package com.benone.mvvm2.paging.repository

import com.benone.mvvm2.base.paging.BaseDataSourceFactory
import com.benone.mvvm2.base.paging.BasePagingRepository
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.model.Article
import com.benone.mvvm2.paging.factory.HomeDataSourceFactory

class HomeRepository(private val httpManager: HttpManager):BasePagingRepository<Article>() {

    override fun createDataBaseFactory(): BaseDataSourceFactory<Article> {
        return HomeDataSourceFactory(httpManager)
    }
}