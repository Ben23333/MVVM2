package com.benone.mvvm2.viewmodel

import androidx.lifecycle.ViewModel
import com.benone.mvvm2.repository.ProjectRepository

class ProjectViewModel(private val repository: ProjectRepository):ViewModel() {

    val projects= repository.getProjects()
}