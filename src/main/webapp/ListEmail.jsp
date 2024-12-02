<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: salma
  Date: 01/12/2024
  Time: 12:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ListEmail.jsp</title>
</head>
<body>
<% String subscribe = (String) request.getAttribute("subscribe");
if ((subscribe != null) && !subscribe.isEmpty()){ %>
<h2 style="color:green">addresse <%=subscribe%> inscrite</h2>
<hr>
<a href="list-email-servlet">Affiche liste</a>
<%
    request.setAttribute("subscribe", "");
    }
%>
<% String unsubscribe = (String) request.getAttribute("unsubscribe");
    if ((unsubscribe != null) && !unsubscribe.isEmpty()){ %>
<h2 style="color:green">addresse <%=unsubscribe%> supprimée</h2>
<hr>
<a href="list-email-servlet">Affiche liste</a>
<%
        request.setAttribute("unsubscribe", "");
    }
%>
<% if(subscribe == null && unsubscribe==null) {%>
<h2>Membres:</h2>
<ul>
<%List<String> emailList = (List<String>) request.getAttribute("emailList");
if (emailList!= null && !emailList.isEmpty()){
    for (String email : emailList){  %>
    <li><%=  email %></li>
    <%}
} %>
</ul>
<hr>
<form method="post">
    <label>Enter your email address :
        <input type="email" name="email" required/>
    </label>
    <button type="submit" name="action" value="subscribe">subscribe</button>
    <button type="submit" name="action" value="unsubscribe">unsubscribe</button>
</form>
<%}%>
</body>
</html>
