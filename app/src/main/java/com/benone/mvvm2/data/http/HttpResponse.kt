package com.benone.mvvm2.data.http

class HttpResponse<T> (
    var data:T?,
    var errorCode:Int,
    var errorMsg:String?

)