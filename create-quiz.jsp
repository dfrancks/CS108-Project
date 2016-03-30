<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import= "finalproject.Question" %> 
<%@ page import= "finalproject.Question.QuestionType" %> 
<%@ page import= "finalproject.Quiz" %>
<%@ page import= "finalproject.QuizManager" %>     
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<%@include file="header.jsp" %>
	<style>
		div {
			display:inline;
		}
	</style>
<head>
	<link rel="stylesheet" href="master.css">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Create Quiz!</title>
</head>
<body>
	<h1>Quiz Creation</h1>
	<%! 
		public String setField(String name, HttpSession session){
			String nameField = (String) session.getAttribute(name);
			if(nameField == null) nameField = "";
			return nameField;
		}
		
		public String getVal(String text, String val){
			return text.equals(val) ? "checked" : "";	
		}
		
		public String getNotVal(String text, String val){
			return !text.equals(val) ? "checked" : "";	
		}
	%>
	<%
		String titleField = setField("title", session);
		String descriptionField = setField("description", session);
		String randomizedValue = setField("randomized", session);
		String numPages = setField("numpages", session);
		String correction = setField("correction", session);
		
		QuizManager quizManager = (QuizManager) application.getAttribute(QuizManager.ATTR_NAME);
		Integer quizId = (Integer) session.getAttribute("currentQuizId");
		Quiz quiz = null;
		ArrayList<Question> questionList;
		if(quizId != null) {
			quiz = quizManager.inProgressQuizForQuizID(quizId);
			if(quiz != null) {
				questionList = quiz.getQuestions();
			} else{
				questionList = new ArrayList<Question>();
			}
		} else{
			questionList = new ArrayList<Question>();
		}

	%>
	<script type="text/javascript" src="create-quiz.js"></script>
	<h2>Quiz Options </h2>
	<form id = "quiz" action="CreateQuestionServlet" method = "post">
		<input type="hidden" name = "type" value = "question" />
		<p><div>Title <input type="text" name="title" id="title" value= "<%= titleField %>" onKeyUp=titleTextChanged()></div>
		<p></p>
		<p><div>Description <input type="text" name="description" value="<%= descriptionField %>"></div>
		<p></p>
  		<div><input type="radio" name="randomized" value="yes" <%= getVal(randomizedValue, "yes") %>>Randomized</div>
  		<div><input type="radio" name="randomized" value="no" <%= getNotVal(randomizedValue, "yes")%>>Not Randomized</div>
  		<p></p>
  
  		<div><input type= "radio" name = "numpages" value="single" <%= getVal(numPages, "single") %>>One Page</div>
  		<div><input type= "radio" name = "numpages" value="multiple" <%= getNotVal(numPages, "single") %>>Multiple Pages</div>
  		<p></p>
  		
  		<div><input type= "radio" name = "correction" value="immediate" <%= getVal(correction, "immediate") %>>Immediate</div>
  		<div><input type= "radio" name = "correction" value="notimmediate" <%= getNotVal(correction, "immediate") %>>Not Immediate</div>
 
  		<h2>Current Questions</h2>
  		<ol type = "1">
  			<%  for (Question q: questionList) { 
  					if(q.getType() == QuestionType.FILL_IN_THE_BLANK){ %>
  						<li><%= q.getQuestionText() %> _________ <%= q.getSecondaryQuestionText() %></li>
  					<% } else if (q.getType().equals(QuestionType.PICTURE_RESPONSE)) {%>
						<li><p><img src="<%=q.getQuestionText()%>" style="width:304px;height:228px;"></p></li>
					<%} else { %>
  						<li><%= q.getQuestionText() %></li>
  					<% } 
			 } %>
  		</ol>
  		<div id= "points" style="display:none;">
  			<p>Points<input type="number" name="points" id="points" min="1" value="1" style="width:45px; margin-left:10px"></p>
  		</div>
  		<div id= "questiontype"></div>
  		<div id = "divbutton"><p><button id = "button" type = "button" onclick="loadQuestionTypes()">Add Question</button></p></div>
  		<p><input type="submit" type="submit" id="submitquiz" value="Submit Quiz" disabled="disabled" onclick="form.action='CreateQuizServlet';"/></p>
  		<script>
  			checkDisable();
  		</script>
	</form>
</body>
</html>