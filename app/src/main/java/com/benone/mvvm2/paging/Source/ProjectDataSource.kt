package com.benone.mvvm2.paging.Source

import com.benone.mvvm2.base.paging.BaseItemKeyedDataSource
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.model.Article
import com.benone.mvvm2.utils.asyncSubscribe

class ProjectDataSource(private val httpManager: HttpManager, private val projectId: Int) :
    BaseItemKeyedDataSource<Article>() {

    var pageNo = 1
    override fun setKey(item: Article): Int {
        return item.id
    }

    override fun onLoadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        httpManager.wanApi.getProjectArticles(pageNo, projectId).asyncSubscribe({
            pageNo += 1
            loadMoreSuccess()
            callback.onResult(it.data?.datas!!)
        }, {
            loadMoreFailed(it.message, params, callback)
        }
        )
    }

    override fun onLoadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        httpManager.wanApi.getProjectArticles(pageNo, projectId).asyncSubscribe({
            pageNo += 1
            refreshSuccess(it.data?.datas!!.isEmpty())
            callback.onResult(it.data?.datas!!)
        }, {
            refreshFailed(it.message, params, callback)
        }
        )
    }

}