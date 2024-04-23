<%--suppress HtmlUnknownTarget --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="${cookie.lang.value}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><spring:message code="label.button.sign.in"/></title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/signin.css" rel="stylesheet">

    <style>
        .error {
            color: red;
            font-size: small;
            margin-left: 5px;
        }
    </style>

    <c:if test="${not empty message}">
        <script>
            alert('${message}');
        </script>
    </c:if>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="text-center"><spring:message code="label.button.sign.in"/></h2>
            <form:form action="${pageContext.request.contextPath}/sign-in" method="post" class="mt-4" modelAttribute="command">
                <div class="form-group">
                    <label for="username"><spring:message code="user.username"/></label>
                    <form:input path="username" id="username" class="form-control"/>
                    <form:errors path="username" class="error"/>
                </div>
                <div class="form-group">
                    <label for="password"><spring:message code="user.password"/></label>
                    <form:password path="password" id="password" class="form-control"/>
                    <form:errors path="password" class="error"/>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block"><spring:message code="label.button.sign.in"/></button>
                    <a href="${pageContext.request.contextPath}/sign-up" class="btn btn-link btn-block"><spring:message code="label.button.sign.up"/></a>
                </div>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>