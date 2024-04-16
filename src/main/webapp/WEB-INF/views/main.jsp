<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="${cookie.lang.value}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>메인페이지</title>

    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <style>
        .border-red {
            border: 1px dashed red;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
<header class="border-red">
    <h1>헤더 영역</h1>
</header>

<nav class="navbar navbar-expand-lg navbar-light bg-light border-red">
    <a class="navbar-brand" href="#">네비게이션 바</a>
</nav>

<main class="container border-red">
    <div class="row">
        <div class="col-12">
            <h2>본문 영역</h2>
            <p>이곳에 본문 내용을 넣어주세요.</p>
        </div>
    </div>
</main>

<footer class="footer bg-light border-red">
    <div class="container">
        <span class="text-muted">풋터 영역</span>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>