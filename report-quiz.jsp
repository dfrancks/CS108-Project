<!DOCTYPE html>
<%@page import="finalproject.UserManager"%>
<%@page import="finalproject.QuizManager"%>
<%@page import="finalproject.Report.ReportType"%>
<html>
<%
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
%>
<%@include file="header.jsp" %>
	<head>
	<link rel="stylesheet" href="master.css">
	<meta charset="UTF-8" />
	<title>Report Quiz</title>
	</head>
	<body>
		<h1>Report Quiz?</h1>
		<p><i>Please provide a reason for reporting this quiz. The additional information will help expedite the review process.</i></p>
		<form action = "ReportServlet" method = "post">
			<div><input type="radio" name="report" value="inappropriate" checked="checked">Inappropriate/offensive content</div>
			<div><input type="radio" name="report" value="targets">Targets someone</div>
			<div><input type="radio" name="report" value="spam">Spam</div>
			<input type="hidden" name="quizID" value="<%=request.getParameter("quizID")%>">
			<input type="submit" name="submit" value="Report" style="background-color: red; margin-top:20px;">
		</form>
	</body>
</html>