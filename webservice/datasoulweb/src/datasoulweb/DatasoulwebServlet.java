package datasoulweb;
import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DatasoulwebServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String sysid = req.getParameter("sysid");
		
		if (sysid != null && sysid.length() > 0){
			
			UsageData usage;
		
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try{
				try{
					usage =  pm.getObjectById(UsageData.class, sysid);
				}catch(JDOObjectNotFoundException ex){
					// Not found, lets create one
					usage = new UsageData();
					usage.setSysid(sysid);
					pm.makePersistent(usage);
				}
				
				// Update data
				usage.setOsname(req.getParameter("osname"));
				usage.setOsversion(req.getParameter("osversion"));
				usage.setJavaversion(req.getParameter("javaversion"));
				usage.setDsversion(req.getParameter("dsversion"));
				usage.setTemplates(req.getParameter("templates"));
				usage.setSongall(req.getParameter("songall"));
				usage.setSongchords(req.getParameter("songchords"));
				usage.setNumDisplays(req.getParameter("numdisplays"));
				usage.setGeometryDisplay1(req.getParameter("geometry1"));
				usage.setGeometryDisplay2(req.getParameter("geometry2"));
				usage.setCountry(GeoIP.getClientCountry(req));
				usage.setUsageUpdateCount(usage.getUsageUpdateCount()+1);
				
			}finally{
				pm.close();
			}
			resp.getWriter().println("Done");
			
		}else{
			resp.getWriter().println("Malformed request");
		}
	}
}
