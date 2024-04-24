<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="${cookie.lang.value}">
    <header class="border-red d-flex justify-content-between align-items-center">
        <h1>Logo Text or Image Here</h1>
        <div>
            <sec:authorize access="isAuthenticated()">
                <sec:authentication property="principal.username" var="username"/>
                <span><spring:message code="message.welcome" arguments="${username}"/></span>
                <sec:authentication property="principal.id" var="id"/>
                <a href="${pageContext.request.contextPath}/users/${id}/edit" class="btn btn-outline-primary"></a>
                <form:form action="${pageContext.request.contextPath}/sign-out" method="post" cssClass="d-inline float-right">
                    <input type="submit" value="<spring:message code="label.sign.out"/>" class="btn btn-danger" />
                </form:form>
            </sec:authorize>
            <sec:authorize access="!isAuthenticated()">
                <a href="${pageContext.request.contextPath}/sign-in" class="btn btn-primary"><spring:message code="label.sign.in"/></a>
                <a href="${pageContext.request.contextPath}/sign-up" class="btn btn-secondary"><spring:message code="label.sign.up"/></a>
            </sec:authorize>
        </div>
</header>
</html>