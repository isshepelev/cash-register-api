# Restaurant Management System

## Описание проекта

Restaurant Management System - это комплексное веб-приложение для автоматизации работы ресторана, разработанное на Java с использованием Spring Framework. Система предоставляет полный цикл управления заведениями общественного питания.

## Ключевые особенности

- 🍽️ Управление меню с системой ревизий
- 👨‍🍳 Учет сотрудников с различными уровнями доступа
- ⚡ Асинхронная обработка заказов через **Apache Kafka**
- 📊 Генерация отчетов с использованием **Apache Commons CSV**
- 🔐 Гибкая система аутентификации и авторизации
- 🤖 REST API для интеграции с другими системами

## Технологический стек

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Spring Kafka
- Spring Session (Redis)
- Lombok

### Базы данных
- PostgreSQL
- Redis

### Инфраструктура
- Docker
- Apache Kafka
- Apache Commons CSV

### Frontend
- Thymeleaf
- Bootstrap
- JavaScript

## Установка и запуск

1. Убедитесь, что у вас установлены Docker и Docker Compose
2. Клонируйте репозиторий
3. Внутри проекта пропишите docker compose up --build
