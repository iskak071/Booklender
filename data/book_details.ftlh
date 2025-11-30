<!DOCTYPE html>

<html lang="ru">
<head>
    <title>${book.title}</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4">
    <a href="/books" class="btn btn-outline-secondary mb-4">Вернуться к каталогу книг</a>

    <h1 class="mb-4">Книга: ${book.title}</h1>

    <div class="row">

        <div class="col-md-4">
            <img src="${book.image}" class="img-fluid rounded-2 shadow-lg border" alt="Обложка книги ${book.title}" style="max-height: 400px; object-fit: cover;">
        </div>

        <div class="col-md-8">

            <ul class="list-group list-group-flush mb-4 mb-4">
                <li class="card p-2 shadow-sm bg-light my-1"><strong>Автор:</strong> ${book.author}</li>
                <li class="card p-2 shadow-sm bg-light my-1"><strong>ID книги:</strong> ${book.id}</li>
            </ul>

            <#if book.currentHolder == "Available">
            <div class="card p-3 shadow-sm bg-light mb-4">
                <h2 class="h5 mb-3">Текущий Статус и Действия</h2>
                <p><span class="badge bg-success fs-6 p-3">Доступна</span></p>

                <div class="mt-3">
                    <form action="/borrow/${book.id}" method="post">
                        <button type="submit" class="btn btn-success btn-lg w-100">Взять книгу</button>
                    </form>
                </div>
            </div>

            <#else>
            <div class="card p-3 shadow-sm bg-light mb-4">
                <h2 class="h5 mb-3">Текущий Статус и Действия</h2>
                <p>
                    <span class="badge bg-danger fs-6 p-3">
                        Выдана: ${book.currentHolder}
                    </span>
                </p>

                <div class="mt-3">
                    <form action="/return/${book.id}" method="post">
                        <button type="submit" class="btn btn-warning btn-lg w-100">Вернуть книгу</button>
                    </form>
                </div>
            </div>
        </#if>
    </div>
</div>
</div>
</body>
</html>