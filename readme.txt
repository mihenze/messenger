Программа написана на языке Java, с использованием фреймворка SpringBoot и Hibernate.
БД MySQL.
Для построения запросов к БД используется Spring JPA
Для аутентификации и генерации токена используется SpringSecurity и JSONWebToken
Lombok используется для исключения повторяющегося кода (геттеры, сеттеры, конструкторы)

Программа состоит из двух SQL таблиц вида:
user: id, name, password
message: id, text, FOREIGN KEY (user_id)

MessengerApplication - стандартный стартовый класс для SpringBoot
В пакете entity - указаны репрезентации данных таблиц.
в пакете repository - описаны объекты отвечающие за хранения и коммуникацию с БД
В пакете dto - описан легковесный класс для сообщений
В пакете service - описаны объекты сервисы, которые манипулируют данными и связывают между собой классы-контроллеров и классы-репозиториев
В пакете security - описаны классы отвечающиеся за генерацию токена, валидацию и аутентификацию
В пакете payload - описываются объекты которые возвращает сервер
В пакете controller - описаны классы-контроллеры, которые обрабатывают поступившие эндпоинты

Эндпоинты
1) Аутентификация пользователя POST-запрос (/auth/signin)
тело запроса:
{
     name: "имя отправителя"
     password: "пароль"
}
- при успешной аутентификации возвращает Bearer_token
{
    token: "тут сгенерированный токен" 
}
- иначе сообщение об ошибке в соответствии с классом SigninRequest, описанным в пакете payload

2) Регистрация пользователя POST-запрос (/auth/signup)
{
     name: "имя отправителя"
     password: "пароль"
}
- возвращает пользователю сообщение об успешной решистрации:
{
    "message": "User registered successfully"
}
- или сообщение об ошибке, в соответсвтии с классом SignupRequest, описанным в пакете payload

3) Сохранение сообщения пользователя POST-запрос (/message/add)
тело запроса:
{
     message:    "текст сообщение"
}
в загаловке указывается Bearer токен
имя пользователя берется из авторизации
- при сохранении выдается сообщение
{
         "id": "id сообщения",
         "text": "текст сообщения",
         "name": "имя пользователя, написавшего сообщение"
}

4) Получение последних сообщений заданного количества GET-запрос (/message/{limitMessage}/get)
тело запроса отсутствует
в заголовке передается параметр количества последних сообщений, которые нужно передать пользователю
в загаловке указывается Bearer
- результат выдается в виде массива данных
{
         "id": "id сообщения",
         "text": "текст сообщения",
         "name": "имя пользователя, написавшего сообщение"
}


Не сумел подружить данное приложение с Docker, при попытки создания образа валится с ошибкой:
Communications link failure
The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:174) ~[mysql-connector-java-8.0.29.jar:8.0.29]
...
поэтому образа Docker нет

Ps. При тестировании запросов с помощью Postman, все работали верно.







