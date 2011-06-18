package datasoulweb;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class ServiceServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String title = URLDecoder.decode(req.getParameter("title"), "UTF-8");
		String content = URLDecoder.decode(req.getParameter("content"), "UTF-8");
		String locale = URLDecoder.decode(req.getParameter("locale"), "UTF-8");
		String desc = URLDecoder.decode(req.getParameter("description"), "UTF-8");
		
		long key = (long) Math.round(Math.random() * Integer.MAX_VALUE);
		
		ServicePlan sp = new ServicePlan();
		
		sp.setContent(new Text(content));
		sp.setTitle(title);
		sp.setCreated(new Date());
		sp.setKey(key);
		sp.setLocale(locale);
		sp.setDescription(desc);
		sp.setSourceip(req.getRemoteAddr());
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(sp);
		}finally{
			pm.close();
		}
		
		String baseurl = req.getRequestURL().toString();
		String url = Long.toString(sp.getId())+"-"+Long.toString(key, 30);
		
		resp.getWriter().println(baseurl+url);
		
	}
	
	public void renderOutput(HttpServletRequest req, HttpServletResponse resp, ServicePlan sp) throws IOException {
		resp.getWriter().println("<html><head>");
		resp.getWriter().println("<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js'></script>");
		resp.getWriter().println("<script src='http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js'></script>");
		resp.getWriter().println("<link rel='stylesheet' type='text/css' href='http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/themes/smoothness/jquery-ui.css' />");
		resp.getWriter().println("<link rel='stylesheet' type='text/css' href='/css/datasoul.css' />");

		String url = req.getRequestURL().toString();
		if (sp != null){
			
			resp.getWriter().println("<script>");
			resp.getWriter().println(sp.getContent().getValue());
			resp.getWriter().println("</script>");
			
			if (sp.getLocale().equals("pt") || sp.getLocale().equals("fr") || sp.getLocale().equals("de")){
				resp.getWriter().println("<script src='/js/datasoul."+sp.getLocale()+".js'></script>");
			}else{
				resp.getWriter().println("<script src='/js/datasoul.en.js'></script>");
			}
			
			resp.getWriter().println("<title>"+sp.getTitle()+"</title>");
			
			resp.getWriter().println("<meta property='og:title' content='"+sp.getTitle()+"' />");
			resp.getWriter().println("<meta property='og:description' content='"+sp.getDescription()+"' />");
			resp.getWriter().println("<meta property='og:url' content='"+url+"' />");
			resp.getWriter().println("<meta property='og:site_name' content='Datasoul' />");
			resp.getWriter().println("<meta property='og:type' content='activity' />");
			
		}else{
			resp.getWriter().println("<script>");
			resp.getWriter().println("$(document).ready(function(){");
			resp.getWriter().println("$('#loading').hide();");
			resp.getWriter().println("$('#bodycont').show();");
			resp.getWriter().println("});");
			resp.getWriter().println("</script>");

		}
		
		resp.getWriter().println("<script src='/js/datasoulservice.js'></script>");
		resp.getWriter().println("</head>");
		resp.getWriter().println("<body>");
		resp.getWriter().println("<div id='topbar'><a href='http://code.google.com/p/datasoul/'><img src='/img/datasoul.png'></a></div>");

		resp.getWriter().println("<div id='loading'><img src='/img/loading.gif'></div>");

		resp.getWriter().println("<div id='bodycont'>");
		
		if (sp != null){
			resp.getWriter().println("<h1>"+sp.getTitle()+"</h1>");
			resp.getWriter().println("<h2>"+sp.getDescription()+"</h2>");
			resp.getWriter().println("<div id='servtable'></div><div id='servnotes'></div><div id='content'></div>");
			resp.getWriter().println("<p>&nbsp;</p>");
			resp.getWriter().println("<div id='fb-root'></div>");
			resp.getWriter().println("<script src='http://connect.facebook.net/en_US/all.js#appId=162685747132742&amp;xfbml=1'></script>");
			resp.getWriter().println("<fb:like href='"+url+"' send='true' width='780' show_faces='true' font=''></fb:like>");
			resp.getWriter().println("<fb:comments href='"+url+"' num_posts='10' width='780'></fb:comments>");
		}else{
			resp.getWriter().println("<h1><div id='servtitle'>Service Not Found</div></h1>");
		}
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div id='footnote'></div>");
		resp.getWriter().println("</body></html>");

	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
        String reqstr[] = req.getPathInfo().split("-");
        if (reqstr.length != 2){
        	renderOutput(req, resp, null);
        	return;
        }

        long id;
        int auth;

        try{
            id = Long.parseLong(reqstr[0].substring(1));
            auth = Integer.parseInt(reqstr[1], 30);
        }catch(NumberFormatException e){
        	renderOutput(req, resp, null);
            return;
        }

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			ServicePlan evt;
			try {
				evt = pm.getObjectById(ServicePlan.class, id);
			} catch (JDOObjectNotFoundException ex) {
	        	renderOutput(req, resp, null);
				return;
			}

			if (evt.getKey() != auth) {
	        	renderOutput(req, resp, null);
				return;
			}

	    	renderOutput(req, resp, evt);
	    	
		} finally {
			pm.close();
		}

	}
	
	
}
