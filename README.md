## Описание

Веб-приложение предоставляет функционал конвертации валют на основании последних валютных курсов, опубликованных ЦБ РФ.

Используемые технологии:

- Spring Boot

- Angular

- Liquibase

### endpoints

- `GET /` - стартовая страница. При переходе загружаются курсы на последнюю дату. Возвращает JSON с кодами валют на последнюю дату (для заполнения выпадающих списков на frontend).

- `GET /convert` - конвертация валюты. 

     Параметры:     
     - `curfrom` - исходная валюта, 
     - `curto` - валюта, в которую нужно конвертировать,
     - `amount` - сумма исходной валюты.
     
     Пример запроса: `localhost:8080/convert?curfrom=USD&curto=EUR&amount=5000`
      

- `GET /history` - список всех конвертаций.

- `GET /stat` - статистика конвертаций за текущую неделю.

## Сборка и запуск проекта

Для работы приложения требуется **JDK** версии 8 или выше.

Сейчас в проекте указана версия JDK 11. Если будет использоваться более низкая версия, нужно указать ее в параметре `java.version` в файле `pom.xml`.

Для работы **frontend** требуется **Node.js** и **npm**.

Перед запуском приложения необходимо создать базу данных **Postgres** с именем **curr_conv**, пользователем **postgres** и паролем **postgres**.

#### Запуск backend

- Клонировать проект

  `git clone https://github.com/ignal1/conv.git`

- Перейти в директорию *backend* и в терминале выполнить команду

  `./mvnw spring-boot:run` (для Linux)
  
  или
  
  `mvnw spring-boot:run` (для Windows)

#### Запуск frontend

- В новом окне терминала перейти в директорию *frontend* и  выполнить команды

  ```
  npm install
  npm start
  ```

  После этого приложение будет доступно на *loclhost:4200*.
  
  Для завершения процессов в каждом окне терминала нажать `Ctrl + C`, после чего ввести в терминале `y` и нажать `Enter`. 