package me.spanchenko

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import me.spanchenko.dto.Dto
import me.spanchenko.dto.ResponseData
import me.spanchenko.dto.toResponseData

@ExperimentalSerializationApi
inline fun <reified T : Dto> T.toRequestString() = Json.encodeToString(
    serializer(T::class.java),
    this
)

@ExperimentalSerializationApi
inline fun <reified T : Dto> T.toResponseString() = Json.encodeToString(
    ResponseData.serializer(serializer(T::class.java)),
    this.toResponseData() as ResponseData<Any>
)

@ExperimentalSerializationApi
inline fun <reified T : Dto> List<T>.toResponseListString() = Json.encodeToString(
    ResponseData.serializer(ListSerializer(serializer(T::class.java))),
    this.toResponseData() as ResponseData<List<Any>>
)
