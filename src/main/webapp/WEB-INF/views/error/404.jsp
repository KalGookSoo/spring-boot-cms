<%--suppress HtmlUnknownTarget --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="${cookie.lang.value}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="author" content="">
    <link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" />

    <title>Error 404</title>
    <style>
        body {
            text-align: center;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            font-family: Arial, sans-serif;
            color: #333;
            background-color: #f6f8fa;
        }
        h1 {
            font-size: 50px;
            font-weight: bold;
        }
        p {
            color: #74757d;
        }
        a {
            color: #0366d6;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div>
    <h1><spring:message code="error.not.found.title" /></h1>
    <p><spring:message code="error.not.found.message" /></p>
    <a href="${pageContext.request.contextPath}/"><spring:message code="error.not.found.link.text" /></a>
</div>
</body>
</html>