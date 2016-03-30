package finalproject;

public class LineGraphs{
	
	private AnalyticsManager am = new AnalyticsManager();
	
	public LineGraphs(){
		
	}

	public String getHTMLHead(){
		String result =  "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n"
		+ "\n<script type=\"text/javascript\">\n"
		+ "\ngoogle.charts.load(\'current\', {\'packages\':[\'corechart\']});\n"
				+ "\ngoogle.charts.setOnLoadCallback(drawChart);\n"
		+ "\ngoogle.charts.setOnLoadCallback(drawCharttwo);\n"
		+ "\ngoogle.charts.setOnLoadCallback(drawChartthree);\n";
		return result;
	}	
	
	public String getCreatedBody(int ndays){
		String result = "function drawChart() {\n"
	        + "var data = google.visualization.arrayToDataTable([\n";
		result += "[ 'Day', 'Quizzes Created'],\n";
		for (int i = 1; i < ndays; i++){
			result += "[ '"+ i + "', " + am.getNumCreatedQuizzesWithDays(i) + "]";
			if (i != ndays - 1)
				result += ",";
			result += "\n";
		}
		result += "]);";
		return result;
	}
	
	public String getUsersTakingQuizBody(int ndays){
		String result = "function drawChartthree() {\n"
	        + "var datathree = google.visualization.arrayToDataTable([\n";
		result += "[ 'Day', 'Users'],\n";
		for (int i = 1; i < ndays; i++){
			result += "[ '"+ i + "', " + am.getNumUsersTakenQuizWithDays(i) + "]";
			if (i != ndays-1)
				result += ",";
			result += "\n";
		}
		result += "]);";
		return result;
	}
	
	public String getNewUsersBody(int ndays){
		String result = "function drawCharttwo() {\n"
	        + "var datatwo = google.visualization.arrayToDataTable([\n";
		result += "[ 'Day', 'New Users'],\n";
		for (int i = 1; i < ndays; i++){
			result += "[ '"+ i + "', " + am.getNumNewUsersWithDays(i) + "]";
			if (i != ndays-1)
				result += ",";
			result += "\n";
		}
		result += "]);";
		return result;
	}
	
	public String getFooter() {
		String result = "</script>\n"
		  + "</head>\n"
		  + "<body>\n"
		  + "<div class=\"graphcard\">\n"
		  + "<h3 style=\"text-align:left;\">Quiz Creation</h3>\n"
		  + "<div id=\"curve_chart\" style=\"width: 100%; height: 350px; margin:auto; text-align:left;\"></div>\n"
		  + "</div>\n"
		  + "<div class=\"graphcard\">\n"
		  + "<h3 style=\"text-align:left;\">New Users</h3>\n"
		  + "<div id=\"curve_chart-two\" style=\"width: 100%; height: 350px; margin:auto;\"></div>\n"
		  + "</div>\n"
		  + "<div class=\"graphcard\">\n"
		  + "<h3 style=\"text-align:left;\">Users Taking Quizzes</h3>\n"
		  + "<div id=\"curve_chart-three\" style=\"width: 100%; height: 350px; margin:auto;\"></div>\n"
		  + "</div>\n"
		  + "</body>\n"
		  + "</html>\n";
		return result;
	}
}