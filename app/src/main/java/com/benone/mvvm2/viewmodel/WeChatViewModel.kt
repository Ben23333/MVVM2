package com.benone.mvvm2.viewmodel

import androidx.lifecycle.ViewModel
import com.benone.mvvm2.repository.WeChatRepository

class WeChatViewModel(private val repository: WeChatRepository): ViewModel() {

    val wxChapters = repository.getWXChapters()
}