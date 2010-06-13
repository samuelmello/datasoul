package datasoulweb;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GetLatestVersionServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		ConfigParam param;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			try{
				param =  pm.getObjectById(ConfigParam.class, "LATEST_VERSION");
			}catch(JDOObjectNotFoundException ex){
				// Not found, lets create one
				param = new ConfigParam();
				param.setName("LATEST_VERSION");
				pm.makePersistent(param);
			}
			
			resp.getWriter().println(param.getValue());
			
		}finally{
			pm.close();
		}

	}
	
	
}
