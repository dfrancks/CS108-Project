package finalproject;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class AccountListener
 *
 */
@WebListener
public class ManagerListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ManagerListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {  }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
    	ServletContext context = arg0.getServletContext();
        context.setAttribute(AccountManager.ATTR_NAME, new AccountManager());
        context.setAttribute(MessageManager.ATTR_NAME, new MessageManager());
        context.setAttribute(QuestionManager.ATTR_NAME, new QuestionManager());
        context.setAttribute(QuizManager.ATTR_NAME, new QuizManager());
        context.setAttribute(UserManager.ATTR_NAME, new UserManager());
        context.setAttribute(AchievementManager.ATTR_NAME, new AchievementManager());
        context.setAttribute(AnalyticsManager.ATTR_NAME, new AnalyticsManager());
        context.setAttribute(AnnouncementManager.ATTR_NAME, new AnnouncementManager());
        context.setAttribute(ResultManager.ATTR_NAME, new ResultManager());
    }
	
}
