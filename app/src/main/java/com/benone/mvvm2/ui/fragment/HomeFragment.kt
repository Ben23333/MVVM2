package com.benone.mvvm2.ui.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.benone.mvvm2.R
import com.benone.mvvm2.adapter.HomeAdapter
import com.benone.mvvm2.base.BaseFragment
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.paging.repository.HomeRepository
import com.benone.mvvm2.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.refresh_layout.*

class HomeFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this,object:ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repositoty = HomeRepository(HttpManager.getInstance())
                return HomeViewModel(repositoty) as T
            }

        })
            .get(HomeViewModel::class.java)
    }

    private val adapter by lazy {
        HomeAdapter{viewModel.retry()}
    }

    override var layoutId = R.layout.refresh_layout

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

        }
    }

}