<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
    <div class="profile">
        <h2>Профиль пользователя</h2>

        <#if employee??>
            <div class="success">Вы успешно авторизованы!</div>
            <p><strong>Имя:</strong> ${employee.fullName}</p>
            <p><strong>Email:</strong> ${employee.email}</p>
            <p><strong>ID:</strong> ${employee.id}</p>
            <br>
            <a href="/books" class="btn btn-primary">Перейти к книгам</a>
        <#else>
            <p><strong>Некий пользователь</strong></p>
            <p>Это общий шаблон профиля.</p>
            <a href="/login" class="btn btn-primary">Войти</a>
            <a href="/register">Зарегистрироваться</a>
        </#if>
    </div>
</body>
</html>