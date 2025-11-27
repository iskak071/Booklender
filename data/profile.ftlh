<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <link rel="stylesheet" href="./css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body class="bg-light">
    <div class="container d-flex justify-content-center">
        <div class="col-8 col-md-6 col-lg-4 mt-5 text-center">

            <h2>Профиль пользователя</h2>

            <#if employee??>
                <div class="alert alert-success">Вы успешно авторизованы!</div>
                <p><strong>Имя:</strong> ${employee.fullName}</p>
                <p><strong>Email:</strong> ${employee.email}</p>
                <p><strong>ID:</strong> ${employee.id}</p>
                <br>
                <a href="/books" class="btn btn-primary">Перейти к книгам</a>
            <#else>
                <p><strong>Пользователь не найден!</strong></p>
                <p>Зарегистрируйтесь или войдите в аккаунт.</p>
                <a href="/login" class="btn btn-success w-100 mb-2">Войти</a>
                <a href="/register" class="btn btn-primary w-100">Зарегистрироваться</a>
            </#if>

        </div>
    </div>
</body>
</html>