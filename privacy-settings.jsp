<%@page import="finalproject.Result"%>
<%@page import="finalproject.ResultManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.User" %>
<%@ page import= "finalproject.User.PrivacySetting" %>  
<%@ page import= "finalproject.UserManager" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	User currUser = um.userForUserID(um.getCurrentUserID(request));
%>

<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Privacy Settings</title>
</head>
<body>
<h1>Edit Privacy Settings</h1>
<h2>Who can view your profile?</h2>
<form action = "PrivacyServlet" method = "post">
	<%
		if (currUser.getPrivacy() == PrivacySetting.ANYONE) {
			%>
			<div><input type="radio" name="privacy" value="everyone" checked="checked">Everyone</div>
			<div><input type="radio" name="privacy" value="friends">Friends</div>
			<div><input type="radio" name="privacy" value="friendsOfFriends">Friends and friends of friends</div>
			<%
		} else if (currUser.getPrivacy() == PrivacySetting.FRIENDS) {
			%>
			<div><input type="radio" name="privacy" value="everyone">Everyone</div>
			<div><input type="radio" name="privacy" value="friends" checked="checked">Friends</div>
			<div><input type="radio" name="privacy" value="friendsOfFriends">Friends and friends of friends</div>
			<%
		} else if (currUser.getPrivacy() == PrivacySetting.FRIENDS_OF_FRIENDS) {
			%>
			<div><input type="radio" name="privacy" value="everyone">Everyone</div>
			<div><input type="radio" name="privacy" value="friends">Friends</div>
			<div><input type="radio" name="privacy" value="friendsOfFriends" checked="checked">Friends and friends of friends</div>
			<%
		}
	%>
	<input type="submit" name="submit" value="Save Settings" style="margin-top:10px;">
</form>
</body>
</html>