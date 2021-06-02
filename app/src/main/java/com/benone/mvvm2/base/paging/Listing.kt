package com.benone.mvvm2.base.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.benone.mvvm2.base.RequestState

data class Listing<T> (

    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<RequestState<Boolean>>,
    val refreshState: LiveData<RequestState<Boolean>>,
    val refresh:()->Unit,
    val retry:()->Unit
)