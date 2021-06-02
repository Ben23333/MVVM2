package com.benone.mvvm2.paging.repository

import com.benone.mvvm2.base.paging.BaseDataSourceFactory
import com.benone.mvvm2.base.paging.BasePagingRepository
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.model.Article
import com.benone.mvvm2.paging.factory.ProjectDataSourceFactory

class ProjectArticleRepository(private val httpManager: HttpManager,private val projectId:Int):
    BasePagingRepository<Article>(){

    override fun createDataBaseFactory(): BaseDataSourceFactory<Article> {
        return ProjectDataSourceFactory(httpManager,projectId)
    }

}