# Patronymic Generator · Генератор Отчеств

**Android-приложение** (Kotlin + Jetpack Compose) для генерации правильных русских отчеств.  
Полностью офлайн, никаких серверов. Стеклянный дизайн, летающие частицы, неон.

<p float="left">
  <img src="https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin" />
  <img src="https://img.shields.io/badge/Compose-BOM%202024.06-green" />
  <img src="https://img.shields.io/badge/API-26%2B-orange" />
  <img src="https://img.shields.io/badge/offline-yes-brightgreen" />
</p>

---

## ✨ Фичи

- **Генерация отчеств** — полная логика на Kotlin, 40+ исключений (Никита → Никитич, Илья → Ильич, Лев → Львович…)
- **Glassmorphism UI** — эффект жидкого стекла, размытие (RenderEffect), неоновые рамки
- **Живые анимации** — летающие частицы, spring-поля, взрыв частиц при нажатии, letter-by-letter текст
- **Агрессивные уведомления** — WorkManager, 20 кликбейтных хуков каждые 3 часа
- **Полностью офлайн** — не требует интернета, все вычисления на устройстве

---

## 🏗️ Структура проекта

```
patronymic-generator/
├── android/                          # Android: Kotlin + Compose
│   └── app/src/main/java/com/patronymic/generator/
│       ├── data/
│       │   └── LocalPatronymicGenerator.kt   ← вся логика генерации
│       │   └── PatronymicResult.kt           ← модель данных
│       ├── ui/theme/                 #   Тёмная тема + Glassmorphism модификаторы
│       ├── ui/effects/               #   Particle system (40 частиц + взрыв)
│       ├── ui/components/            #   GlassCard, GlassTextField, GenerateButton...
│       ├── ui/screens/               #   MainScreen + MainViewModel
│       └── service/                  #   WorkManager уведомления (20 хуков)
├── patronymic-core/                  # Python-прототип (для сверки, не обязателен)
│   ├── generator.py
│   ├── server.py
│   └── test_generator.py             # 49 тестов
├── README.md
└── shell.nix                         # Среда сборки для NixOS
```

---

## 🚀 Сборка APK

### Вариант A: Android Studio
```bash
# Открыть android/ как проект
# Build → Build Bundle(s) / APK(s) → Build APK
```

### Вариант B: CLI
```bash
# Требования: Java 17+, Android SDK 34

cd android

# Gradle wrapper (если нет gradlew)
gradle wrapper --gradle-version=8.7

# Сборка
./gradlew assembleDebug

# APK: app/build/outputs/apk/debug/app-debug.apk
```

### Вариант C: NixOS
```bash
nix-shell shell.nix
cd android
./gradlew assembleDebug
```

---

## 📱 Использование

1. Запусти приложение
2. Введи имя отца (например, **Александр**)
3. Выбери: Сын / Дочь / Оба варианта
4. Нажми **Сгенерировать** — наслаждайся взрывом частиц!
5. Отчество появится с letter-by-letter анимацией
6. Жми **Копировать** — отчество в буфере обмена

Уведомления будут приходить каждые 3 часа и напоминать об отчествах.

---

## 🧪 Python-тесты (для разработчиков)

```bash
cd patronymic-core
pip install -r requirements.txt
pytest test_generator.py -v
# 49 passed
```

---

## 🛠️ Стек

| Компонент | Технология |
|-----------|-----------|
| Язык | Kotlin 2.0 |
| UI | Jetpack Compose + Material3 |
| Эффекты | Glassmorphism, RenderEffect, Canvas Particles |
| Анимации | Spring, InfiniteTransition, Animatable |
| Уведомления | WorkManager 2.9 |
| Сборка | Gradle 8.5, Android Gradle Plugin 8.5 |
| Прототип | Python 3.11+ (FastAPI, не обязателен) |

---

## 📄 Лицензия

MIT
