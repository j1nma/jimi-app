<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Table ${table.name} </title>
</head>
<body>

<h1>TABLE ${table.name}</h1>

<c:if test="${table.status == 'Free'}">
    <h2>Table is <span style="color: green;">FREE</span>. Do you want to occupy it?
        <form action="/tables/${table.id}/status" method="post">
            <input value="1" name="status" type="hidden"/>
            <input type="submit" value="OCCUPY"/>
        </form>
    </h2>
</c:if>
<c:if test="${table.status == 'CleaningRequired'}">
    <h2>Table is <span style="color: yellow;">CLEANING</span>. Is it ready for use?
        <form action="/tables/${table.id}/status" method="post">
            <input value="2" name="status" type="hidden"/>
            <input type="submit" value="FREE"/>
        </form>
    </h2>
</c:if>
<c:if test="${table.status == 'Busy'}">
    <h2>Table is <span style="color: red;">BUSY</span>. Do you want to close it and clean?

        <form action="/tables/${table.id}/status" method="post">
            <input value="3" name="status" type="hidden"/>
            <input type="submit" value="CLEAN"/>
        </form>

    </h2>
</c:if>

<c:url value="/tables/${table.id}/set_diners" var="postPath"/>
<form:form modelAttribute="tableSetDinersForm" action="${postPath}" method="post">
    <form:label for="diners" path="diners">Number of diners</form:label>
    <form:input type="number" id="diners" name="diners" value="${table.diners}" path="diners"/>
    <form:errors path="diners" cssStyle="color: red;" element="p"/>
    <input type="submit" value="Set"/>
</form:form>

<table border="1">
    <tr>
        <td>Dish name</td>
        <td>Dish price</td>
        <td>Dish amount</td>
        <td>Dish total</td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <c:forEach items="${dishes}" var="dishEntry">
        <tr>
            <td><c:out value="${dishEntry.key.name}"/></td>
            <td><c:out value="${dishEntry.key.price}"/></td>
            <td><c:out value="${dishEntry.value}"/></td>
            <td><c:out value="${dishEntry.value * dishEntry.key.price}"/></td>
            <td>
                <form action="/tables/${table.id}/add_one_dish" method="post">
                    <input type="submit" value="+" />
                    <input type="hidden" value="${dishEntry.key.id}" name="dishid">
                </form>
            </td>
            <td>
                <form action="/tables/${table.id}/remove_one_dish" method="post">
                    <input type="submit" value="-" />
                    <input type="hidden" value="${dishEntry.key.id}" name="dishid">
                </form>
            </td>
            <td>
                <form action="/tables/${table.id}/remove_all_dish" method="post">
                    <input type="submit" value="X" style="color: red;"/>
                    <input type="hidden" value="${dishEntry.key.id}" name="dishid">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<form action="/tables/${table.id}/add_dish">
    <input type="submit" value="Add dish!" />
</form>

<h4><a href="/tables">Return to table list?</a></h4><br>

</body>
</html>