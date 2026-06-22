package com.patronymic.generator.data

/**
 * Локальный генератор русских отчеств — чистая Kotlin-реализация.
 * Полный аналог Python-версии (generator.py), не требует сервера.
 * Работает офлайн, встроен прямо в APK.
 */
object LocalPatronymicGenerator {

    // Полный словарь исключений: имя → (отчество для сына, отчество для дочери)
    private val exceptions = mapOf(
        // Имена на -а/-я (особые правила)
        "Никита" -> Pair("Никитич", "Никитична"),
        "Илья" -> Pair("Ильич", "Ильинична"),
        "Фома" -> Pair("Фомич", "Фоминична"),
        "Кузьма" -> Pair("Кузьмич", "Кузьминична"),
        "Савва" -> Pair("Саввич", "Саввична"),
        "Лука" -> Pair("Лукич", "Лукинична"),
        "Добрыня" -> Pair("Добрынич", "Добрынична"),
        // Имена с историческими чередованиями / выпадением гласных
        "Лев" -> Pair("Львович", "Львовна"),
        "Пётр" -> Pair("Петрович", "Петровна"),
        "Павел" -> Pair("Павлович", "Павловна"),
        "Яков" -> Pair("Яковлевич", "Яковлевна"),
        "Всеволод" -> Pair("Всеволодович", "Всеволодовна"),
        // Имена на -ей
        "Алексей" -> Pair("Алексеевич", "Алексеевна"),
        "Сергей" -> Pair("Сергеевич", "Сергеевна"),
        "Андрей" -> Pair("Андреевич", "Андреевна"),
        "Тимофей" -> Pair("Тимофеевич", "Тимофеевна"),
        "Матвей" -> Pair("Матвеевич", "Матвеевна"),
        "Дмитрий" -> Pair("Дмитриевич", "Дмитриевна"),
        // Имена на -ий
        "Юрий" -> Pair("Юрьевич", "Юрьевна"),
        "Василий" -> Pair("Васильевич", "Васильевна"),
        "Геннадий" -> Pair("Геннадьевич", "Геннадьевна"),
        "Евгений" -> Pair("Евгеньевич", "Евгеньевна"),
        "Виталий" -> Pair("Витальевич", "Витальевна"),
        "Анатолий" -> Pair("Анатольевич", "Анатольевна"),
        "Григорий" -> Pair("Григорьевич", "Григорьевна"),
        "Арсений" -> Pair("Арсеньевич", "Арсеньевна"),
        "Дементий" -> Pair("Дементьевич", "Дементьевна"),
        "Леонид" -> Pair("Леонидович", "Леонидовна"),
        "Герман" -> Pair("Германович", "Германовна"),
        // Имена на -слав
        "Вячеслав" -> Pair("Вячеславович", "Вячеславовна"),
        "Ярослав" -> Pair("Ярославович", "Ярославовна"),
        "Ростислав" -> Pair("Ростиславович", "Ростиславовна"),
        "Владислав" -> Pair("Владиславович", "Владиславовна"),
        "Станислав" -> Pair("Станиславович", "Станиславовна"),
        "Святослав" -> Pair("Святославович", "Святославовна"),
        "Болеслав" -> Pair("Болеславович", "Болеславовна"),
        "Мстислав" -> Pair("Мстиславович", "Мстиславовна"),
        // Прочие частые исключения
        "Олег" -> Pair("Олегович", "Олеговна"),
        "Игорь" -> Pair("Игоревич", "Игоревна"),
        "Егор" -> Pair("Егорович", "Егоровна"),
        "Михаил" -> Pair("Михайлович", "Михайловна"),
        "Гавриил" -> Pair("Гаврилович", "Гавриловна"),
        "Даниил" -> Pair("Данилович", "Даниловна"),
        "Кирилл" -> Pair("Кириллович", "Кирилловна"),
        "Владимир" -> Pair("Владимирович", "Владимировна"),
    )

    /**
     * Генерирует отчество для сына и дочери по имени отца.
     *
     * @param name Имя отца (например "Александр")
     * @return PatronymicResult или null, если имя не поддерживается
     */
    fun generate(name: String): PatronymicResult? {
        val normalized = normalizeName(name) ?: return null

        // 1. Проверка исключений
        exceptions[normalized]?.let { (son, daughter) ->
            return PatronymicResult(normalized, son, daughter)
        }

        // 2. Алгоритмическая генерация по окончанию
        val (son, daughter) = when {
            normalized.endsWith("ий") -> generateFromIy(normalized)
            normalized.endsWith("ей") -> generateFromEy(normalized)
            normalized.endsWith("а") -> generateFromA(normalized)
            normalized.endsWith("я") -> generateFromYa(normalized)
            normalized.endsWith("ь") -> generateFromSoftSign(normalized)
            else -> generateDefault(normalized)
        }

        return PatronymicResult(normalized, son, daughter)
    }

    private fun normalizeName(name: String): String? {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return null
        return trimmed.replaceFirstChar { it.uppercase() }
    }

    // --- Правила ---

    // Твёрдый согласный: Александр → Александрович / Александровна
    private fun generateDefault(stem: String) =
        Pair("${stem}ович", "${stem}овна")

    // -ий: Дмитрий → Дмитриевич / Дмитриевна (отрезаем "ий" → "Дмитр" + "евич")
    private fun generateFromIy(name: String): Pair<String, String> {
        val stem = name.dropLast(2) // отрезаем "ий"
        return Pair("${stem}евич", "${stem}евна")
    }

    // -ей: Андрей → Андреевич / Андреевна
    private fun generateFromEy(name: String): Pair<String, String> {
        val stem = name.dropLast(2) // отрезаем "ей"
        return Pair("${stem}евич", "${stem}евна")
    }

    // -а: Никита → Никитич / Никитична (только для имён не из исключений)
    private fun generateFromA(name: String): Pair<String, String> {
        val stem = name.dropLast(1) // отрезаем "а"
        return Pair("${stem}ич", "${stem}ична")
    }

    // -я: обычно уже в исключениях (Илья, Добрыня)
    private fun generateFromYa(name: String): Pair<String, String> {
        val stem = name.dropLast(1) // отрезаем "я"
        return Pair("${stem}ич", "${stem}ична")
    }

    // -ь: Игорь → Игоревич / Игоревна
    private fun generateFromSoftSign(name: String): Pair<String, String> {
        val stem = name.dropLast(1) // отрезаем "ь"
        return Pair("${stem}евич", "${stem}евна")
    }

    /** Список всех поддерживаемых имён (для автодополнения). */
    fun getSupportedNames(): List<String> {
        val names = mutableSetOf<String>()
        names.addAll(exceptions.keys)
        names.addAll(listOf(
            "Александр", "Дмитрий", "Максим", "Артём", "Иван",
            "Роман", "Денис", "Константин", "Николай", "Виктор",
            "Борис", "Глеб", "Вадим", "Семён", "Трофим",
            "Назар", "Захар", "Тимур", "Руслан", "Марат",
            "Артур", "Эрик", "Феликс", "Платон", "Георгий",
            "Валентин", "Вениамин", "Аркадий", "Эдуард", "Антон",
        ))
        return names.sorted()
    }
}
