package com.ervin.mypokedex.utils

class Response<T> (val status:Status, val message:String?, val data:T? ){
    companion object{
        fun <T>success(data:T):Response<T>{
            return Response(Status.SUCCESS,null, data)
        }

        fun <T>fail(message:String):Response<T>{
            return Response(Status.ERROR, message, null)
        }

        fun <T>loading():Response<T>{
            return Response(Status.LOADING,null, null)
        }
    }
}