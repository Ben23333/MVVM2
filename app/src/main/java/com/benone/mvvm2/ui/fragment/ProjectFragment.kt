package com.benone.mvvm2.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.benone.mvvm2.R
import com.benone.mvvm2.adapter.ProjectViewPagerAdapter
import com.benone.mvvm2.base.BaseFragment
import com.benone.mvvm2.data.http.HttpManager
import com.benone.mvvm2.repository.ProjectRepository
import com.benone.mvvm2.viewmodel.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_tab_vp.*
import com.google.android.material.tabs.TabLayout

class ProjectFragment: BaseFragment() {

    private val viewModel by lazy{
        ViewModelProviders.of(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repository = ProjectRepository(HttpManager.getInstance())
                return ProjectViewModel(repository) as T
            }

        })
    }

    private val adapter by lazy{
        ProjectViewPagerAdapter(childFragmentManager)
    }

    override var layoutId = R.layout.fragment_tab_vp

    override fun initData() {
        multipleStatusView = multiple_status_view
        viewPager.adapter = adapter
        (tabLayout as TabLayout).run{
            setupWithViewPager(viewPager)
        }
    }

    override fun subscribeUi() {
        TODO("Not yet implemented")
    }

}