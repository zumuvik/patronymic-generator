"""
Юнит-тесты для генератора отчеств.
Запуск: pytest test_generator.py -v
"""

import pytest
from generator import generate_patronymic, get_supported_names


class TestBasicGeneration:
    """Базовые случаи генерации."""

    def test_aleksandr(self):
        result = generate_patronymic("Александр")
        assert result["son_patronymic"] == "Александрович"
        assert result["daughter_patronymic"] == "Александровна"
        assert result["father_name"] == "Александр"

    def test_dmitriy(self):
        result = generate_patronymic("Дмитрий")
        assert result["son_patronymic"] == "Дмитриевич"
        assert result["daughter_patronymic"] == "Дмитриевна"

    def test_maksim(self):
        result = generate_patronymic("Максим")
        assert result["son_patronymic"] == "Максимович"
        assert result["daughter_patronymic"] == "Максимовна"

    def test_artem(self):
        result = generate_patronymic("Артём")
        assert result["son_patronymic"] == "Артёмович"
        assert result["daughter_patronymic"] == "Артёмовна"

    def test_roman(self):
        result = generate_patronymic("Роман")
        assert result["son_patronymic"] == "Романович"
        assert result["daughter_patronymic"] == "Романовна"


class TestExceptions:
    """Тестирование всех исключений."""

    @pytest.mark.parametrize("name, son, daughter", [
        # Имена на -а/-я
        ("Никита", "Никитич", "Никитична"),
        ("Илья", "Ильич", "Ильинична"),
        ("Фома", "Фомич", "Фоминична"),
        ("Кузьма", "Кузьмич", "Кузьминична"),
        ("Савва", "Саввич", "Саввична"),
        ("Лука", "Лукич", "Лукинична"),
        ("Добрыня", "Добрынич", "Добрынична"),
        # Имена с чередованиями
        ("Лев", "Львович", "Львовна"),
        ("Пётр", "Петрович", "Петровна"),
        ("Павел", "Павлович", "Павловна"),
        ("Яков", "Яковлевич", "Яковлевна"),
        # Имена на -ей
        ("Алексей", "Алексеевич", "Алексеевна"),
        ("Сергей", "Сергеевич", "Сергеевна"),
        ("Андрей", "Андреевич", "Андреевна"),
        ("Тимофей", "Тимофеевич", "Тимофеевна"),
        ("Матвей", "Матвеевич", "Матвеевна"),
        # Имена на -ий
        ("Юрий", "Юрьевич", "Юрьевна"),
        ("Василий", "Васильевич", "Васильевна"),
        ("Геннадий", "Геннадьевич", "Геннадьевна"),
        ("Евгений", "Евгеньевич", "Евгеньевна"),
        ("Виталий", "Витальевич", "Витальевна"),
        ("Анатолий", "Анатольевич", "Анатольевна"),
        ("Григорий", "Григорьевич", "Григорьевна"),
        ("Арсений", "Арсеньевич", "Арсеньевна"),
        # Имена на -слав
        ("Вячеслав", "Вячеславович", "Вячеславовна"),
        ("Ярослав", "Ярославович", "Ярославовна"),
        ("Ростислав", "Ростиславович", "Ростиславовна"),
        ("Владислав", "Владиславович", "Владиславовна"),
        ("Станислав", "Станиславович", "Станиславовна"),
        # Прочие
        ("Олег", "Олегович", "Олеговна"),
        ("Игорь", "Игоревич", "Игоревна"),
        ("Егор", "Егорович", "Егоровна"),
        ("Михаил", "Михайлович", "Михайловна"),
        ("Гавриил", "Гаврилович", "Гавриловна"),
        ("Даниил", "Данилович", "Даниловна"),
        ("Кирилл", "Кириллович", "Кирилловна"),
        ("Владимир", "Владимирович", "Владимировна"),
    ])
    def test_exception(self, name, son, daughter):
        result = generate_patronymic(name)
        assert result["son_patronymic"] == son, f"{name}: сын"
        assert result["daughter_patronymic"] == daughter, f"{name}: дочь"


class TestLowerCase:
    """Имена в нижнем регистре."""

    def test_lowercase(self):
        result = generate_patronymic("александр")
        assert result["son_patronymic"] == "Александрович"

    def test_mixed_case(self):
        result = generate_patronymic("аЛекСандР")
        assert result["son_patronymic"] == "Александрович"


class TestErrors:
    """Ошибки и граничные случаи."""

    def test_empty_name(self):
        with pytest.raises(ValueError, match="пустым"):
            generate_patronymic("")

    def test_whitespace_name(self):
        with pytest.raises(ValueError, match="пустым"):
            generate_patronymic("   ")

    def test_numbers_in_name(self):
        with pytest.raises(ValueError, match="недопустимые символы"):
            generate_patronymic("Alex123")


class TestSupportedNames:
    """Список поддерживаемых имён."""

    def test_contains_exceptions(self):
        names = get_supported_names()
        assert "Александр" in names
        assert "Никита" in names
        assert "Илья" in names

    def test_sorted(self):
        names = get_supported_names()
        assert names == sorted(names)


if __name__ == "__main__":
    pytest.main([__file__, "-v"])
