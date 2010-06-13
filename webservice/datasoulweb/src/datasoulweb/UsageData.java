package datasoulweb;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UsageData {

    @PrimaryKey
    @Persistent
    private String sysid;

    @Persistent
    private String osname;    

    @Persistent
    private String osversion;    

    @Persistent
    private String javaversion;    

    @Persistent
    private String dsversion;    

    @Persistent
    private Integer songall;
    
    @Persistent
    private Integer songchords;    

    @Persistent
    private Integer templates;
    
    @Persistent
    private Integer numDisplays;
    
    @Persistent
    private String geometryDisplay1;

    @Persistent
    private String geometryDisplay2;

    @Persistent
    private Integer usageUpdateCount;
    
    @Persistent
    private String country;
    
    public String getOsname() {
		return osname;
	}

	public void setOsname(String osname) {
		this.osname = osname;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public String getJavaversion() {
		return javaversion;
	}

	public void setJavaversion(String javaversion) {
		this.javaversion = javaversion;
	}

	public String getDsversion() {
		return dsversion;
	}

	public void setDsversion(String dsversion) {
		this.dsversion = dsversion;
	}

	public int getSongall() {
		return songall;
	}

	public void setSongall(String songall) {
		try{
			this.songall = Integer.parseInt(songall);
		}catch(Exception e){
			// ignore
		}
	}

	public int getSongchords() {
		return songchords;
	}

	public void setSongchords(String songchords) {
		try{
			this.songchords = Integer.parseInt(songchords);
		}catch(Exception e){
			// ignore
		}
	}

	public int getTemplates() {
		return templates;
	}

	public void setTemplates(String templates) {
		try{
			this.templates = Integer.parseInt(templates);
		}catch(Exception e){
			// ignore
		}
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public Integer getNumDisplays() {
		return numDisplays;
	}

	public void setNumDisplays(String numDisplays) {
		try{
			this.numDisplays = Integer.parseInt(numDisplays);
		}catch(Exception e){
			// ignore
		}
	}

	public String getGeometryDisplay1() {
		return geometryDisplay1;
	}

	public void setGeometryDisplay1(String geometryDisplay1) {
		this.geometryDisplay1 = geometryDisplay1;
	}

	public String getGeometryDisplay2() {
		return geometryDisplay2;
	}

	public void setGeometryDisplay2(String geometryDisplay2) {
		this.geometryDisplay2 = geometryDisplay2;
	}

	public void setSongall(Integer songall) {
		this.songall = songall;
	}

	public void setSongchords(Integer songchords) {
		this.songchords = songchords;
	}

	public void setTemplates(Integer templates) {
		this.templates = templates;
	}

	public Integer getUsageUpdateCount() {
		if (usageUpdateCount != null)
			return usageUpdateCount;
		else
			return 0;
	}

	public void setUsageUpdateCount(Integer usageUpdateCount) {
		this.usageUpdateCount = usageUpdateCount;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
    
}
