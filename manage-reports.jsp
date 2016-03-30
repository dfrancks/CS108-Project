<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.Question" %>
<%@ page import= "finalproject.Question.QuestionType" %>
<%@ page import= "finalproject.Quiz" %>
<%@ page import= "finalproject.QuizManager" %>
<%@ page import= "finalproject.Report.ReportType" %>
<%@ page import= "finalproject.UserManager" %>
<%@ page import= "java.util.ArrayList" %>
<!DOCTYPE html>
<%
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	ArrayList<Quiz> reportedInappropriate = qm.getReportedQuizzes(ReportType.INAPPROPRIATE.ordinal());
	ArrayList<Quiz> reportedTarget = qm.getReportedQuizzes(ReportType.TARGETS_PERSON.ordinal());
	ArrayList<Quiz> reportedSpam = qm.getReportedQuizzes(ReportType.SPAM.ordinal());
%>
<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reported Quizzes</title>
</head>
<body>
<h1>Reported Quizzes</h1>
<div style="width:100%;"></div>
	<div style="width:33%; float:left;">
		<h2>Inappropriate/Offensive</h2>
		<ul>
			<%
				for (Quiz q : reportedInappropriate) {
					%>
					<a href="quiz-page.jsp?quizid=<%=q.getQuizID()%>">
						<div class="card" style="margin-left:-35px; width:90%;">
							<h3><%=q.getTitle()%></h3>
							<p><b>Description:</b><i> <%=q.getDescription()%></i></p>
							<p><b>Questions:</b></p>
							<ul style="margin-left:-30px;">
								<%
									for (Question question : q.getQuestions()) {
										if (question.getType().equals(QuestionType.FILL_IN_THE_BLANK)) {
											%>
											<li><p><%=question.getQuestionText()%> _________ <%=question.getSecondaryQuestionText()%></p></li>
											<%
										} else if (question.getType().equals(QuestionType.PICTURE_RESPONSE)) {%>
											<li><p><img src="<%=question.getQuestionText()%>" style="width:304px;height:228px;"></p></li>
										<%} else {
											%>
											<li><p><%=question.getQuestionText()%></p></li>
											<%
										}
									}
								%>
							</ul>
							<form action = "AdminServlet" method = "post">
								<input type = "submit" name = "admin" style="background-color:red;" value = "Delete Quiz">
								<input type = "submit" name = "admin" value="Verify Quiz">
								<input type = "hidden" name = "quizID" value = "<%=q.getQuizID()%>">
								<input type = "hidden" name = "adminPanel" value = "1">
							</form>
						</div>
					</a>
					<%
				}
			%>
		</ul>
	</div>
	<div style="width:33%; float:left;">
		<h2>Target Someone</h2>
		<ul>
			<%
				for (Quiz q : reportedTarget) {
					%>
					<a href="quiz-page.jsp?quizid=<%=q.getQuizID()%>" style="text-decoration:none;color: black;">
						<div class="card" style="margin-left:-35px; width:90%;">
							<h3><%=q.getTitle()%></h3>
							<p><b>Description:</b><i> <%=q.getDescription()%></i></p>
							<p><b>Questions:</b></p>
							<ul style="margin-left:-30px;">
								<%
									for (Question question : q.getQuestions()) {
										if (question.getType() == QuestionType.FILL_IN_THE_BLANK) {
											%>
											<li><p><%=question.getQuestionText()%> _________ <%=question.getSecondaryQuestionText()%></p></li>
											<%
										}  else if (question.getType().equals(QuestionType.PICTURE_RESPONSE)) {%>
										<li><p><img src="<%=question.getQuestionText()%>" style="width:304px;height:228px;"></p></li>
										<%} else {
											%>
											<li><p><%=question.getQuestionText()%></p></li>
											<%
										}
									}
								%>
							</ul>
							<form action = "AdminServlet" method = "post">
								<input type = "submit" name = "admin" style="background-color:red;" value = "Delete Quiz">
								<input type = "submit" name = "admin" value="Verify Quiz">
								<input type = "hidden" name = "quizID" value = "<%=q.getQuizID()%>">
								<input type = "hidden" name = "adminPanel" value = "1">
							</form>
						</div>
					</a>
					<%
				}
			%>
		</ul>
	</div>
	<div style="width:33%; float:left;">
		<h2>Spam</h2>
		<ul>
			<%
				for (Quiz q : reportedSpam) {
					%>
					<a href="quiz-page.jsp?quizid=<%=q.getQuizID()%>">
						<div class="card" style="margin-left:-30px; width:90%;">
							<h3><%=q.getTitle()%></h3>
							<p><b>Description:</b><i> <%=q.getDescription()%></i></p>
							<p><b>Questions:</b></p>
							<ul style="margin-left:-35px;">
								<%
									for (Question question : q.getQuestions()) {
										if (question.getType() == QuestionType.FILL_IN_THE_BLANK) {
											%>
											<li><p><%=question.getQuestionText()%> _________ <%=question.getSecondaryQuestionText()%></p></li>
											<%
										} else if (question.getType().equals(QuestionType.PICTURE_RESPONSE)) {%>
											<li><p><img src="<%=question.getQuestionText()%>" style="width:304px;height:228px;"></p></li>
										<%} else {
											%>
											<li><p><%=question.getQuestionText()%></p></li>
											<%
										}
									}
								%>
							</ul>
							<form action = "AdminServlet" method = "post">
								<input type = "submit" name = "admin" style="background-color:red;" value = "Delete Quiz">
								<input type = "submit" name = "admin" value="Verify Quiz">	
								<input type = "hidden" name = "quizID" value = "<%=q.getQuizID()%>">
								<input type = "hidden" name = "adminPanel" value = "1">
							</form>
						</div>
					</a>
					<%
				}
			%>
		</ul>
	</div>
</body>
</html>