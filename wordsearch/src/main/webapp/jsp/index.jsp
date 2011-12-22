<%@ page language="java" 
contentType="text/html; charset=UTF-8" 
pageEncoding="UTF-8"
import="java.sql.*, java.io.*, java.util.*" 
isELIgnored="false"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Word Search</title>
</head>
<body>
<% 
String pass = (String) request.getAttribute("pass");
boolean isOK = true;
if (pass == null) {
	isOK = false;
}
%>
<%
if (isOK) {
%>
<h2>Word Search</h2>
<form method="GET" action="index.jsp" enctype="application/x-www-form-urlencoded" style="margin-bottom: 1em; ">
<input type="text" name="word" value="${word}" size="10" tabindex="1">
<input type="submit" value="search" name="action">
</form>


<%
List resultList = (List) request.getAttribute("resultList");
Iterator it = resultList.iterator();
if (resultList.size() == 0) {
%>
<div>Oh, find nothing!</div>
<% } else { 
int wt = (Integer) request.getAttribute("wordTotal");
int pt = (Integer) request.getAttribute("pageTotal");
int cp = (Integer) request.getAttribute("currentPage");
%>
<div>
<% if (cp > 1) { %>
<a href="index.jsp?word=${ word }&action=search&p=${ 1 }"> first </a>
<% } else { %>
 first
<% } %>
|
<% if (cp > 1) {  %>
<a href="index.jsp?word=${ word }&action=search&p=${ currentPage - 1 }"> prev </a>
<% } else { %>
 prev 
<% } %>
|
<% if (cp < pt) { %>
<a href="index.jsp?word=${ word }&action=search&p=${ currentPage + 1 }"> next </a>
<% } else { %>
 next 
<% } %>
|
<% if (cp < pt) { %>
<a href="index.jsp?word=${ word }&action=search&p=${ pageTotal }"> last </a>
<% } else { %>
 last
<% } %>
</div>
<table border="1">
<%
while (it.hasNext()) {  
	String[] item = (String[])it.next();
%>
<tr>
<td><font size="2"><%= item[0] %></font></td>
<td><%= item[1] %></td>
</tr>
<% } %>
</table>
<div>
wordTotal:${ wordTotal }, pageTotal:${ pageTotal }, currentPage:${ currentPage }
</div>
<% } %>

<%
Long startTime = (Long) request.getAttribute("startTime");
double qtime = (Calendar.getInstance().getTimeInMillis() - startTime) / 1000.0;
%>
<div align="center">
<font size="2">Query time <%=qtime %> second</font><br />
<font size="2">Word search, by <a href="http://weimingtom.iteye.com/">weimingtom</a>, 2011</font><br />
</div>

<% } else {%>
<div> Don't access this page directly !!! </div>
<% } %>
</body>
</html>
