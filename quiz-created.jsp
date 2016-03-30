<!DOCTYPE html>
<%@page import="finalproject.QuizManager"%>
<%@page import="finalproject.Quiz"%>
<html>
<%@include file="header.jsp" %>
	<head>
	<link rel="stylesheet" href="master.css">
	<meta charset="UTF-8" />
		<title>Success!</title>
	</head>
	<body>
	<% QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
		Quiz cur = qm.quizForQuizID(Integer.parseInt(request.getParameter("quizID")));
		%>
		<h1>You've just created <a href = quiz-page.jsp?quizid=<%=cur.getQuizID()%>><%=cur.getTitle()%></a></h1>
		<%if (!request.getParameter("ach").equals("")){ %>
			<h2>Congrats! You've earned an achievement.</h2>
			<p><%=request.getParameter("ach")%></p>
		<%}%>	
		</body>
</html>