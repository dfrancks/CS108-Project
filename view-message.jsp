<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.Message" %>
<%@ page import= "finalproject.Message.MessageType" %>
<%@ page import= "finalproject.MessageManager" %>
<%@ page import= "finalproject.Quiz" %> 
<%@ page import= "finalproject.QuizManager" %>
<%@ page import= "finalproject.Result" %>  
<%@ page import= "finalproject.ResultManager" %>  
<%@ page import= "finalproject.UserManager" %>
<%@ page import= "finalproject.UserManager.FriendRequestStatus" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	// Init managers
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	MessageManager mm = (MessageManager)request.getServletContext().getAttribute(MessageManager.ATTR_NAME);
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
	
	// Get relevant message information
	int messageID = Integer.parseInt(request.getParameter("messageID"));
	Message m = mm.messageForMessageID(messageID);
	boolean userSent;
	if (um.getCurrentUserID(request) == m.getSenderID()) {
		userSent = true;
	} else {
		userSent = false;
	}
	int requestStatus = um.getFriendStatus(m.getRecipientID(), m.getSenderID()).ordinal();
	
	// If challenge, get quiz and relevant result (if exists)
	int quizID = -1;
	Quiz quiz = null;
	Result recipientResult = null;
	Result senderResult = null;
	if (m.getType().ordinal() == MessageType.CHALLENGE.ordinal()) {
		quizID = m.getQuizID();
		quiz = qm.quizForQuizID(quizID);
		recipientResult = rm.bestUserResultOnQuiz(m.getRecipientID(), quizID);
		senderResult = rm.bestUserResultOnQuiz(m.getSenderID(), quizID);
	}
%>

<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Message</title>
</head>
<h1><%=m.getTypeString()%></h1>
<body>
	<div id=user>
		<%
			if (userSent) {
				out.println("<p>To: <a href=\"user-page.jsp?userID=" + m.getRecipientID() + 
							"\">" + um.usernameForUserID(m.getRecipientID()) + "</a></p>");
			} else {
				out.println("<p>From: <a href=\"user-page.jsp?userID=" + m.getSenderID() + 
							"\">" + um.usernameForUserID(m.getSenderID()) + "</a></p>");
			}
		%>
	</div>
	<div id=text>
		<p>Message: "<%=m.getMessageText()%>"</p>
	</div>
	<div id=reply style="display:none;">
		<form action="send-message.jsp">
			<input type="hidden" name="toID" value=<%=m.getSenderID()%>>
			<input type="submit" value="Reply">
		</form>
	</div>
	<script>
		if (!<%=userSent%>) document.getElementById("reply").style = "display:inline-block;";
	</script>
	<div id=request style="display:none;"></div>
	<div id=quiz>
		<% 
			if (m.getType().ordinal() == MessageType.CHALLENGE.ordinal()) {
				if (userSent) {
					if (recipientResult == null) {
						out.println("<p>Your challenge has not yet been accepted.</p>");
					} else {
						out.println("<p>" + um.usernameForUserID(m.getRecipientID()) + 
									" accepted your challenge and achieved a score of " + recipientResult.getScore() + "</p>");
					}
				} else {
					if (recipientResult == null) {
						%>
						<form action="quiz-page.jsp">
							<p><%=um.usernameForUserID(m.getSenderID())%> challenged you to beat a score of <%=senderResult.getScore()%> on the following quiz:</p>
							<p>Title: <%=quiz.getTitle()%> </p>
							<p>Description: <%=quiz.getDescription()%> </p>
							<input type="hidden" name="quizid" value=<%=quizID%>>
							<input type="submit" value="Take Quiz">
						</form>
						<%
					} else {
						%>
						<form action="quiz-page.jsp">
							<p>You already accepted this challenge and achieved a score of <%=recipientResult.getScore()%> on the following quiz:</p>
							<p>Title: <%=quiz.getTitle()%> </p>
							<p>Description: <%=quiz.getDescription()%> </p>
							<input type="hidden" name="quizid" value=<%=quizID%>>
							<input type="submit" value="Re-take Quiz">
						</form>
						<%
					}
				}
			}
		%>
	</div>
	<script>
		var innerHTML;
		if (<%=m.getType().ordinal()%> === <%=MessageType.REQUEST.ordinal()%>) {
			if (<%=requestStatus%> === <%=FriendRequestStatus.PENDING.ordinal()%>) {
				if (!<%=userSent%>) {
					innerHTML = "<form action=FriendRequestServlet method=post>";
					innerHTML += "<input type=hidden name=otherUserID id=otherUserID value=" + <%=m.getSenderID()%> + ">";
					innerHTML += "<input type=submit name=button value=Accept>";
					innerHTML += "<input type=submit name=button value=Reject>";
					innerHTML += "</form>";
				} else {
					console.log("HERE");
					innerHTML = "<p>Status: Pending</p>"
				}
			} else if (<%=requestStatus%> === <%=FriendRequestStatus.ACCEPTED.ordinal()%>) {
				innerHTML = "<p>Status: Accepted</p>";
			} else if (<%=requestStatus%> === <%=FriendRequestStatus.REJECTED.ordinal()%>) {
				innerHTML = "<p>Status: Rejected</p>";
			} else {
				innerHTML = "";
			}
			var request = document.getElementById("request");
			request.style = "display:inline-block;";
			request.innerHTML = innerHTML;
		}
	</script>
</body>
</html>