<%@ page import="finalproject.UserManager" %>
<!DOCTYPE html>
<html>
	<head>
	<link rel="stylesheet" href="master.css">
	<meta charset="UTF-8" />
		<title>Create Account</title>
	</head>
	<body>
		<% 
			String username = "";
			Cookie[] cookies = request.getCookies();
			if(cookies != null){
				for (int i = 0; i < cookies.length; i++){
					if(cookies[i].getName().equals("currentUserID")) {
						UserManager manager = (UserManager)request.getServletContext().getAttribute(UserManager.ATTR_NAME);
						username = manager.usernameForUserID(Integer.parseInt(cookies[i].getValue()));
						response.sendRedirect("homepage.jsp");
						break;
					}
				}
			}
		%>
		<div class="centerFlex">
			<div class="card login">
				<h1>The Name <%=request.getParameter("username")%> is Already In Use</h1>
				<p>Please enter a different username.</p>
				<form action="CreateAccountServlet" method = "post">
					<p>Username: <input type = "text" name = "username" />
					<p>Password: <input type = "password" name = "password" />
					<input type = "submit" style="width:50%; margin-top:30px; padding:5px;" value = "Sign Up" />
				</form>
				<p><a href="index.html">Back to Sign In</a></p>
			</div>
		</div>
	</body>
</html>