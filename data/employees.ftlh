<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Список сотрудников</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4">
    <div class="d-flex justify-content-end mb-4">
        <a href="/profile" class="btn btn-success me-2">Перейти в профиль</a>
        <a href="/books" class="btn btn-primary">К книгам</a>
    </div>

    <h1 class="text-center">Наши сотрудники: </h1>

    <div class="row d-flex justify-content-around mt-5">
        <#if employeesWithBooks??>
            <#list employeesWithBooks as empInfo>
            <div class="col-md-4 col-sm-6 mb-4">
                <div class="card h-100 shadow-lg p-3">

                    <div class="d-flex justify-content-center mb-3">
                        <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center"
                             style="width: 150px; height: 150px; font-size: 3.5rem;">
                            ${empInfo.employee.fullName?substring(0,1)?upper_case}
                        </div>
                    </div>

                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title text-center">
                            <a href="/employee/${empInfo.employee.id}">${empInfo.employee.fullName}</a>
                        </h5>
                        <p class="card-text text-muted text-center">
                            ${empInfo.employee.email}
                        </p>

                        <div class="mt-auto pt-2">
                            <strong>Книги на руках:</strong>
                            <#if empInfo.bookCount == 0>
                                <span class="badge bg-secondary">Нет книг</span>
                            <#else>
                                <div class="mt-2">
                                    <#list empInfo.currentBooks as bookRecord>
                                        <div class="badge bg-info mb-1">
                                            ${bookRecord.book.title}
                                        </div>
                                    </#list>
                                </div>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
            </#list>
        <#else>
            <div class="col-12">
                <div class="alert alert-info text-center">
                    Нет данных о сотрудниках
                </div>
            </div>
        </#if>
    </div>
</div>
</body>
</html>