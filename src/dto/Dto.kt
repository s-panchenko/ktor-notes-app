package me.spanchenko.dto

interface Dto

fun <T : Dto> T.toResponseData() = ResponseData(data = this)

fun <T : Dto> List<T>.toResponseData() = ResponseData(data = this)
