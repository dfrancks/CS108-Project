<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.Quiz" %>
<%@ page import= "finalproject.QuizManager" %>
<%@ page import= "finalproject.User" %> 
<%@ page import= "finalproject.UserManager" %>
<%@ page import= "finalproject.UserManager.FriendRequestStatus" %>
<%@ page import= "java.util.ArrayList" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	int toUserID = Integer.parseInt(request.getParameter("toID"));
	String username = um.usernameForUserID(toUserID);
	int isFriendRequest = 0;
	if (request.getParameter("isRequest") != null) {
		isFriendRequest = 1;
	}
	int statusCurrUserSent = um.getFriendStatus(toUserID, um.getCurrentUserID(request)).ordinal();
	int statusOtherUserSent = um.getFriendStatus(um.getCurrentUserID(request), toUserID).ordinal();
	int statusPending = FriendRequestStatus.PENDING.ordinal();
	int statusRejected = FriendRequestStatus.REJECTED.ordinal();
	int statusAccepted = FriendRequestStatus.ACCEPTED.ordinal();
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	ArrayList<Quiz> quizzes = qm.getRecentlyTakenQuizzes(um.getCurrentUserID(request));
%>

<html>
<%@include file="header.jsp" %>
<script type="text/javascript" src="send-message.js"></script>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Send Message</title>
</head>
<body>
<h1>Send Message</h1>

<%
	if (toUserID == -1) {
		out.println("<a href=\"all-users.jsp?select=1\">Select Recipient</a>");
	} else {
		out.println("<p>To: " + username + "</p>");
	}
%>

<script type="text/javascript">
	function detectChange() {
		document.getElementById("submit").disabled = false;
		var requestStatus = document.getElementById("requestStatus")
		requestStatus.innerHTML = "";
		requestStatus.style = "display:none;";
		var select = document.getElementById("typeSelect");
		var selected = select.options[select.selectedIndex].value;
		var quizzes = document.getElementById("quizzes");
		var textArea = document.getElementById("textArea");
		if (selected === "challenge") {
			quizzes.style = "display:inline-block;";
			textArea.innerHTML = "Try to beat my score on this quiz!";
			var quizzesTaken = <%=quizzes.size()%>;
			if (quizzesTaken === 0) document.getElementById("submit").disabled = true;
		} else if (selected === "request") {
			if (<%=statusCurrUserSent%> === <%=statusPending%> || <%=statusCurrUserSent%> === <%=statusRejected%>) {
				requestStatus.innerHTML = "<p>You have already sent this user a friend request!<p>";
				requestStatus.style = "display:inline-block;";
				document.getElementById("submit").disabled = true;
			} else if (<%=statusOtherUserSent%> === <%=statusPending%>) {
				console.log("HERE");
				var innerHTML = "<form action=FriendRequestServlet method=post>";
				innerHTML += "<input type=hidden name=otherUserID id=otherUserID value=" + <%=toUserID%> + ">";
				innerHTML += "<p>This user already sent you a friend request!<p>";
				innerHTML += "<input type=submit name=button value=Accept>";
				innerHTML += "<input type=submit name=button value=Reject>";
				innerHTML += "</form>";
				requestStatus.innerHTML = innerHTML;
				requestStatus.style = "display:inline-block;";
				document.getElementById("submit").disabled = true;
			} else if (<%=statusCurrUserSent%> === <%=statusAccepted%> || <%=statusOtherUserSent%> === <%=statusAccepted%>) {
				requestStatus.innerHTML = "<p>You and this user are already friends!<p>";
				requestStatus.style = "display:inline-block;";
				document.getElementById("submit").disabled = true;
			}
			quizzes.style = "display:none;";
			textArea.innerHTML = "Please add me as a friend!";
		} else {
			quizzes.style = "display:none;";
			textArea.innerHTML = "";
		}
	}
</script>

<div id=requestStatus style="display:none;">
</div>
	
<form action="MessageServlet" method="post">
	<div id="type">
		<p>Message type:
		<select id="typeSelect" name="typeSelect" onChange="detectChange()">
			<%
				if (isFriendRequest == 0) {
					out.println("<option value=\"request\">Friend Request</option>");
					out.println("<option selected=\"selected\" value=\"note\">Note</option>");
				} else {
					out.println("<option selected=\"selected\" value=\"request\">Friend Request</option>");
					out.println("<option value=\"note\">Note</option>");
				}
			%>
		<option value="challenge">Challenge</option>
		</select>
		</p>
	</div>

	<div id=quizzes style="display:none;"> 
		<%
			if (quizzes.isEmpty()) {
				out.println("<p>You need to take a quiz before you can challenge another user!</p>");
			} else {
				out.println("<p>Select a quiz for your challenge:");
				out.println("<select id=quizSelect name=quizSelect>");
				for (Quiz q: quizzes) {
					out.println("<option value=" + q.getQuizID() + ">" + q.getTitle() + "</option>");
				}
				out.println("</select>");
				out.println("</p>");
	 		}
		%>
	</div>

	<div id=messageText>
		<p>Text:</p>
		<textarea id=textArea name=textArea rows="4" cols="50" onKeyUp="textChanged(<%=toUserID%>)"><%if(isFriendRequest == 1)out.println("Please add me as a friend!");%></textarea>
	</div>
	<input type="hidden" name="toUserID" id="toUserID" value="<%=toUserID%>"/>
	<input type=submit id=submit style="margin-top:10px;" value=Submit>
	<script>
		checkDisable(<%=quizzes.size()%>, <%=toUserID%>);
	</script>
</form>

</body>
</html>