package com.benone.mvvm2.paging.factory

import com.benone.mvvm2.base.paging.BaseDataSourceFactory
import com.benone.mvvm2.base.paging.BaseItemKeyedDataSource
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.model.Article
import com.benone.mvvm2.paging.Source.HomeDataSource

class HomeDataSourceFactory(private val httpManager: HttpManager):BaseDataSourceFactory<Article>() {

    override fun createDataSource(): BaseItemKeyedDataSource<Article> {
        return HomeDataSource(httpManager)
    }
}