<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import= "finalproject.AnnouncementManager" %>
    <%@ page import= "finalproject.Announcement" %>
<%@ page import= "finalproject.AnalyticsManager" %>
<%@ page import= "finalproject.UserManager" %>
<%@ page import= "java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage Announcements</title>
</head>
<body>
<h1>Manage Announcements</h1>
<%AnnouncementManager am = (AnnouncementManager)request.getServletContext().getAttribute(AnnouncementManager.ATTR_NAME);
ArrayList<Announcement> announcements = am.getAnnouncements();%>

<form action = "AnnouncementServlet" method = "post">

<p>Add New Announcement:</p>
<input type = "text" name = "announcement" style = "width: 400px">
<input type = "submit" name = "Path" value = "Post">

<p>Check to toggle visibility of existing announcements:</p>

<%for(int i = 0; i < announcements.size(); i++){ 
	Announcement cur = announcements.get(i);
	String output = ("<p><input type = \"checkbox\" name = \"" + cur.getID() +"\"");
	if (cur.isActive())
		output += " checked";
	output += ">" + cur.getText() + "</p>";
	out.println(output);
}%>
<p><input type = "submit" name = "Path" value = "Save Changes"></p>
</form>

</body>
</html>