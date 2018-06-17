<%@ page import="edu.itba.paw.jimi.models.TableStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="BusyCode" value="<%=TableStatus.BUSY.ordinal()%>"/>
<c:set var="PayingCode" value="<%=TableStatus.PAYING.ordinal()%>"/>


<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <title>Jimi Rest</title>
    <!-- google font -->
    <link href="<c:url value="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700"/>" rel="stylesheet"
          type="text/css"/>
    <!-- icons -->
    <link href="<c:url value="/webjars/font-awesome/4.7.0/css/font-awesome.min.css"/>" rel="stylesheet"
          type="text/css"/>

    <!--Material-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <%--<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">--%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css"
          integrity="sha256-e22BQKCF7bb/h/4MFJ1a4lTRR2OuAe8Hxa/3tgU5Taw=" crossorigin="anonymous"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/11.1.0/nouislider.min.css"
          integrity="sha256-tkYpq+Xdq4PQNNGRDPtH3G55auZB4+kh/RA80Abngaw=" crossorigin="anonymous"/>

    <link href="<c:url value="/resources/css/header.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/resources/css/tables/add_dish.css"/>" rel="stylesheet" type="text/css">
    <!-- Favicon -->
    <link rel="shortcut icon" type="image/x-icon"
          href="${pageContext.request.contextPath}/resources/img/jimi-rest/favicon.ico"/>
</head>

<body>

<jsp:include page="/WEB-INF/jsp/header2.jsp"/>

<%-- TODO hay que hacer responsive esta tabla --%>
<div class="table-container">
    <div class="card">
        <div class="card-head">
            <div class="row">
                <div class="col s6">
                    <span>
                        <spring:message code="table.please_select_dish"/>${table.name}
                    </span>
                </div>
            </div>
        </div>
        <div class="card-content">
            <div class="row">
                <div class="col-sm-12">

                    <c:choose>
                        <c:when test="${dishes.size() > 0}">

                            <c:url value="/tables/${table.id}/add_dish" var="postPath"/>
                            <form:form modelAttribute="tableAddDishForm" action="${postPath}"
                                       method="post">

                                <div class="row">
                                    <div class="input-field col s12">

                                        <form:select id="dishid" name="dishid" path="dishid">
                                            <c:forEach items="${dishes}" var="dish">
                                                <option value="${dish.id}"
                                                        data-max="${dish.stock}">${dish.name}</option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12">
                                        <div id="range-input-container"></div>
                                        <form:input type="number" id="amount" path="amount"
                                                    step="1" min="1"
                                                    max="100"
                                                    value="1" class="hide"/>
                                        <form:errors path="amount" cssClass="formError" element="p"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <input type="submit" value="<spring:message code="dish.add"/>"
                                           class="waves-effect waves-light btn"/>
                                </div>

                            </form:form>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info text-center">
                                <strong><spring:message code="ouch"/></strong>
                                <spring:message
                                        code="dishes.no_dishes"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="card-action">

        </div>
    </div>
</div>


<!-- start js include path -->
<%--<script defer src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>--%>
<%--<script defer src="https://code.getmdl.io/1.3.0/material.min.js">--%>

<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/js/materialize.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/11.1.0/nouislider.min.js"
        integrity="sha256-IB524Svhneql+nv1wQV7OKsccHNhx8OvsGmbF6WCaM0=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/wnumb/1.1.0/wNumb.min.js"
        integrity="sha256-HT7c4lBipI1Hkl/uvUrU1HQx4WF3oQnSafPjgR9Cn8A=" crossorigin="anonymous"></script>
<script src="<c:url value="/resources/js/tables/add_dish.js"/>"></script>
<!-- end js include path -->
</body>
</html>
