package com.benone.mvvm2.base.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.benone.mvvm2.base.RequestState
import com.benone.mvvm2.utils.ExecutorUtils

abstract class BaseItemKeyedDataSource<T>: ItemKeyedDataSource<Int, T>() {

    private var retry:(()->Any)?=null
    private var retryExecutor = ExecutorUtils.NETWORK_IO

    val loadMoreStatus by lazy{
        MutableLiveData<RequestState<Boolean>>()
    }

    val refreshStatus by lazy{
        MutableLiveData<RequestState<Boolean>>()
    }

    fun retryFailed(){
        val preRetry = retry
        retry= null
        preRetry.let{
            retryExecutor.execute {
                it?.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<T>) {
        refreshStatus.postValue(RequestState.loading())
        onLoadInitial(params,callback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<T>) {
        loadMoreStatus.postValue(RequestState.loading())
        onLoadAfter(params,callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<T>) {
    }

    fun refreshSuccess(isDataEmpty:Boolean = false){
        refreshStatus.postValue(RequestState.success(isDataEmpty))
        retry=null
    }

    fun loadMoreSuccess(){
        retry = null
        loadMoreStatus.postValue(RequestState.success())
    }

    fun loadMoreFailed(msg:String?,params:LoadParams<Int>,callback:LoadCallback<T>){
        loadMoreStatus.postValue(RequestState.error())
        retry = {
            loadAfter(params,callback)
        }
    }

    fun refreshFailed(msg:String?,params:LoadInitialParams<Int>,calback:LoadInitialCallback<T>){
        refreshStatus.postValue(RequestState.error())
        retry = {
            loadInitial(params,calback)
        }
    }

    override fun getKey(item: T): Int {
        return setKey(item)
    }

    abstract fun setKey(item:T):Int

    abstract fun onLoadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<T>)
    abstract fun onLoadAfter(params: LoadParams<Int>, callback: LoadCallback<T>)
}