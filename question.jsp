<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="finalproject.Answer"%>
<%@page import="finalproject.Question"%>
<%@page import="finalproject.Quiz"%>
<%@page import="finalproject.QuizManager"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="master.css">
<%
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	int quizID = Integer.parseInt(request.getParameter("quizID"));
	Quiz currentQuiz = qm.getCurrentlyTakenQuiz(quizID);
	if(currentQuiz == null) return;
	int i = Integer.parseInt(request.getParameter("questionIndex"));
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
 	<%=i+1%>.
	<%
		ArrayList<Question> questions = currentQuiz.getQuestions();
		Question currentQuestion = questions.get(i);
		switch (currentQuestion.getType()){
			case QUESTION_RESPONSE: %>
				<%=currentQuestion.getQuestionText()%>
					<p><input type = "text" name = "question<%=i%>"></p>
			<% break;
			case FILL_IN_THE_BLANK:%>
					<%= currentQuestion.getQuestionText() %> _________ <%= currentQuestion.getSecondaryQuestionText() %>
					<p><input type = "text" name = "question<%=i%>"></p>
			<% break;
			case MULTIPLE_CHOICE:
				ArrayList<Answer> answers = currentQuestion.getAnswers();%>
				<%=currentQuestion.getQuestionText()%>
				<%for (int j = 0; j < answers.size(); j++){
					Answer a = answers.get(j); %>
					<p><input type = "radio" name = "question<%=i%>" value = "<%= a.getAnswerText() %>"><%= a.getAnswerText() %></p>
				<%}%>
			<%break;
			case PICTURE_RESPONSE:%>
				<p><img src="<%=currentQuestion.getQuestionText()%>" style="width:304px;height:228px;"></p>
				<p><input type = "text" name = "question<%=i%>"></p>
			<%break;
			default: break;
		} %>
</body>
</html>