package datasoulweb;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class GeoIP {
    private static final Logger    logger     = Logger.getLogger(GeoIP.class.getName());
    private static final String    USER_AGENT = "GAE/" + GeoIP.class.getName();
    private static final String    GEOIP_URL  = "http://geoip.wtanaka.com/cc/";
    private static MemcacheService memcache;
    private static URLFetchService urlfetch;

    static {
        memcache = MemcacheServiceFactory.getMemcacheService();
    }

    public static String fetchCountry(String ip) {
        try {
            if (urlfetch == null) {
                urlfetch = URLFetchServiceFactory.getURLFetchService();
            }

            URL url = new URL(GEOIP_URL + ip);
            HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET);
            request.addHeader(new HTTPHeader("User-Agent", USER_AGENT));

            HTTPResponse response = urlfetch.fetch(request);
            if (response.getResponseCode() != 200) {
                return null;
            }

            String country = new String(response.getContent(), "UTF-8");
            return country;
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Unable to fetch GeoIp for " + ip, t);
            return null;
        }
    }

    public static String getClientCountry(HttpServletRequest request) {
        return getCountry(request.getRemoteAddr());
    }

    public static String getCountry(String ip) {
        if (ip == null) {
            return null;
        }

        String cacheid = "geoip-" + ip;
        String country = (String) memcache.get(cacheid);

        if (country == null) {
            // Perform lookup at geoip.wtanaka.com
            country = fetchCountry(ip);

            if (country != null) {
                // Store lookup in memcache.
                memcache.put(cacheid, country);
            }
        }

        return country;
    }

}
