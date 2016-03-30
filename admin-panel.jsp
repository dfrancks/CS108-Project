<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "finalproject.AnalyticsManager" %>
<%@ page import= "finalproject.LineGraphs" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
	final int N_DAYS = 7; 
	AnalyticsManager am = (AnalyticsManager)request.getServletContext().getAttribute(AnalyticsManager.ATTR_NAME);
	int newUsersTotal = am.getTotalNumUsers();
	int newQuizzesTotal = am.getTotalNumQuizzes();
	int tookQuizzesTotal = am.getTotalNumUsersTakenQuiz();
%>
<html>
<%@include file="header.jsp" %>
<head>
<link rel="stylesheet" href="master.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin Panel</title>
</head>
<body id="admin">
<h1>Admin Panel</h1>

<form action="manage-announcements.jsp" method="post">
  <input type ="submit" style="margin-bottom:10px;" value ="Manage Announcements"/>
</form>
<form action="manage-reports.jsp" method="post">
  <input type ="submit" style="margin-bottom:20px;" value ="View Reported Quizzes"/>
</form>

<h2>Site Stats</h2>
<div style="width:60%; float:left">
<% LineGraphs lg = new LineGraphs();%>
<%=lg.getHTMLHead()%>

<%=lg.getCreatedBody(N_DAYS) %>

	var options = {
          hAxis: { title: 'Days Ago'},
          vAxis: { title: 'Quizzes'},
          legend: { position: 'bottom' }
        };

        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

        chart.draw(data, options);
      }
      
   <%=lg.getNewUsersBody(N_DAYS) %>

var optionstwo = {
          hAxis: { title: 'Days Ago'},
          vAxis: { title: 'Users'},
          legend: { position: 'bottom' }
        };

        var charttwo = new google.visualization.LineChart(document.getElementById('curve_chart-two'));

        charttwo.draw(datatwo, optionstwo);
      }

 <%=lg.getUsersTakingQuizBody(N_DAYS) %>

var optionsthree = {
          hAxis: { title: 'Days Ago'},
          vAxis: { title: 'Users'},
          legend: { position: 'bottom' }
        };

        var chartthree = new google.visualization.LineChart(document.getElementById('curve_chart-three'));

        chartthree.draw(datathree, optionsthree);
      }

<%=lg.getFooter() %>
</div>
<div style="width 40%; float:left">
	<div class="card" style="width:100%;">
		<h3>All-time Stats</h3>
		<ul>
  			<li><%=newUsersTotal%> user<%if(newUsersTotal != 1) out.println("s");%> signed up</li>
  			<li><%=newQuizzesTotal%> quiz<%if(newQuizzesTotal != 1) out.println("zes");%> created</li>
  			<li><%=tookQuizzesTotal%> user<%if(tookQuizzesTotal != 1) out.println("s have"); else out.println("has");%> taken a quiz</li>
  		</ul>
	</div>
</div>
