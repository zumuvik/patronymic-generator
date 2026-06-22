package com.patronymic.generator.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patronymic.generator.data.Gender
import com.patronymic.generator.data.LocalPatronymicGenerator
import com.patronymic.generator.data.PatronymicResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel главного экрана генератора отчеств.
 * Использует [LocalPatronymicGenerator] — никаких сетевых запросов,
 * всё работает офлайн прямо на устройстве.
 */
class MainViewModel : ViewModel() {

    private val _fatherName = MutableStateFlow("")
    val fatherName: StateFlow<String> = _fatherName.asStateFlow()

    private val _selectedGender = MutableStateFlow(Gender.BOTH)
    val selectedGender: StateFlow<Gender> = _selectedGender.asStateFlow()

    private val _result = MutableStateFlow<PatronymicResult?>(null)
    val result: StateFlow<PatronymicResult?> = _result.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _explosionTrigger = MutableStateFlow(false)
    val explosionTrigger: StateFlow<Boolean> = _explosionTrigger.asStateFlow()

    private val _explosionOriginX = MutableStateFlow(0f)
    val explosionOriginX: StateFlow<Float> = _explosionOriginX.asStateFlow()

    private val _explosionOriginY = MutableStateFlow(0f)
    val explosionOriginY: StateFlow<Float> = _explosionOriginY.asStateFlow()

    fun updateFatherName(name: String) {
        _fatherName.value = name
        _result.value = null
        _error.value = null
    }

    fun updateGender(gender: Gender) {
        _selectedGender.value = gender
    }

    fun generate(x: Float = 0f, y: Float = 0f) {
        val name = _fatherName.value.trim()
        if (name.isEmpty()) {
            _error.value = "Введите имя отца"
            return
        }

        _explosionOriginX.value = x
        _explosionOriginY.value = y
        _explosionTrigger.value = true

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _result.value = null

            // Небольшая задержка, чтобы взрыв частиц успел проиграться
            delay(300)

            val result = LocalPatronymicGenerator.generate(name)
            if (result != null) {
                _result.value = result
            } else {
                _error.value = "Имя «$name» не поддерживается"
            }

            _isLoading.value = false
            _explosionTrigger.value = false
        }
    }

    fun dismissExplosion() {
        _explosionTrigger.value = false
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("patronymic", text))
    }

    fun clearError() {
        _error.value = null
    }
}
