<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.Quiz" %> 
<%@ page import= "finalproject.QuizManager" %>
<%@ page import= "java.util.ArrayList" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	ArrayList<Quiz> displayedQuizzes;
	if (request.getAttribute("quizList") == null) {
		displayedQuizzes = qm.getAllQuizzes();
	} else {
		displayedQuizzes = (ArrayList<Quiz>) request.getAttribute("quizList");
	}
%>
<html>
<link rel="stylesheet" href="master.css">
<%@include file="header.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quizzes</title>
</head>
<body id="quizzes">
<script type="text/javascript">
	function searchQuizzes() {
		var searchText = document.getElementById("searchText").value;
		var<%%>
	}
</script>
<h1>Search Quizzes</h1>
	<form action="QuizSearchServlet" method="get">
		<% 
			if (request.getParameter("select") != null) {
				out.println("<input type=hidden name=select value=1>");
			}
		%>
		<input id=searchText type=text name=searchText size=30 maxlength=100>
		<input type=submit value=Search>
	</form>
<form action="create-quiz.jsp" method = "post">
	<input type = "submit" value = "Create Quiz"/>
</form>
<ul style="columns: 4;-webkit-columns: 4;-moz-columns: 4;">
	<%
		for (Quiz q: displayedQuizzes) {
			out.println("<li><a href = quiz-page.jsp?quizid=" + q.getQuizID() + ">" + q.getTitle() + "</li>");
		}
	%>
</ul>
<p><i><%if (displayedQuizzes.isEmpty()) out.println("Search returned no Quizzes");%></i></p>
</body>
</html>