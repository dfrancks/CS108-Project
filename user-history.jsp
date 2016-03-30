<!DOCTYPE html>
<%@page import="finalproject.QuizManager"%>
<%@page import="finalproject.Quiz"%>
<%@page import="finalproject.ResultManager"%>
<%@page import="finalproject.Result"%>
<%@page import="finalproject.User"%>
<%@page import="java.util.ArrayList"%>
<html>
<%@include file="header.jsp" %>
	<head>
	<link rel="stylesheet" href="master.css">
	<meta charset="UTF-8" />
	<%QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	String username = um.usernameForUserID(Integer.parseInt(request.getParameter("userID")));
	%>
		<title><%=username %>'s Quiz History</title>
	</head>
	<body>
		<h1><%=username %>'s Quiz History</h1>
		<ul>
			<%ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
			ArrayList<Result> results = rm.recentScoresForUser(Integer.parseInt(request.getParameter("userID")));
			for (Result r: results){
				out.println("<li><a href=\"quiz-page.jsp?quizid=" + r.getQuizID() + "\">" + qm.quizForQuizID(r.getQuizID()).getTitle() + "</a>: " + r.getScore() + " points</li>");
			}%>
		</ul>
		<% if (results.size() == 0){%>
			<p><%=username%> has yet to take a quiz!</p>
		<%}%>	
</body>
</html>