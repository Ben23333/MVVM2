package com.benone.mvvm2.ui.fragment

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.benone.mvvm2.R
import com.benone.mvvm2.adapter.HomeAdapter
import com.benone.mvvm2.base.BaseFragment
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.paging.repository.ProjectArticleRepository
import com.benone.mvvm2.viewmodel.ProjectArticleViewModel
import kotlinx.android.synthetic.main.refresh_layout.*

class ProjectArticleFragment(private val projectId: Int) : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repository = ProjectArticleRepository(HttpManager.getInstance(), projectId)
                return ProjectArticleViewModel(repository) as T
            }

        })
            .get(ProjectArticleViewModel::class.java)
    }

    private val adapter by lazy {
        HomeAdapter { viewModel.retry() }
    }

    override var layoutId: Int = R.layout.refresh_layout

    override fun initData() {
        multipleStatusView = multiple_status_view
        initSwipe()
        initRecyclerView()
    }

    override fun subscribeUi() {
        viewModel.run {
            pagedList.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
            refreshState.observe(viewLifecycleOwner, Observer {
                swipeRefreshLayout.isRefreshing = false
                when {
                    it.isLoading()->if(isRefreshFromPull){
                        swipeRefreshLayout.isRefreshing = true
                        isRefreshFromPull = false
                    }else{
                        multipleStatusView?.showLoading()
                    }
                    it.isSuccess()->if(it.data!!){
                        multipleStatusView?.showEmpty()
                    }else{
                        multipleStatusView?.showContent()
                    }
                    it.isError()->multipleStatusView?.showError()
                }
            })
            networkState.observe(viewLifecycleOwner, Observer {
                adapter.setRequestState(it)
            })
            initLoad()
        }
    }

    override fun onRetry() {
        viewModel.refresh()
    }

    private fun initSwipe(){
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE)
        swipeRefreshLayout.setOnRefreshListener {
            isRefreshFromPull = true
            viewModel.refresh()
        }
    }

    private fun initRecyclerView(){
        recycleView.adapter =adapter
    }

}