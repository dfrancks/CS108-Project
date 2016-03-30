<%@page import="finalproject.QuizManager"%>
<%@page import="finalproject.Result"%>
<%@page import="finalproject.ResultManager"%>
<%@page import="finalproject.UserManager"%>
<%@page import="finalproject.Quiz"%>
<%@ page import= "java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<%
	final int MAX_NUM_DISPLAY = 4; 
	final int TIME_INTERVAL_MIN = 15;
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	Quiz cur = qm.quizForQuizID(Integer.parseInt(request.getParameter("quizid")));
	
	// Get various result sets
	ArrayList<Result> currUserResults = rm.getUserResultsOnQuiz(um.getCurrentUserID(request), cur.getQuizID());
	ArrayList<Result> highScoreResults = rm.highScoresForQuiz(cur.getQuizID());
	ArrayList<Result> recentTopPerformers = rm.recentTopPerformers(cur.getQuizID(), TIME_INTERVAL_MIN);
	ArrayList<Result> recentResults = rm.allResultsForQuiz(cur.getQuizID());
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=cur.getTitle()%></title>
</head>
<body>
<h1><%=cur.getTitle()%></h1>

<% if (um.isAdminForUserID(um.getCurrentUserID(request))){%>
	<form action = "AdminServlet" method = "post">
		<input type = "submit" name = "admin" style="background-color:red;" value = "Delete Quiz">
		<input type = "submit" name = "admin" style="background-color:red;" value = "Delete Results">	
		<input type = "hidden" name = "quizID" value = "<%=cur.getQuizID()%>">
	</form>
<%}%>

<form action="take-quiz.jsp" method="get">
	<input type = "hidden" name = "quizID" value = "<%=cur.getQuizID()%>">
	<input type = "submit" value = "Take Quiz">
</form>

<%
	if (!qm.userHasReportedQuiz(um.getCurrentUserID(request), cur.getQuizID())) {
		%>
		<form action="report-quiz.jsp" method="post">
			<input type="hidden" name="quizID" value="<%=cur.getQuizID()%>">
			<input type="submit" style="background-color: red;" value="Report Quiz">
		</form>
		<%
	} else {
		%>
		<p><i>Thank you for reporting this quiz. Our team will review it shortly.</i></p>
		<%
	}
%>

<div class=quizInfo style="width:60%;float:left;">
	<div class=card>
		<p><b>Author:</b> <%=um.usernameForUserID(cur.getAuthor())%>
		<p><b>Quiz Description:</b> <%=cur.getDescription()%></p>
		<%int count = 0;
		for(Result r: recentResults){
			count += r.getScore();
		}
		double avgscore = ((double)count)/((double)recentResults.size());
		if (recentResults.size() != 0){%>
			<p><b>Average Score:</b> <%=avgscore%><p>
		<%}%>
	</div>
	
	<div class=card>
		<h2>Your Recent Results</h2>
		<ul>
			<%
				for (int i = 0; i < Math.min(MAX_NUM_DISPLAY, currUserResults.size()); i++) {
					Result r = currUserResults.get(i);
					out.println("<li><a href = user-page.jsp?userID="+ r.getUserID() + ">" + um.usernameForUserID(r.getUserID()) + "</a>: " + r.getScore() + "</li>");
				}
				if (currUserResults.size() == 0) {
					out.println("<p><i>You have not yet taken this quiz.</i></p>");
				}
			%>
		</ul>
	</div>
	
	<div class=card>
		<h2>High Scores</h2>
		<ul>
			<%
				for (int i = 0; i < Math.min(MAX_NUM_DISPLAY, highScoreResults.size()); i++) {
					Result r = highScoreResults.get(i);
					out.println("<li><a href = user-page.jsp?userID="+ r.getUserID() + ">" + um.usernameForUserID(r.getUserID()) + "</a>: " + r.getScore() + " points</li>");
				}
				if (highScoreResults.size() == 0) {
					out.println("<p><i>No users have taken this quiz. Be the first!</i></p>");
				}
			%>
		</ul>
	</div>
	
	<div class=card>
		<h2>Top Performers (Last <%=TIME_INTERVAL_MIN%> Minutes)</h2>
		<ul>
			<%
				for (int i = 0; i < Math.min(MAX_NUM_DISPLAY, recentTopPerformers.size()); i++) {
					Result r = recentTopPerformers.get(i);
					out.println("<li><a href = user-page.jsp?userID="+ r.getUserID() + ">" + um.usernameForUserID(r.getUserID()) + "</a>: " + r.getScore() + " points</li>");
				}
				if (recentTopPerformers.size() == 0) {
					out.println("<p><i>No users have taken this quiz in the last 15 minutes.</i></p>");
				}
			%>
		</ul>
	</div>
	
	<div class=card>
		<h2>All Recent Results</h2>
		<ul>
			<%
				for (int i = 0; i < Math.min(MAX_NUM_DISPLAY, recentResults.size()); i++) {
					Result r  = recentResults.get(i);
					out.println("<li><a href = user-page.jsp?userID="+ r.getUserID() + ">" + um.usernameForUserID(r.getUserID()) + "</a>: " + r.getScore() + " points</li>");
				}
				if (recentResults.size() == 0) {
					out.println("<p><i>No user have taken this quiz. Be the first!</i></p>");
				}
			%>
		</ul>
	</div>
</div>	
</body>
</html>