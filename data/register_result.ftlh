<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Результат регистрации</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>

<body class="bg-light">
    <div class="container d-flex justify-content-center">
        <div class="col-10 col-md-6 col-lg-4 mt-5 text-center">

            <h2>Результат регистрации</h2>

            <#if success>
                <div class="alert alert-success mt-3">
                    Регистрация прошла успешно!
                </div>

                <p><strong>Добро пожаловать:</strong> ${employee.fullName}</p>
                <p><strong>Email:</strong> ${employee.email}</p>

                <br>
                <a href="/login" class="btn btn-danger w-100 mb-2">Выйти из аккаунта</a>
                <a href="/books" class="btn btn-primary w-100">Перейти к книгам</a>

            <#else>
                <div class="alert alert-danger mt-3">
                    Регистрация не удалась!
                </div>

                <p>Пользователь с email <strong>${email}</strong> уже существует.</p>

                <br>
                <a href="/register" class="btn btn-primary w-100 mb-2">Попробовать снова</a>
                <a href="/login" class="btn btn-success w-100">Войти в существующий аккаунт</a>
            </#if>

        </div>
    </div>
</body>
</html>
