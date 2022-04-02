package com.example.sabbartask.data.remote.entities

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

    @field:SerializedName("success")
    val success: Boolean? = false

    @field:SerializedName("message")
    val message: String? = ""

    @field:SerializedName("msg")
    val msg: String? = ""

    @field:SerializedName("errors")
    val errors: Map<String, List<String>>? = null

    @field:SerializedName("data")
    val data: T? = null

}