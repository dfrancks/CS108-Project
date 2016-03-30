<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.UserManager" %>
<!DOCTYPE html>
<%
	UserManager manager = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
%>
<html>
<head>
<link rel="stylesheet" href="master.css">
</head>
<body>
<ul class=headerlist>
	<li><a href="homepage.jsp" id="homenav">Home</a></li>
	<li><a href="all-quizzes.jsp" id="quiznav">Quizzes</a></li>
	<li><a href="all-users.jsp" id="usernav">Users</a></li>
	<li><a href="mailbox.jsp" id="mailnav">Mailbox</a></li>
	<li><a href="user-page.jsp?userID=<%=manager.getCurrentUserID(request)%>" id="profilenav">Your Profile</a></li>
	<%
  		UserManager mgr = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
  		if (mgr.isAdminForUserID(mgr.getCurrentUserID(request))) {
  			out.println("<li id=headeritem><a id=adminnav href=\"admin-panel.jsp\">Admin Panel</a></li>");
  		}
	%>
</ul>
<div class=spacer></div>
</body>
</html>
