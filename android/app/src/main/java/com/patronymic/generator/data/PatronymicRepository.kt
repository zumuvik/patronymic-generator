package com.patronymic.generator.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Репозиторий для работы с Python FastAPI бэкендом генерации отчеств.
 */
class PatronymicRepository(private val baseUrl: String = DEFAULT_BASE_URL) {

    private val api: PatronymicApi by lazy {
        createApi()
    }

    private fun createApi(): PatronymicApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PatronymicApi::class.java)
    }

    /**
     * Генерирует отчество по имени отца.
     */
    suspend fun generatePatronymic(name: String): Result<PatronymicResult> {
        return try {
            val result = api.generate(name)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Получает список всех поддерживаемых имён.
     */
    suspend fun getSupportedNames(): Result<List<String>> {
        return try {
            val response = api.getNames()
            Result.success(response.names)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Проверка соединения с сервером.
     */
    suspend fun checkHealth(): Result<Boolean> {
        return try {
            val health = api.health()
            Result.success(health["status"] == "ok")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        // Для Android эмулятора localhost — 10.0.2.2
        // Для реального устройства — IP вашего компьютера в локальной сети
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8000/"
    }
}
