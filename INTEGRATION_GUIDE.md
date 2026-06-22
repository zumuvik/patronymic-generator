# Интеграция Python FastAPI с Android приложением

## Архитектура

```
┌──────────────────────┐       HTTP/REST        ┌──────────────────────┐
│   Python FastAPI     │ ◄────────────────────►  │   Android (Kotlin)   │
│   patronymic-core/   │    Retrofit + JSON      │     приложение       │
│   port 8000          │                         │                      │
└──────────────────────┘                         └──────────────────────┘
```

## Способ 1: Локальный сервер на ПК (для разработки)

### 1. Запуск Python сервера

```bash
cd patronymic-core/

# Установка зависимостей
pip install fastapi uvicorn pydantic

# Запуск
python server.py
# или: uvicorn server:app --host 0.0.0.0 --port 8000 --reload
```

### 2. Проверка

```bash
curl http://localhost:8000/api/health
# → {"status":"ok","service":"patronymic-generator","version":"1.0.0"}

curl http://localhost:8000/api/generate?name=Александр
# → {"father_name":"Александр","son_patronymic":"Александрович","daughter_patronymic":"Александровна"}

curl http://localhost:8000/api/names
# → {"count":45,"names":["Александр","Алексей",...]}
```

### 3. Подключение Android

**Для эмулятора Android:** `10.0.2.2` это localhost хост-машины.
Уже настроено в `PatronymicRepository.kt`:
```kotlin
const val DEFAULT_BASE_URL = "http://10.0.2.2:8000/"
```

**Для реального устройства:**
- Подключите устройство и ПК к одной Wi-Fi сети
- Найдите IP вашего ПК: `ipconfig` (Windows) или `ifconfig` (Linux/macOS)
- Измените URL в `PatronymicRepository.kt`:
```kotlin
const val DEFAULT_BASE_URL = "http://192.168.1.100:8000/"
```

## Способ 2: Python скрипт внутри Android (Chaquopy)

Если не хотите запускать отдельный сервер, можно встроить Python прямо в APK.

### Gradle (app/build.gradle.kts):
```kotlin
plugins {
    id("com.chaquo.python") version "15.0.1"
}

android {
    defaultConfig {
        python {
            buildPython("/usr/bin/python3")
            pip {
                install("")
            }
        }
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
    }
}
```

### Вызов из Kotlin:
```kotlin
import com.chaquo.python.Python

fun generateLocal(name: String): PatronymicResult {
    val py = Python.getInstance()
    val module = py.getModule("generator")
    val result = module.callAttr("generate_patronymic", name)
    return PatronymicResult(
        fatherName = result.get("father_name").toString(),
        sonPatronymic = result.get("son_patronymic").toString(),
        daughterPatronymic = result.get("daughter_patronymic").toString(),
    )
}
```

⚠️ **Минусы Chaquopy:** APK увеличивается на ~30MB, медленный импорт.

## Способ 3: FastAPI на VPS / Cloud (продакшн)

### 1. Деплой Python сервера

**Через Docker:**
```dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
CMD ["uvicorn", "server:app", "--host", "0.0.0.0", "--port", "8000"]
```

**На Railway / Render / Fly.io:**
```bash
# Railway: подключить репозиторий, стартовая команда:
uvicorn server:app --host 0.0.0.0 --port $PORT
```

### 2. Android → Production URL
```kotlin
// PatronymicRepository.kt
const val PROD_BASE_URL = "https://your-app.railway.app/"
```

## Как это работает (сеть)

```
Android (Retrofit)                    Python (FastAPI)
       │                                    │
       │  GET /api/generate?name=Александр   │
       │──────────────────────────────────►  │
       │                                    │
       │  {                                 │
       │    "father_name": "Александр",      │  generate_patronymic("Александр")
       │    "son_patronymic": "Александрович"│  → {"son": "Александрович", ...}
       │    "daughter_patronymic": "Александровна" │
       │  }                                 │
       │◄──────────────────────────────────  │
       │                                    │
       │  ViewModel ← Result                │
       │  MainScreen ← StateFlow            │
       │  ResultCard отображает             │
       │                                    │
```

## Endpoints API

| Метод | Путь | Параметры | Ответ |
|-------|------|-----------|-------|
| GET | `/api/health` | — | `{"status": "ok", "version": "1.0.0"}` |
| GET | `/api/generate` | `?name=Александр` | `{"father_name": "...", "son_patronymic": "...", "daughter_patronymic": "..."}` |
| GET | `/api/names` | — | `{"count": 45, "names": ["Александр", ...]}` |

## Требования к сети (Android)

**В манифесте** уже прописан INTERNET:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

**Для HTTP (не HTTPS) на Android 9+** нужно в `res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
```

И в манифест:
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ...>
```

## Оффлайн-режим (рекомендация)

Для production добавьте локальную копию Python-логики через файл правил:

```kotlin
// LocalPatronymicGenerator.kt — дублирует Python логику на Kotlin
object LocalPatronymicGenerator {
    private val exceptions = mapOf(
        "Никита" to ("Никитич" to "Никитична"),
        // ... полный список
    )
    
    fun generate(name: String): PatronymicResult {
        // Та же логика, что и в Python
    }
}
```

Используйте: сначала пробуем API, при ошибке → локальная генерация.
