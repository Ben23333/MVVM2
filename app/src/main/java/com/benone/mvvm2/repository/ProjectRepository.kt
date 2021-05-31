package com.benone.mvvm2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benone.mvvm2.base.RequestState
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.model.Chapter
import com.benone.mvvm2.utils.asyncSubscribe

class ProjectRepository(private val httpManager: HttpManager) {

    fun getProjects(): LiveData<RequestState<List<Chapter>>> {
        val liveData = MutableLiveData<RequestState<List<Chapter>>>()
        liveData.value = RequestState.loading()
        httpManager.wanApi.getProjects().asyncSubscribe({
            liveData.postValue(RequestState.success(it.data))
        }, {
            liveData.postValue(RequestState.error(it.message))
        })
        return liveData
    }
}