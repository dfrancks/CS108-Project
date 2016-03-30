package finalproject;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/AnnouncementServlet")
public class AnnouncementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnnouncementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AnnouncementManager am = (AnnouncementManager)request.getServletContext().getAttribute(AnnouncementManager.ATTR_NAME);
		if (request.getParameter("Path").equals("Save Changes")){
			ArrayList<Announcement> announcements = am.getAnnouncements();
			for (int i = 0; i < announcements.size(); i++){
				int curID = announcements.get(i).getID();
				boolean checked = true;
				if (request.getParameter(Integer.toString(curID)) == null)
					checked = false;
				am.updateAnnouncement(curID, checked);
			}			
		} else {
			String announcement = request.getParameter("announcement");
			am.insertAnnouncement(announcement, true);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("manage-announcements.jsp");
		dispatcher.include(request, response);
	}

}