package me.spanchenko.dto

import kotlinx.serialization.Serializable

@Serializable
class ResponseData<T>(val data: T)

