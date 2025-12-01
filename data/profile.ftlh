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

                <#if currentBooks?? && currentBooks?size gt 0>
                    <div class="mt-4">
                        <h5>Ваши текущие книги:</h5>
                        <div class="list-group">
                            <#list currentBooks as record>
                                <div class="list-group-item my-3 rounded-4 shadow-lg">
                                    <strong>${record.book.title}</strong><br>
                                    <small>Автор: ${record.book.author}</small><br>
                                    <small>Взята: ${record.issueDate?number_to_datetime?string("dd.MM.yyyy")}</small><br>
                                    <form action="/return/${record.book.id}" method="post" class="mt-2">
                                        <button type="submit" class="btn btn-sm btn-warning">Вернуть</button>
                                    </form>
                                </div>
                            </#list>
                        </div>
                    </div>
                <#else>
                    <div class="mt-3">
                        <p>У вас нет взятых книг.</p>
                    </div>
                </#if>

                <br>
                <a href="/books" class="btn btn-primary w-100 mb-2">Перейти к книгам</a>
                <a href="/logout" class="btn btn-outline-danger w-100">Выйти</a>
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