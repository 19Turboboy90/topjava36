# Meals API — cURL Testing Guide

### GET - Получить все приемы пищи

> curl -X GET localhost:8080/topjava/rest/meals -H "Content-Type: application/json"

### GET - Получить один прием пищи по ID

> curl -X GET "http://localhost:8080/topjava/rest/meals/100003" -H "Content-Type: application/json"

### POST - Создать новый прием пищи

> curl -X POST "http://localhost:8080/topjava/rest/meals" -H "Content-Type: application/json" -d '{"dateTime": "
> 2020-02-01T12:00","description": "Lunch","calories": 600}'

### PUT — Обновить приём пищи по ID

> curl -X PUT "http://localhost:8080/topjava/rest/meals/100003"  -H "Content-Type: application/json" -d '{"dateTime": "
> 2020-02-01T12:30","description": "Updated lunch","calories": 550}'

### DELETE — Удалить приём пищи

> curl -X DELETE "http://localhost:8080/topjava/rest/meals/100003"

### GET — Фильтрация по дате и времени

> curl -X
> GET "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=00:00&endDate=2020-01-30&endTime=23:59"
> -H "Content-Type: application/json"