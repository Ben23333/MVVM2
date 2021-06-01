package com.benone.mvvm2.base

import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<T : ViewDataBinding> : BaseActivity() {

    override var layoutId = 0

    protected abstract var binding: T
}