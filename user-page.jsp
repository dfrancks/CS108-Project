<%@page import="finalproject.Result"%>
<%@page import="finalproject.ResultManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.Quiz" %>
<%@ page import= "finalproject.QuizManager" %>
<%@ page import= "finalproject.User" %>
<%@ page import= "finalproject.User.PrivacySetting" %>  
<%@ page import= "finalproject.UserManager" %>
<%@ page import= "finalproject.AchievementManager" %>
<%@ page import= "finalproject.Achievement" %>
<%@ page import= "finalproject.UserManager.FriendRequestStatus" %>
<%@ page import= "java.util.ArrayList" %> 
<!DOCTYPE html>

<%
	int MAX_NUM_DISPLAY = 4;

	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	int userID;
	if (request.getParameter("userID") != null) {
		userID = Integer.parseInt(request.getParameter("userID"));
	} else {
		userID = 1;
	}
	User user = um.userForUserID(userID);
	String username = user.getUsername();
	
	// Privacy-related data
	PrivacySetting privacySetting = user.getPrivacy();
	ArrayList<User> friends = um.friendsForUser(userID);
	boolean isFriend = false;
	for (User friend : friends) {
		if (friend.getID() == um.getCurrentUserID(request)) {
			isFriend = true;
			break;
		}
	}
	boolean isFriendOfFriend = false;
	if (!isFriend) {
		for (User friend : friends) {
			for (User friendOfFriend : um.friendsForUser(friend.getID())) {
				if (friend.getID() == um.getCurrentUserID(request)) {
					isFriendOfFriend = true;
					break;
				}
			}
		}
	}
	
	FriendRequestStatus statusCurrUserSent = um.getFriendStatus(userID, um.getCurrentUserID(request));
	FriendRequestStatus statusOtherUserSent = um.getFriendStatus(um.getCurrentUserID(request), userID);
	boolean showFriendRequestButton = false;
	
	boolean isAdmin = um.isAdminForUserID(um.getCurrentUserID(request));
%>

<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=username%></title>
</head>
<body <%if (userID == um.getCurrentUserID(request)) out.println("id=profile");%>>
<h1><%=username%></h1>

<form action="FriendRequestServlet" method="post">
	<input type="hidden" name="otherUserID" id="otherUserID" value="<%=userID%>"/>
	<%
		if (userID != um.getCurrentUserID(request)) {
			if (statusCurrUserSent == FriendRequestStatus.ACCEPTED || statusOtherUserSent == FriendRequestStatus.ACCEPTED) {
				out.println("<div>You and " + username + " are friends!");
				if (statusCurrUserSent == FriendRequestStatus.ACCEPTED) {
					out.println("<input type=hidden name=currUserInitiated id=otherUserID value=1></div>");
				} else {
					out.println("<input type=hidden name=currUserInitiated id=otherUserID value=0></div>");
				}
				out.println("<input type=submit name=button value=Un-friend>");
			} else if (statusOtherUserSent == FriendRequestStatus.PENDING) {
				out.println(username + " sent you a friend request!");
				out.println("<input type=submit name=button value=Accept>");
				out.println("<input type=submit name=button value=Reject style=\"background-color: red;\"></div>");
			} else if (statusCurrUserSent == FriendRequestStatus.PENDING || statusCurrUserSent == FriendRequestStatus.REJECTED) {
				out.println("Your friend request to " + username + " is pending.");
			} else {
				showFriendRequestButton = true;
			}
		}
	%>
</form>
	<%
		if (showFriendRequestButton) {
			%>
			<form action="send-message.jsp">
				<input type="hidden" name="toID" value=<%=userID%>>
				<input type="hidden" name="isRequest" value="1">
				<input type="submit" value="Send Friend Request">
			</form>
			<%
		}
	%>

<%
	if (userID != um.getCurrentUserID(request)) {
		%>
		<div id="messageForm">
			<form action="send-message.jsp">
				<input type="hidden" name="toID" value=<%=userID%>>
				<input type="submit" value="Send Message">
			</form>
		</div>
		<%
	} else {
		%>
		<div id="privacyForm">
			<form action="privacy-settings.jsp">
				<input type="submit" value="Edit Privacy Settings">
			</form>
		</div>
		<%
	}
%>

<%if (isAdmin){%>
<form action = "UserStatusServlet" method = "post">
	<% if(!um.isAdminForUserID(userID)){ %>
	<input type = "submit" name = "Path" value = "Promote to Admin">
	<%}
		if(um.userIsActive(userID) && um.getCurrentUserID(request) != userID){ %>
		<input type = "submit" name = "Path" value = "Suspend Account" style = "background-color: red;">
	<%} else if (um.getCurrentUserID(request) != userID){ %>
		<input type="submit" name = "Path" value = "Unsuspend Account">
	<%}%>	
	<input type ="hidden" name = "userID" value = "<%=userID%>">
</form>
<%}%>

<%
	if (user.getPrivacy() == PrivacySetting.FRIENDS && !isFriend && !isAdmin && userID != um.getCurrentUserID(request)) {
		out.println("<p><i>You must be a friend to view this profile.</i></p>");
	} else if (user.getPrivacy() == PrivacySetting.FRIENDS_OF_FRIENDS && !isFriendOfFriend && !isAdmin && userID != um.getCurrentUserID(request)) {
		out.println("<p><i>You must either be a friend or a friend of a friend to view this profile.</i></p>");
	} else {
%>
	<div class=sidebar>
		<div class=card>
		<h2>Achievements</h2>
		<ul>
			<% 
				AchievementManager am = (AchievementManager)request.getServletContext().getAttribute(AchievementManager.ATTR_NAME);
				ArrayList<Achievement> achievements = am.achievementsForUser(userID);
				int max = Math.min(achievements.size(), MAX_NUM_DISPLAY);	 
				for (int i = 0; i < max; i++){
					%>
					<li><%=achievements.get(i).getText()%></li>
					<%
				}
			%>
		</ul>
		</div>
		<div class=card>
		<h2><%=username%>'s Friends</h2>
		<ul>
			<%
			for (User u: friends) {
				out.println("<li><a href=\"user-page.jsp?userID=" + u.getID() + "\">" + u.getUsername() + "</a></li>");
			}
			%>
		</ul>
		</div>
	</div>
	<div style="float:left; width:60%;">
	<div class=card>
	<h2>Created Quizzes</h2>
		<ul>
			<% 
				QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
				ArrayList<Quiz> cur = qm.getCreatedQuizzes(userID);
				max = Math.min(cur.size(), MAX_NUM_DISPLAY);	 
				for (int i = 0; i < max; i++){
					%>
					<li><a href =quiz-page.jsp?quizid=<%=cur.get(i).getQuizID()%>><%=cur.get(i).getTitle()%>: <i>"<%=cur.get(i).getDescription()%>"</i></a></li>
					<%
				}
			%>
		</ul>
	</div>
	<div class=card>
	<h2>Recently Taken Quizzes <a href = "user-history.jsp?userID=<%=userID%>">(View Full History)</a></h2>
	<ul>
		<% 
			cur = qm.getRecentlyTakenQuizzes(userID);
			max = Math.min(cur.size(), MAX_NUM_DISPLAY);	 
			for (int i = 0; i < max; i++){
				%>
				<li><a href =quiz-page.jsp?quizid=<%=cur.get(i).getQuizID()%>><%=cur.get(i).getTitle()%>: <i>"<%=cur.get(i).getDescription()%>"</i></a></li>
				<%
			}
		%>
	</ul>
	</div>

	<div class=card>
	<h2>Highest Scores</h2>
	<ul>
		<%
			ResultManager rm = (ResultManager)request.getServletContext().getAttribute(ResultManager.ATTR_NAME);
			ArrayList<Result> results = rm.highScoresForUser(userID);
			max = Math.min(results.size(), MAX_NUM_DISPLAY);
			for (int i = 0; i < max; i++) {
				out.println("<li><a href=\"quiz-page.jsp?quizid=" + results.get(i).getQuizID() + "\">" + qm.quizForQuizID(results.get(i).getQuizID()).getTitle() + "</a>: " + results.get(i).getScore() + " points</li>");
			}
		%>
	</ul>
	</div>
	</div>
<%
	}
%>
</body>
</html>