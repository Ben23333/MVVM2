package com.benone.mvvm2.base.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

abstract class BaseDataSourceFactory<T>: DataSource.Factory<Int,T>() {

    val sourceLiveData = MutableLiveData<BaseItemKeyedDataSource<T>>()

    override fun create(): BaseItemKeyedDataSource<T> {
        val dataSource:BaseItemKeyedDataSource<T> = createDataSource()
        sourceLiveData.postValue(dataSource)
        return dataSource
    }

    abstract fun createDataSource():BaseItemKeyedDataSource<T>
}