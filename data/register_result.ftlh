<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Результат регистрации</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
    <div class="container">
        <#if success>
            <div class="success">
                <h3>Регистрация успешна!</h3>
                <p>Добро пожаловать, ${employee.fullName}!</p>
                <p>Ваш email: ${employee.email}</p>
                <a href="/login" class="btn btn-primary">Войти в систему</a>
                <a href="/books" class="btn btn-secondary">Перейти к книгам</a>
            </div>
        <#else>
            <div class="error">
                <h3>Регистрация не удалась</h3>
                <p>Пользователь с email ${email} уже существует.</p>
                <a href="/register" class="btn btn-primary">Попробовать снова</a>
                <a href="/login" class="btn btn-secondary">Войти в существующий аккаунт</a>
            </div>
        </#if>
    </div>
</body>
</html>