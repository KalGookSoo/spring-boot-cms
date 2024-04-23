<%--suppress HtmlUnknownTarget --%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="${cookie.lang.value}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>메인페이지</title>

    <!-- Bootstrap CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

    <style>
        .border-red {
            border: 1px dashed red;
            box-sizing: border-box;
        }
    </style>

    <%--@elvariable id="message" type="java.lang.String"--%>
    <c:if test="${not empty message}">
        <script>
            alert('${message}');
        </script>
    </c:if>
</head>
<body>
<header class="border-red d-flex justify-content-between align-items-center">
    <h1>Logo Text or Image Here</h1>
    <div>
        <sec:authorize access="isAuthenticated()">
            <span><sec:authentication property="username"/></span>
            <form:form action="${pageContext.request.contextPath}/sign-out" method="post" cssClass="d-inline float-right">
                <input type="submit" value="<spring:message code="label.button.sign.out"/>" class="btn btn-danger" />
            </form:form>
        </sec:authorize>
        <sec:authorize access="!isAuthenticated()">
            <a href="${pageContext.request.contextPath}/sign-in" class="btn btn-primary"><spring:message code="label.button.sign.in"/></a>
            <a href="${pageContext.request.contextPath}/sign-up" class="btn btn-secondary"><spring:message code="label.button.sign.up"/></a>
        </sec:authorize>
    </div>
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