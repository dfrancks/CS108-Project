<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.Message" %>
<%@ page import= "finalproject.Message.MessageType" %>
<%@ page import= "finalproject.MessageManager" %>   
<%@ page import= "finalproject.UserManager" %>
<%@ page import= "java.util.ArrayList" %> 
<!DOCTYPE html>

<%
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	MessageManager mm = (MessageManager)request.getServletContext().getAttribute(MessageManager.ATTR_NAME);
	ArrayList<Message> receivedMessages = mm.receivedMessagesForUser(um.getCurrentUserID(request));
	ArrayList<Message> sentMessages = mm.sentMessagesForUser(um.getCurrentUserID(request));
%>

<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Your Mailbox</title>
</head>
<body id="mailbox">
	<form action="send-message.jsp" method="post">
		<input type="hidden" name="toID" value="-1">
		<input type ="submit" style="margin-top:20px;" value ="Create New Message"/>
	</form>
	<div style="float:left; width:50%;">
		<div class="messagelist">
			<h2>Received Messages</h2>
			<ul>
				<%
					if (receivedMessages.size() == 0) {
						out.println("<p><i>No received messages.</i></p>");
					}
					for (Message m : receivedMessages) {
						%>
						<li style="margin-left:-50px;">
						<a href="view-message.jsp?messageID=<%=m.getMessageID()%>">
							<div class="card">
								<p><%=m.getTypeString()%> from <%=um.usernameForUserID(m.getSenderID())%>:</p>
								<p class="messagetext">"<%=m.getMessageText().trim()%>"</p>
							</div>
						</a>
						</li>
						<%
					}
				%>
			</ul>
		</div>
	</div>
	<div style="float:left; width:50%;">
		<div class="messagelist">
			<h2>Sent Messages</h2>
			<ul>
				<%
					if (sentMessages.size() == 0) {
						out.println("<p><i>No sent messages.</i></p>");
					}
					for (Message m : sentMessages) {
						%>
						<li style="margin-left:-50px;">
						<a href="view-message.jsp?messageID=<%=m.getMessageID()%>">
							<div class="card">
								<p><%=m.getTypeString()%> to <%=um.usernameForUserID(m.getRecipientID())%>:</p>
								<p class="messagetext">"<%=m.getMessageText().trim()%>"</p>
							</div>
						</a>
						</li>
						<%
					}
				%>
			</ul>
		</div>
	</div>
</body>
</html>