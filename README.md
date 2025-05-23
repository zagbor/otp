# Проект OTP

Данный проект предназначен для реализации одноразовых паролей (OTP) — их генерации, проверки, а также для обеспечения регистрации, входа пользователей и выполнения административных операций.

## Навигация

* [Основные возможности](#основные-возможности)
* [Необходимые компоненты](#необходимые-компоненты)
* [Установка и запуск проекта](#установка-и-запуск-проекта)
* [Использование API](#использование-api)
* [Команды и API-методы](#команды-и-api-методы)
* [Проверка функциональности](#проверка-функциональности)
* [Настройки приложения](#настройки-приложения)

## Основные возможности

* **Регистрация и вход**
  Пользователи могут создавать учетные записи и входить в систему. В процессе аутентификации создается JWT-токен.

* **Работа с одноразовыми паролями (OTP)**
  OTP создаются и проверяются для различных действий. Поддерживаются следующие способы доставки:

    * **FILE** — сохранение кода в файл.
    * **EMAIL** — отправка на email-адрес.
    * **SMS** — передача по SMS-сообщению.
    * **TELEGRAM** — отправка через Telegram-бота.

* **Администрирование**
  Предоставляется доступ к управлению конфигурацией OTP, получению перечня обычных пользователей и удалению пользователей вместе с их OTP.

## Необходимые компоненты

* **Java 17**

* **Maven**

* **PostgreSQL** — база данных должна быть доступна на:

    * URL: `jdbc:postgresql://localhost:5434/otp`
    * Пользователь: `postgres`
    * Пароль: `postgres`

* Для корректной работы Email/SMS/Telegram-уведомлений требуется предварительная настройка соответствующих сервисов в [application.yaml](./src/main/resources/application.yaml).

## Установка и запуск проекта

1. **Склонируйте репозиторий**:

   ```bash
   git clone <URL_репозитория>
   cd otp-project
   ```

2. **Соберите и запустите приложение** с использованием Maven:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   После запуска приложение будет доступно по адресу `http://localhost:8080`.

## Использование API

Взаимодействие с сервисом осуществляется через HTTP-запросы. Для этого можно использовать Postman, cURL или любой другой клиент.

### Примеры операций

1. **Создание нового пользователя**

    * **POST** `/api/v1/auth/register`

    * Пример тела запроса:

      ```json
      {
        "login": "username",
        "password": "your_password",
        "role": "USER", // или "ADMIN" (разрешен только один администратор)
        "email": "user@example.com",
        "phoneNumber": "+1234567890",
        "telegram": "telegram_handle"
      }
      ```

    * **Ответ:** JWT-токен

2. **Вход пользователя**

    * **POST** `/api/auth/login`

    * Пример тела запроса:

      ```json
      {
        "login": "username",
        "password": "your_password"
      }
      ```

    * **Ответ:** JWT-токен

3. **Создание одноразового пароля (OTP)**

   Требуется JWT-токен в заголовке Authorization.

    * **POST** `/api/v1/otp/generate`

    * Пример тела запроса:

      ```json
      {
        "operationId": "some_unique_operation_id",
        "deliveryType": "EMAIL" // допустимые значения: FILE, EMAIL, SMS, TELEGRAM
      }
      ```

    * **Ответ:** строка с OTP-кодом

4. **Проверка OTP-кода**

    * **POST** `/api/v1/otp/validate`

    * Пример тела запроса:

      ```json
      {
        "operationId": "some_unique_operation_id",
        "code": "введенный_код"
      }
      ```

    * **Ответ:** `true` — код валиден, `false` — нет

5. **Административные действия**

   > Требуется авторизация под учетной записью администратора.

    * **Обновление настроек OTP**

        * **PUT** `/api/v1/admin/otp-config`

        * Тело запроса (пример):

          ```json
          {
            "otpLength": 6,
            "otherConfigField": "value"
          }
          ```

        * **Ответ:** Объект конфигурации

    * **Список обычных пользователей**

        * **GET** `/api/v1/admin/users`
        * **Ответ:** JSON-массив с пользователями

    * **Удаление пользователя по ID**

        * **DELETE** `/api/v1/admin/user/{id}`
        * Пример: `DELETE /api/v1/admin/user/1`

## Проверка функциональности

Проект включает тесты. Их можно запускать стандартными средствами Maven:

```bash
mvn test
```

## Настройки приложения

Файл `application.properties`, расположенный в `src/main/resources`, содержит ключевые параметры:

* Настройки подключения к базе данных
* Конфигурация JPA/Hibernate
* Параметры для сервисов отправки сообщений через Email, SMS и Telegram (логины, пароли, токены и пр.)
* Для тестирования использовался телеграм бот @otp_zagbor_bot, токен которого лежит в конфигурации. Можно использовать и свой бот, но тогда нужно будет указать его токен в конфигурации.

Перед запуском убедитесь, что все нужные параметры указаны корректно.
