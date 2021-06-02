package com.benone.mvvm2.viewmodel

import com.benone.mvvm2.base.paging.BasePagingViewModel
import com.benone.mvvm2.model.Article
import com.benone.mvvm2.paging.repository.ProjectArticleRepository

class ProjectArticleViewModel(repository: ProjectArticleRepository): BasePagingViewModel<Article>(repository) {
}