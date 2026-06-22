package com.patronymic.generator.data

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit интерфейс для Python FastAPI бэкенда.
 */
interface PatronymicApi {

    @GET("api/generate")
    suspend fun generate(
        @Query("name") name: String
    ): PatronymicResult

    @GET("api/names")
    suspend fun getNames(): NamesResponse

    @GET("api/health")
    suspend fun health(): Map<String, String>
}
