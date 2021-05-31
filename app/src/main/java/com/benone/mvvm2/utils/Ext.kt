package com.benone.mvvm2.utils

import com.benone.mvvm2.data.http.RxHttpObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.async():Observable<T>{
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.asyncSubscribe(onNext:(T)->Unit,onError:(Throwable)->Unit){
    this.async().subscribe(object :RxHttpObserver<T>(){
        override fun onNext(t: T) {
            super.onNext(t)
            onNext(t)
        }

        override fun onError(e: Throwable) {
            super.onError(e)
            onError(e)
        }
    })
}