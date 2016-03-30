<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.User" %> 
<%@ page import= "finalproject.UserManager" %>
<%@ page import= "java.util.ArrayList" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	ArrayList<User> displayedUsers;
	if (request.getAttribute("userList") == null) {
		displayedUsers = um.getAllUsers(um.getCurrentUserID(request));
	} else {
		displayedUsers = (ArrayList<User>) request.getAttribute("userList");
	}
%>
<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Users</title>
</head>
<body id="users">
<script type="text/javascript">
	function searchUsers() {
		var searchText = document.getElementById("searchText").value;
		var<%%>
	}
</script>
<h1>Search Users</h1>
	<form action="UserSearchServlet" method="get">
		<% 
			if (request.getParameter("select") != null) {
				out.println("<input type=hidden name=select value=1>");
			}
		%>
		<input id=searchText type=text name=searchText size=30 maxlength=100>
		<input type=submit value=Search>
	</form>
<ul style="columns: 4;-webkit-columns: 4;-moz-columns: 4;">
	<%
		for (User u: displayedUsers) {
			if (request.getParameter("select") != null) {
				out.println("<li><a href=\"send-message.jsp?toID=" + u.getID() + "\">" + u.getUsername() + "</a></li>");
			} else {
				out.println("<li><a href=\"user-page.jsp?userID=" + u.getID() + "\">" + u.getUsername() + "</a></li>");
			}
		}
	%>
</ul>
<p><i><%if (displayedUsers.isEmpty()) out.println("Search returned no users");%></i></p>
</body>
</html>