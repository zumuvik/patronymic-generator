"""
FastAPI сервер для генерации русских отчеств.
Предоставляет REST API для Android приложения.
"""

from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
import logging
from typing import Optional

from generator import generate_patronymic, get_supported_names

# Настройка логирования
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
)
logger = logging.getLogger("patronymic-api")

app = FastAPI(
    title="Генератор Отчеств API",
    description="REST API для генерации русских отчеств по имени отца",
    version="1.0.0",
)

# CORS — разрешаем все origins для Android/веб-клиентов
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.on_event("startup")
async def startup():
    logger.info("🚀 API Генератора Отчеств запущен")
    names_count = len(get_supported_names())
    logger.info(f"📚 Загружено {names_count} поддерживаемых имён")


@app.get("/api/health")
async def health():
    """Проверка работоспособности сервера."""
    return {
        "status": "ok",
        "service": "patronymic-generator",
        "version": "1.0.0",
    }


@app.get("/api/generate")
async def generate(
    name: str = Query(
        ...,
        min_length=2,
        max_length=50,
        description="Имя отца в именительном падеже (например, Александр)",
    )
):
    """
    Генерирует отчество для сына и дочери по имени отца.

    Пример: /api/generate?name=Александр
    """
    logger.info(f"📝 Запрос на генерацию отчества для имени: '{name}'")

    try:
        result = generate_patronymic(name)
        logger.info(f"✅ Результат: {result}")
        return {
            "father_name": result["father_name"],
            "son_patronymic": result["son_patronymic"],
            "daughter_patronymic": result["daughter_patronymic"],
        }
    except ValueError as e:
        logger.warning(f"❌ Ошибка: {e}")
        raise HTTPException(status_code=422, detail=str(e))


@app.get("/api/names")
async def list_names():
    """Возвращает список всех поддерживаемых имён."""
    names = get_supported_names()
    return {
        "count": len(names),
        "names": names,
    }


@app.exception_handler(HTTPException)
async def http_exception_handler(request, exc):
    """Кастомный обработчик ошибок."""
    return JSONResponse(
        status_code=exc.status_code,
        content={
            "error": True,
            "message": exc.detail,
            "status_code": exc.status_code,
        },
    )


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "server:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info",
    )
