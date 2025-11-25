<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Каталог Библиотеки</title>
    <link rel="stylesheet" href="./css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4">
    <h1 class="text-center">Добро пожаловать в Booklender!</h1>

    <div class="row d-flex justify-content-around mt-5">

        <#list books as book>
        <div class="col-md-4 col-sm-6 mb-4">
            <div class="card h-100 shadow-lg p-3">

                <img src="${book.image}" class="card-img-top" alt="Обложка книги ${book.title}"
                     style="height: 250px; object-fit: cover;">

                <div class="card-body d-flex flex-column">
                    <h5 class="card-title">
                        <a href="/book/${book.id}">${book.title}</a>
                    </h5>
                    <p class="card-text text-muted">
                        ${book.author}
                    </p>

                    <div class="mt-auto pt-2">
                        <strong>Статус:</strong>
                        <#if book.currentHolder == "Available">
                        <span class="badge bg-success status-available">Доступна</span>
                        <#else>
                        <span class="badge bg-danger status-borrowed">
                                    Выдана:
                                    <a href="/employee/${book.currentHolderId!}" class="text-white">${book.currentHolder}</a>
                                </span>
                        </#if> </div>
                </div>
            </div>
        </div>
        </#list> </div>
</div>
</body>
</html>