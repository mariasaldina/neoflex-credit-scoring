[![Coverage Status](https://coveralls.io/repos/github/mariasaldina/neoflex-credit-scoring/badge.svg?branch=feature/MVP-6)](https://coveralls.io/github/mariasaldina/neoflex-credit-scoring?branch=feature/MVP-6)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mariasaldina_mariasaldina_neoflex-credit-scoring&metric=alert_status&token=473d6d72fd61761e1e12c458fa8466c3a8ce8487)](https://sonarcloud.io/summary/new_code?id=mariasaldina_mariasaldina_neoflex-credit-scoring)

### Конвейер кредитного скоринга

Учебное приложение для автоматизированного анализа кредитных заявок, выдачи и подписания кредитных договоров.

## Стек

Java 21 + Spring Boot 3.5.14

## Архитектура:

Приложение имеет микросервисную архитектуру:

- **gateway**: API для внешнего пользователя
- **statement**: сервис для выдачи и обновления кредитных заявок
- **deal**: сервис для сохранения информации о клиенте и его заявке в БД PostgreSQL
- **calculator**: сервис скоринга заявок, принимающий решение о выдаче кредита / отказе
- **dossier**: клиент Kafka, управляющий отправкой электронных писем пользователю

Сервисы deal и dossier зависят от Kafka. deal зависит от PostgreSQL.

Адреса сервисов для запуска с docker-compose указаны в ./env.docker, их можно изменить, другие микросервисы подхватят их автоматически (при запуске в контейнере).

## Запуск

1. На хосте:

- Запустить контейнеры kafka и postgres из docker-compose.yml.
- Запустить каждый микросервис командой `./mvnw spring-boot:run`

*Примечание*: можно переопределить параметры POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD контейнера postgres.
Потребуется создать .env в deal/ с обновленными переменными для подключения к БД, например:

```bash
POSTGRES_URL=<your_url>
POSTGRES_USER=<your_user>
POSTGRES_PASSWORD=<your_password>
POSTGRES_DB=<your_db>
```

2. docker-compose: запустить в корне проекта:

`docker compose up --build`

*Примечание*: можно переопределить параметры POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD контейнера postgres.
Потребуется изменить соответствующие параметры контейнера deal.