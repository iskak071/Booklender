<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${employee.fullName} - Профиль сотрудника</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4">
    <div class="d-flex justify-content-between mb-4">
        <a href="/employees" class="btn btn-outline-secondary">← К списку сотрудников</a>
        <a href="/books" class="btn btn-outline-primary">К книгам</a>
    </div>

    <h1 class="text-center mb-4">Профиль сотрудника</h1>

    <div class="card shadow-lg mb-4">
        <div class="card-body">
            <div class="row">
                <div class="col-md-3 text-center">
                    <div class="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center mb-3"
                         style="width: 120px; height: 120px; font-size: 3rem;">
                        ${employee.fullName?substring(0,1)?upper_case}
                    </div>
                </div>
                <div class="col-md-9">
                    <h2 class="card-title">${employee.fullName}</h2>
                    <p class="card-text"><strong>Email:</strong> ${employee.email}</p>
                    <p class="card-text"><strong>ID сотрудника:</strong> ${employee.id}</p>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="card shadow-lg h-100">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Текущие книги на руках (${currentBooks?size})</h4>
                </div>
                <div class="card-body">
                    <#if currentBooks?? && currentBooks?size gt 0>
                        <div class="list-group">
                            <#list currentBooks as record>
                                <div class="list-group-item mb-2">
                                    <h5 class="mb-1">
                                        <a href="/book/${record.book.id}">${record.book.title}</a>
                                    </h5>
                                    <p class="mb-1"><small>Автор: ${record.book.author}</small></p>
                                    <p class="mb-1"><small>Взята: ${record.issueDate?number_to_datetime?string("dd.MM.yyyy HH:mm")}</small></p>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <p class="text-muted">У сотрудника нет текущих книг</p>
                    </#if>
                </div>
            </div>
        </div>

        <div class="col-md-6 mt-4 mt-md-0">
            <div class="card shadow-lg h-100">
                <div class="card-header bg-secondary text-white">
                    <h4 class="mb-0">История книг (${pastBooks?size})</h4>
                </div>
                <div class="card-body">
                    <#if pastBooks?? && pastBooks?size gt 0>
                        <div class="list-group">
                            <#list pastBooks as record>
                                <div class="list-group-item mb-2">
                                    <h5 class="mb-1">
                                        <a href="/book/${record.book.id}">${record.book.title}</a>
                                    </h5>
                                    <p class="mb-1"><small>Автор: ${record.book.author}</small></p>
                                    <p class="mb-1">
                                        <small>Взята: ${record.issueDate?number_to_datetime?string("dd.MM.yyyy")}</small><br>
                                        <small>Возвращена: ${record.returnDate?number_to_datetime?string("dd.MM.yyyy")}</small>
                                    </p>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <p class="text-muted">У сотрудника нет истории книг</p>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>