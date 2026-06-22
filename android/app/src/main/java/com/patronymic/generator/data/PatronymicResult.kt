package com.patronymic.generator.data

import com.google.gson.annotations.SerializedName

/**
 * Результат генерации отчества от FastAPI сервера.
 */
data class PatronymicResult(
    @SerializedName("father_name")
    val fatherName: String,

    @SerializedName("son_patronymic")
    val sonPatronymic: String,

    @SerializedName("daughter_patronymic")
    val daughterPatronymic: String
)

/**
 * Ответ от /api/names endpoint.
 */
data class NamesResponse(
    val count: Int,
    val names: List<String>
)

/**
 * Пол для генерации.
 */
enum class Gender(val displayName: String) {
    SON("Сын"),
    DAUGHTER("Дочь"),
    BOTH("Оба варианта")
}
