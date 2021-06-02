package com.benone.mvvm2.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.benone.mvvm2.R
import com.benone.mvvm2.base.BaseFragment
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.paging.repository.ProjectArticleRepository
import com.benone.mvvm2.viewmodel.ProjectArticleViewModel

class ProjectArticleFragment(private val projectId:Int):BaseFragment() {

    private val viewModel by lazy{
        ViewModelProviders.of(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repository = ProjectArticleRepository(HttpManager.getInstance(),projectId)
                return ProjectArticleViewModel(repository) as T
            }

        })
            .get(ProjectArticleViewModel::class.java)
    }

    private val adapter by lazy {
        HomeAdapter{viewModel.retry()}
    }

    override var layoutId: Int= R.layout.refresh_layout

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun subscribeUi() {
        TODO("Not yet implemented")
    }

}