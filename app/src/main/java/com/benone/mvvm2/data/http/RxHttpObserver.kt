package com.benone.mvvm2.data.http

import com.benone.mvvm2.R
import com.benone.mvvm2.WanApplication
import com.benone.mvvm2.utils.ExecutorUtils
import com.benone.mvvm2.utils.NetUtils
import com.benone.mvvm2.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.RuntimeException

abstract class RxHttpObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {
        if (!NetUtils.isConnected(WanApplication.instance)) {
            onError(RuntimeException(WanApplication.instance.getString(R.string.network_error)))
        }
    }

    override fun onError(e: Throwable) {
        e.message?.let {
            ExecutorUtils.main_thread(Runnable { ToastUtils.show(it) })
        }
    }

    override fun onNext(t: T) {
        //业务失败
        val result = t as? HttpResponse<*>
        if (result?.errorCode != 0) {
            onError(
                RuntimeException(
                    if (result?.errorMsg.isNullOrBlank()) WanApplication.instance.getString(R.string.business_error) else {
                        result?.errorMsg
                    }
                )
            )
        }
    }

    override fun onComplete() {

    }
}