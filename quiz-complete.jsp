<!DOCTYPE html>
<%@page import="finalproject.Question"%>
<%@page import="finalproject.Question.QuestionType"%>
<%@page import="finalproject.UserManager"%>
<%@page import="finalproject.QuizManager"%>
<%@page import="finalproject.ResultManager"%>
<%@page import="finalproject.Quiz"%>
<%@page import="finalproject.QuestionResult"%>
<%@page import="finalproject.AchievementManager"%>
<%@page import="finalproject.Achievement"%>
<%@page import="java.util.ArrayList"%>
<html>
<%@include file="header.jsp" %>
	<head>
	<link rel="stylesheet" href="master.css">
	<meta charset="UTF-8" />
		<%
			QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
			int quizID = Integer.parseInt(request.getParameter("quizID"));
			Quiz currentQuiz = qm.getCurrentlyTakenQuiz(quizID);
			if(currentQuiz == null) return;
			ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
			QuestionResult[] qr = rm.getQuestionResults(quizID);
			String text = (String) request.getAttribute("evaluationText");
			Long elapsedTime = (Long) request.getAttribute("elapsedTime");
			request.removeAttribute("elapsedTime");
		%>
		<title>Results for <%=currentQuiz.getTitle()%></title>
	</head>
	<body>
		<% if(currentQuiz.getImmediateCorrection() && !currentQuiz.getSinglePage()){ 
			 	if (text != null) { %>
			 		<p><%= text %> </p>
			 <% } 
			} %>

		<h1>Results for <%= currentQuiz.getTitle() %></h1>
		<p>You scored <%= request.getParameter("score") %> in <%= elapsedTime %> sec.</p>
		<% 
			for (QuestionResult result : qr){ 
				if (result.getType().equals(Question.QuestionType.PICTURE_RESPONSE)){%>
					<p><b>Question <%=result.getQuestionIndex() + 1 %>:</b><img src="<%=result.getQuestionText()%>" style="width:304px;height:228px;"></p>

				<%} else {%>
					<p><b>Question <%=result.getQuestionIndex() + 1 %>:</b> <%=result.getQuestionText()%>
				<%}
				if(result.getType() == Question.QuestionType.FILL_IN_THE_BLANK) { %>
					_________ <%=result.getSecQuestionText()%></p>
				<% } else { %>
					</p>
				<% } %>
				<% if(result.getScore() > 0){ %>
					<p>You answered <%=result.getUserAnswer()%>, which is correct. Nice!</p>
				<% } else { %>
					<p>You answered <%=result.getUserAnswer()%>, which is incorrect. The correct answer 
					is <%= result.getCorrectAnswers().get(0).getAnswerText()%>.</p>
				<% }
			}
		%>
		<%
		UserManager userManager = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);	
		AchievementManager am = (AchievementManager)request.getServletContext().getAttribute(AchievementManager.ATTR_NAME);
		Achievement hs = am.checkHighScoreAchievement(userManager.getCurrentUserID(request), currentQuiz.getQuizID(), Integer.parseInt(request.getParameter("score")));
		boolean takequiz = request.getParameter("ach").equals("");
		int maxscore = 0;
		ArrayList<Question> questions = currentQuiz.getQuestions();
		for (int i = 0; i < questions.size(); i++)
			maxscore += questions.get(i).getPoints();
		boolean perfscore = (Integer.parseInt(request.getParameter("score")) == maxscore) ? true : false;
		//TODO change above line when weights implemented
		if (perfscore) {
			ArrayList<Achievement> previous = am.achievementsForUser(userManager.getCurrentUserID(request));
			for (Achievement a: previous){
				if (a.getType() == Achievement.AchievementType.PERFECT_SCORE)
					perfscore = false;
			}	
		}
		if (perfscore)
			am.createAchievement(userManager.getCurrentUserID(request), quizID, Achievement.AchievementType.PERFECT_SCORE);
		int count = 0;
		if (!takequiz)
			count++;
		if (hs != null)
			count++;
		if (perfscore)
			count++;
		if (count == 1){
			out.println("<h2>Congrats! You've earned an achievement.</h2>");
		} else if (count == 2){
			out.println("<h2>Congrats! You've earned 2 achievements.</h2>");
		} else if (count == 3){
			out.println("<h2>Congrats! You've earned 3 achievements.</h2>");
		}
		if (!takequiz)
			out.println("<p>" + request.getParameter("ach") + "</p>");
		if (hs != null)
			out.println("<p>" + hs.getText() + "</p>");
		if (perfscore)
			out.println("<p>" + "Perfection: You scored 100% on a quiz" + "</p>");%>
	</body>
</html>