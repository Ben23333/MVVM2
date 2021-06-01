package com.benone.mvvm2.base

import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<T : ViewDataBinding> : BaseFragment() {

    override var layoutId = 0

    protected abstract var binding: T
}