# Patronymic Generator · Генератор Отчеств

**Android-приложение** (Kotlin + Jetpack Compose) с **Python-бэкендом** (FastAPI) для генерации правильных русских отчеств. Стеклянный дизайн, летающие частицы, неоновая эстетика.

<p float="left">
  <img src="https://img.shields.io/badge/Python-3.11+-blue?logo=python" />
  <img src="https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin" />
  <img src="https://img.shields.io/badge/Compose-BOM%202024.06-green" />
  <img src="https://img.shields.io/badge/FastAPI-0.111-teal?logo=fastapi" />
</p>

---

## ✨ Фичи

- **Генерация отчеств** — полная логика с 40+ исключениями (Никита → Никитич, Илья → Ильич, Лев → Львович…)
- **Glassmorphism UI** — эффект жидкого стекла, размытие заднего плана (RenderEffect), неоновые рамки
- **Живые анимации** — летающие частицы, spring-анимации полей, взрыв частиц при нажатии, letter-by-letter появление текста
- **Агрессивные уведомления** — WorkManager с кликбейтными хуками каждые 3 часа
- **Python + Android** — FastAPI REST API, Retrofit на клиенте

---

## 🏗️ Структура проекта

```
patronymic-generator/
├── patronymic-core/          # Python: логика генерации
│   ├── generator.py          #   правила + исключения
│   ├── server.py             #   FastAPI сервер
│   └── test_generator.py     #   49 тестов
├── android/                  # Android: Kotlin + Compose
│   ├── app/src/main/java/com/patronymic/generator/
│   │   ├── ui/theme/         #   Тёмная тема + Glassmorphism модификаторы
│   │   ├── ui/effects/       #   Particle system (40 частиц + взрыв)
│   │   ├── ui/components/    #   GlassCard, GlassTextField, GenerateButton...
│   │   ├── ui/screens/       #   MainScreen + MainViewModel
│   │   ├── service/          #   WorkManager уведомления (20 хуков)
│   │   └── data/             #   Retrofit API слой
│   └── build.gradle.kts      #   Gradle с Compose, Retrofit, WorkManager
├── INTEGRATION_GUIDE.md      # Интеграция Python ↔ Android
└── shell.nix                 # Среда сборки для NixOS
```

---

## 🚀 Быстрый старт

### 1. Python сервер

```bash
cd patronymic-core
pip install -r requirements.txt
python server.py
# → http://localhost:8000
# → http://localhost:8000/api/docs (Swagger)
```

Проверка:
```bash
curl http://localhost:8000/api/generate?name=Александр
# → {"father_name":"Александр","son_patronymic":"Александрович","daughter_patronymic":"Александровна"}
```

### 2. Android приложение

#### Вариант A: Android Studio
```bash
# Открыть android/ как проект
# Build → Build APK
# Или просто Run на устройстве/эмуляторе
```

#### Вариант B: CLI

**Требования:** Java 17+, Android SDK 34

```bash
cd android

# Gradle wrapper (если нет gradlew)
gradle wrapper --gradle-version=8.7

# Сборка APK
./gradlew assembleDebug

# APK: app/build/outputs/apk/debug/app-debug.apk
```

#### Вариант C: NixOS

```bash
nix-shell shell.nix
cd android
./gradlew assembleDebug
```

> **Важно:** Для эмулятора URL уже настроен (`10.0.2.2:8000`). Для реального устройства замените IP в `PatronymicRepository.kt` на IP вашего ПК в локальной сети.

---

## 🧪 Тесты Python

```bash
cd patronymic-core
pip install pytest
pytest test_generator.py -v
# 49 passed
```

---

## 📱 Скриншоты

_(добавьте свои скриншоты UI)_

---

## 🛠️ Стек

| Компонент | Технология |
|-----------|-----------|
| Backend | Python 3.11+, FastAPI, Uvicorn |
| Android | Kotlin 2.0, Jetpack Compose, Material3 |
| UI-эффекты | Glassmorphism, RenderEffect, Canvas Particles |
| Анимации | Spring animations, InfiniteTransition, Animatable |
| Сеть | Retrofit 2, OkHttp, Gson |
| Уведомления | WorkManager 2.9 |
| Сборка | Gradle 8.5, Android Gradle Plugin 8.5 |

---

## 📄 Лицензия

MIT
