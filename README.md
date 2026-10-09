# AgroManagement

Учебная информационная система аграрного предприятия (проект для колледжа).
Агроном планирует, какую культуру и на каком поле сеять в этом сезоне, механик отмечает,
сколько часов отработала техника и сколько ушло топлива, а кладовщик ведёт приход и расход
на складе. Администратор управляет сотрудниками и видит журнал действий, а ещё может
одной кнопкой сделать резервную копию базы.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-server--rendered-green)

## Возможности

### Агроном
- План посевных работ: поле, площадь, культура, сезон
- Список планов на дашборде

### Механик
- Учёт работы техники: какая машина, дата, отработанные часы, расход топлива
- Справочник техники (трактор, комбайн, опрыскиватель, плуг)

### Кладовщик
- Складские остатки (топливо, семена, удобрения, масло)
- Приход и расход по позициям, история операций

### Администратор
- Создание и редактирование сотрудников, назначение роли
- Блокировка / разблокировка учётной записи
- Журнал действий пользователей
- Резервная копия базы через `pg_dump` одной кнопкой (файлы падают в папку `backups/`)

## Роли и права

Доступ режется на уровне URL в `SecurityConfig`, после логина `/` редиректит на дашборд своей роли.

| Раздел | ADMIN | AGRONOMIST | MECHANIC | STOREKEEPER |
|---|:---:|:---:|:---:|:---:|
| Вход / выход | ✔ | ✔ | ✔ | ✔ |
| `/admin/**` — сотрудники, журнал, бэкап | ✔ | — | — | — |
| `/agronomist/**` — планирование посевов | — | ✔ | — | — |
| `/mechanic/**` — работа техники и топливо | — | — | ✔ | — |
| `/storekeeper/**` — склад | — | — | — | ✔ |

## Стек

| Слой | Технология |
|---|---|
| Backend | Java 17, Spring Boot 4.1 |
| Безопасность | Spring Security (BCrypt, роли, form login) |
| Доступ к данным | Spring Data JPA / Hibernate |
| БД | PostgreSQL, миграции Flyway |
| Шаблоны | Thymeleaf |
| Прочее | Lombok, Maven Wrapper |

Схема БД ведётся миграциями (`db/migration/V1__init.sql`, `V2__data.sql`),
Hibernate только проверяет её (`ddl-auto=validate`).

## Запуск

### Требования
- JDK 17+
- PostgreSQL (по умолчанию ждём его на `localhost:5433`, база `agro_db`)
- `pg_dump` в `PATH` — нужен только для кнопки бэкапа

### 1. Создайте базу

```sql
CREATE DATABASE agro_db;
```

Таблицы и стартовые данные Flyway накатит сам при первом запуске.
Если PostgreSQL на другом порту, поправьте `spring.datasource.url` в `application.properties`.

### 2. Задайте переменные окружения

| Переменная | По умолчанию | Описание |
|---|---|---|
| `DB_USERNAME` | `postgres` | Пользователь БД |
| `DB_PASSWORD` | `postgres` | Пароль БД |

Пример лежит в `.env.example`.

### 3. Запустите приложение

```bash
./mvnw spring-boot:run
```

Откройте http://localhost:7676

### Тестовые пользователи

Создаются миграцией `V2__data.sql`:

| Логин | Пароль | Роль |
|---|---|---|
| `admin` | `admin` | Администратор |
| `agro` | `agro` | Агроном |
| `mech` | `mech` | Механик |
| `store` | `store` | Кладовщик |

## Структура проекта

```
src/main/java/com/pavlent1yy/agro_management/
├── config/        # SecurityConfig, загрузка пользователя для Security
├── controller/    # Auth, Admin, Agronomist, Mechanic, Storekeeper
├── dto/           # формы
├── entity/        # JPA-сущности
├── repository/    # Spring Data репозитории
└── service/       # бизнес-логика, журнал действий, бэкап

src/main/resources/
├── application.properties
├── db/migration/  # Flyway
├── static/css/
└── templates/     # Thymeleaf, по папке на роль
```
