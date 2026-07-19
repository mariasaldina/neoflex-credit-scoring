[![Coverage Status](https://coveralls.io/repos/github/mariasaldina/neoflex-credit-scoring/badge.svg?branch=feature/MVP-6)](https://coveralls.io/github/mariasaldina/neoflex-credit-scoring?branch=feature/MVP-6)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mariasaldina_mariasaldina_neoflex-credit-scoring&metric=alert_status&token=473d6d72fd61761e1e12c458fa8466c3a8ce8487)](https://sonarcloud.io/summary/new_code?id=mariasaldina_mariasaldina_neoflex-credit-scoring)

## Конвейер кредитного скоринга

Учебное приложение для автоматизированного анализа кредитных заявок, выдачи и подписания кредитных договоров.

---

### Стек

Java 21 + Spring Boot 3.5.14

---

### Архитектура:

Приложение имеет микросервисную архитектуру:

- **gateway**: API для внешнего пользователя
- **statement**: сервис для выдачи и обновления кредитных заявок
- **deal**: сервис для сохранения информации о клиенте и его заявке в БД PostgreSQL
- **calculator**: сервис скоринга заявок, принимающий решение о выдаче кредита / отказе
- **dossier**: клиент Kafka, управляющий отправкой электронных писем пользователю

**Важно**:
- deal и dossier имеют runtime-зависимость от Kafka
- deal имеет runtime-зависимость от PostgreSQL

---

### Запуск

**Важно**: для запуска dossier необходимо создать файл ./dossier/.env и указать параметры подключения к SMTP-серверу.

Пример (данные MAIL_HOST и MAIL_PORT указаны по умолчанию в application.yml):

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_username
MAIL_PASSWORD=your_password
```

Доступно 3 режима запуска:

1. Микросервисы — на хосте, Kafka и PostgreSQL — в docker-контейнерах:

- Запустить контейнеры kafka и postgres: `docker compose up kafka postgres`
- Запустить каждый микросервис командой `./mvnw spring-boot:run`

*Примечание*: можно переопределить параметры `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` контейнера postgres.
Потребуется создать ./deal/.env с обновленными переменными для подключения к БД.

Пример:

```bash
POSTGRES_URL=<your_url>
POSTGRES_USER=<your_user>
POSTGRES_PASSWORD=<your_password>
```

2. Запуск всего приложения через docker-compose:
   
Запустить в корне проекта: `docker compose up --build`

*Примечание*: можно переопределить параметры `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` контейнера postgres.
Потребуется изменить соответствующие параметры контейнера deal (см. пункт 1).

3. Запуск всего приложения на хосте

- Запустить Kafka, при необходимости переопределить `KAFKA_BOOTSTRAP_SERVERS` в ./deal/.env, ./dossier/.env
- Создать БД PostgreSQL и настроить подключение к ней: добавить ./deal/.env (см. пункт 1)
- Запустить каждый микросервис командой `./mvnw spring-boot:run`

---

### Переменные окружения

.env.docker в корне проекта используется как дефолтный набор переменных для docker compose.
Это публичный файл, он не содержит секретов.
**Важно:** используется только при запуске в Docker, не предназначен для конфигурации локальных запусков.

Переменные конкретных сервисов задаются отдельно:
- при запуске в Docker — через environment или env_file
- при запуске на хосте — через файл <service>/.env, который подхватывается приложением