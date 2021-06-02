package com.benone.mvvm2.base.paging

import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.toLiveData

abstract class BasePagingRepository<T> {

    fun getData(pageSize: Int): Listing<T> {
        val sourceFactory = createDataBaseFactory()
        val pagedList = sourceFactory.toLiveData(
            config = Config(
                pageSize = pageSize,
                enablePlaceholders = false,
                initialLoadSizeHint = pageSize * 2
            )
        )
        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.refreshStatus
        }
        val networkStatus = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.loadMoreStatus
        }

        return Listing(
            pagedList,
            networkStatus,
            refreshState,
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
            retry = { sourceFactory.sourceLiveData.value?.retryFailed() })
    }

    abstract fun createDataBaseFactory(): BaseDataSourceFactory<T>
}