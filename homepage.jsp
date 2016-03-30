<%@page import="finalproject.Announcement"%>
<%@page import="finalproject.AnnouncementManager"%>
<%@ page import="finalproject.AccountManager" %>
<%@page import="finalproject.Achievement"%>
<%@page import="finalproject.AchievementManager"%>
<%@ page import="finalproject.Message"%>
<%@ page import="finalproject.MessageManager"%>
<%@ page import="finalproject.UserManager" %>
<%@ page import="finalproject.User" %>
<%@ page import="finalproject.QuizManager" %>
<%@ page import="finalproject.Quiz" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
	UserManager um = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
	QuizManager qm = (QuizManager)request.getServletContext().getAttribute(QuizManager.ATTR_NAME);
	MessageManager mm = (MessageManager)request.getServletContext().getAttribute(MessageManager.ATTR_NAME);
	AchievementManager am = (AchievementManager)request.getServletContext().getAttribute(AchievementManager.ATTR_NAME);
	AnnouncementManager anm = (AnnouncementManager)request.getServletContext().getAttribute(AnnouncementManager.ATTR_NAME);
	int userID = um.getCurrentUserID(request);
	String username = um.getCurrentUsername(request);
	final int MAX_NUM_DISPLAY = 4;
%>
<html>
	<link rel="stylesheet" href="master.css">
	<%@include file="header.jsp" %>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Welcome <%=username%></title>
	</head>
	<body id="home">
		<% 
			ArrayList<Announcement> announcements = anm.getActiveAnnouncements();
			int max = Math.min(announcements.size(), MAX_NUM_DISPLAY);
			for (int i = 0; i < max; i++){
				out.println("<div class=announcement>" + announcements.get(i).getText() + "</div>");
			}
		%>
		<h1>Welcome to QuizTimes, <%=username%>!</h1>
		<form action="LogoutServlet" method = "post">
			<input type = "submit" value = "Logout"/>
		</form>
		<form action="create-quiz.jsp" method = "post">
			<input type = "submit" value = "Create Quiz"/>
		</form>
		
		<div style="float:left; width:50%;">
		<div class="card">
		<h2>Popular Quizzes</h2>
		<ul>
		<% ArrayList<finalproject.Quiz> cur = qm.getPopularQuizzes();
		max = Math.min(cur.size(), MAX_NUM_DISPLAY);	 
			for (int i = 0; i < max; i++){%>
				<li>
					<a href =quiz-page.jsp?quizid=<%=cur.get(i).getQuizID()%>><%=cur.get(i).getTitle()%>: <i>"<%=cur.get(i).getDescription()%>"</i></a>
				</li>
			<%}%>
		</ul>
		</div>
		
		<div class="card">
		<h2>Recently Created Quizzes</h2>
		<ul>
		<% cur = qm.getRecentQuizzes();
		max = Math.min(cur.size(), MAX_NUM_DISPLAY);	 
			for (int i = 0; i < max; i++){%>
				<li>
					<a href = quiz-page.jsp?quizid=<%=cur.get(i).getQuizID()%>><%=cur.get(i).getTitle()%>: <i>"<%=cur.get(i).getDescription()%>"</i></a>
				</li>
			<%}%>
		</ul>	
		</div>
		
		<div class="card">
		<h2>Your Recently Taken Quizzes</h2>
		<ul>
		<% cur = qm.getRecentlyTakenQuizzes(userID);
		max = Math.min(cur.size(), MAX_NUM_DISPLAY);	 
			for (int i = 0; i < max; i++){%>
				<li>
					<a href =quiz-page.jsp?quizid=<%=cur.get(i).getQuizID()%>><%=cur.get(i).getTitle()%>: <i>"<%=cur.get(i).getDescription()%>"</i></a>
				</li>
			<%}%>
		</ul>
		</div>
		
		<div class="card">	
		<h2>Your Recently Created Quizzes</h2>
		<ul>
		<% cur = qm.getCreatedQuizzes(userID);
		max = Math.min(cur.size(), MAX_NUM_DISPLAY);	 
		for (int i = 0; i < max; i++){%>
			<li>
				<a href =quiz-page.jsp?quizid=<%=cur.get(i).getQuizID()%>><%=cur.get(i).getTitle()%>: <i>"<%=cur.get(i).getDescription()%>"</i></a>
			</li>
		<%}%>
		</ul>
		</div>
		</div>
		
		<div style="float:left; width:50%;">
		<div class="card">	
		<h2>Your Achievements</h2>
		<ul>
		<% ArrayList<Achievement> achlist = am.achievementsForUser(userID);
		max = Math.min(achlist.size(), MAX_NUM_DISPLAY);	 
			for (int i = 0; i < max; i++){%>
				<li><%=achlist.get(i).getText()%></li>
			<%}%>
		</ul>	
		</div>
		
		<div class="card">
		<h2>Your Recent Messages</h2>
		<ul>
			<% 
				ArrayList<Message> messages = mm.receivedMessagesForUser(userID);
				max = Math.min(messages.size(), MAX_NUM_DISPLAY);	 
				for (int i = 0; i < max; i++){
					Message m = messages.get(i);
					out.println("<li><a href=\"view-message.jsp?messageID=" + m.getMessageID() + 
							"\">" + m.getTypeString() + " from " + um.usernameForUserID(m.getSenderID()) + 
							": \"" + m.getMessageText().trim() + "\"</a></li>");
				}
			%>
		</ul>
		</div>
		
		<div class="card">
		<h2>Your Friends</h2>
		<ul>
		<% ArrayList<User> friends = um.friendsForUser(userID);
		max = Math.min(friends.size(), MAX_NUM_DISPLAY);
		for (int i = 0; i < max; i++) {
			out.println("<li><a href = user-page.jsp?quizID=" +friends.get(i).getID() + ">" + friends.get(i).getUsername()+ "</a></li>");
		}%>
		</ul>
		</div>
		
		<div class="card">
		<h2>Your Friends' Activity</h2>
		<ul>
		<% class listItem {
			public String type;
			public User friend;
			public Object thing;
			
			public listItem(String type, User friend, Object thing){
				this.type = type;
				this.friend = friend;
				this.thing = thing;
			}
		}
		ArrayList<listItem> list = new ArrayList<listItem>();
		 for (int i = 0; i < max; i++) {
			ArrayList<Quiz> quizzes = qm.getRecentlyTakenQuizzes(friends.get(i).getID());
			if (quizzes.size() > 0)
				list.add(new listItem("Took Quiz", friends.get(i), quizzes.get(0)));
			quizzes = qm.getCreatedQuizzes(friends.get(i).getID());
			if (quizzes.size() > 0)
				list.add(new listItem("Created Quiz", friends.get(i), quizzes.get(0)));
			ArrayList<Achievement> achs = am.achievementsForUser(friends.get(i).getID());
			if (achs.size() > 0)
				list.add(new listItem("Achievement", friends.get(i), achs.get(0)));
		}
		Collections.shuffle(list);
		for (listItem item: list){
			if (item.type.equals("Took Quiz")){
				out.println("<li><a href = user-page.jsp?quizID=" + item.friend.getID() + ">" + item.friend.getUsername()+ "</a> took " +
						"<a href = quiz-page.jsp?quizid="+ ((Quiz)item.thing).getQuizID() + ">" + ((Quiz)item.thing).getTitle() + "</a></li>");
			} else if (item.type.equals("Created Quiz")){
				out.println("<li><a href = user-page.jsp?userID=" + item.friend.getID() + ">" + item.friend.getUsername()+ "</a> created " +
						"<a href = quiz-page.jsp?quizid="+ ((Quiz)item.thing).getQuizID() + ">" + ((Quiz)item.thing).getTitle() + "</a></li>");
			} else {
				String text = "";
				switch(((Achievement)item.thing).getType()){
					case ONE_QUIZ: text = " earned Amateur Author by creating a quiz."; break;
					case FIVE_QUIZ: text = " earned Prolific Author by creating 5 quizzes."; break;
					case TEN_QUIZ: text = " earned Prodigious Author by creating 10 quizzes."; break;
					case TAKE_TEN_QUIZ: text = " earned Quiz Machine by creating a quiz."; break;
					case HIGH_SCORE: text = " became Top Banana by obtaining the high score on a quiz."; break;
					case PERFECT_SCORE: text =  " achieved Perfection! He/she scored 100% on a quiz."; break;
					default: ;
			}	
				out.println("<li><a href = user-page.jsp?quizID=" + item.friend.getID() + ">" + item.friend.getUsername() + "</a>" + text + "</li>");
			}
		}%>
		</ul>
		</div>
		</div>
	</body>
</html>