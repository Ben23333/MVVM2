package com.benone.mvvm2.utils

object Listeners {

    interface PermissionListener {
        fun onGranted()

        fun onDenied(permissions: List<String>)

        fun onShowReason()
    }
}