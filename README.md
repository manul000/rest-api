# REST API Task Manager

Установите JDK 22. В переменных среды Windows задайте `JAVA_HOME` — папку JDK, а в `Path` добавьте `%JAVA_HOME%\bin`.
Затем откройте PowerShell в папке проекта. Maven загрузится автоматически; первый запуск требует интернета.

Установка зависимостей и сборка:

```powershell
.\mvnw.cmd package
```

Запуск тестов:

```powershell
.\mvnw.cmd test
```

Запуск сервера:

```powershell
java -jar .\target\09-rest-api-1.0-SNAPSHOT.jar
```

Откройте второе окно PowerShell и создайте задачу. Команда отправляет JSON в байтах UTF-8 и явно декодирует ответ сервера в UTF-8.

```powershell
$uri = 'http://localhost:8080/tasks'
$utf8 = [System.Text.Encoding]::UTF8
$body = @{ title = 'Первая задача'; status = 'TODO' } | ConvertTo-Json
$response = Invoke-WebRequest -UseBasicParsing -Uri $uri -Method Post `
    -ContentType 'application/json; charset=utf-8' -Body ($utf8.GetBytes($body))
$task = $utf8.GetString($response.RawContentStream.ToArray()) | ConvertFrom-Json
$task.title
```

Ожидаемый вывод — `Первая задача`. Чтобы получить список задач, выполните в том же окне ещё один запрос. Его ответ тоже читается в UTF-8.

```powershell
$response = Invoke-WebRequest -UseBasicParsing -Uri $uri -Method Get
$utf8.GetString($response.RawContentStream.ToArray()) | ConvertFrom-Json
```

Остановка — `Ctrl+C`.
