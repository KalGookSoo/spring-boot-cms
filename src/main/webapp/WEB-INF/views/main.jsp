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
<%@ include file="header.jsp" %>

<%@ include file="nav.jsp" %>

<main class="container border-red">
    <div class="row">
        <div class="col-12">
            <h2>본문 영역</h2>
            <p>이곳에 본문 내용을 넣어주세요.</p>
        </div>
    </div>
</main>

<%@ include file="nav.jsp" %>

<!-- Bootstrap JS -->
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>