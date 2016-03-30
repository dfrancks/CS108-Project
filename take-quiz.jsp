<%@page import="finalproject.Answer"%>
<%@page import="finalproject.Question"%>
<%@page import="finalproject.Quiz"%>
<%@page import="finalproject.QuizManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%
		QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		Quiz currentQuiz = qm.getCurrentlyTakenQuiz(quizID);
		if (currentQuiz == null) {
			currentQuiz = qm.quizForQuizID(Integer.parseInt(request.getParameter("quizID")));
		}
		String s = request.getParameter("questionCount");
		int count = 0;
		if(s != null) count = Integer.parseInt(s);
		String value = (count != currentQuiz.getSize() - 1) ? "Submit Question" : "Submit Quiz";
		String scoreString = (String) request.getParameter("currentScore");
		if (scoreString == null) scoreString = "0";
		String text = (String) request.getAttribute("evaluationText");
		long startedTime;
		if (count == 0) {
			startedTime = System.currentTimeMillis();
		} else {
			startedTime = Long.parseLong(request.getParameter("startedTime"));
		}
	%>
<title><%=currentQuiz.getTitle()%></title>
</head>
<h1><%=currentQuiz.getTitle()%></h1>
<body>
	<% 
		ArrayList<Question> questions = currentQuiz.getQuestions();
		if (currentQuiz.getSinglePage()){ %>
		<form action = "TakeQuizServlet" method = "post">
		<%for (int i = 0; i < questions.size(); i++){%>
			<jsp:include page="question.jsp">
				<jsp:param name="quizID" value="<%= quizID%>"/>
  				<jsp:param name="questionIndex" value="<%=i%>"/>
			</jsp:include>
			<p></p>
			<%} %>
		<input type = "hidden" name = "quizID" value = "<%=currentQuiz.getQuizID()%>">
		<input type = "hidden" name = "startedTime" value = "<%= startedTime %>">
		<input type = "submit" value = "Submit Quiz">
		</form>
		<% } else {
			if(currentQuiz.getImmediateCorrection()){ 
			 	if (text != null) { %>
			 		<p><%= text %> </p>
			 <% } 
			} %>
			<form action = "TakeQuizServlet" method = "post">
				<jsp:include page="question.jsp">
  					<jsp:param name="questionIndex" value="<%=count%>"/>
  					<jsp:param name="quizID" value="<%=currentQuiz.getQuizID()%>"/>
				</jsp:include> 
			<input type = "hidden" name = "quizID" value = "<%=currentQuiz.getQuizID()%>">
			<input type = "hidden" name = "questionCount" value = "<%=count + 1%>">
			<input type = "hidden" name = "currentScore" value = "<%= scoreString %>">
			<input type = "hidden" name = "startedTime" value = "<%= startedTime %>">
			<input type = "submit" value = "<%= value %>">
			</form>
		<% }%>
</body>
</html>