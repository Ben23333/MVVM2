package com.benone.mvvm2.adapter

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.benone.mvvm2.model.Chapter
import com.benone.mvvm2.ui.fragment.ProjectArticleFragment

class ProjectViewPagerAdapter(fm:FragmentManager):FragmentStatePagerAdapter(fm) {

    private var fragments = mutableListOf<Fragment>()
    private var list = mutableListOf<Chapter>()

    override fun getCount() = fragments.size

    override fun getItem(position: Int)=fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return HtmlCompat.fromHtml(list[position].name,FROM_HTML_MODE_LEGACY).toString()
    }
    fun setData(data:List<Chapter>){
        list.run{
            clear()
            addAll(data)
            forEach {
                fragments.add(ProjectArticleFragment(it.id))
            }
        }
        notifyDataSetChanged()
    }

}