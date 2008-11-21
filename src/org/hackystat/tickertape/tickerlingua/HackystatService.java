package org.hackystat.tickertape.tickerlingua;

/**
 * A read-only record for Hackystat services. 
 * @author Philip Johnson
 */
public class HackystatService {
  
  private String id;
  private String sensorbase;
  private String dailyprojectdata;
  private String telemetry;
  private String projectbrowser;

  /**
   * Construct the Hackystat service. 
   * @param id The service id.
   * @param sensorbase The sensorbase service URL.
   * @param dailyprojectdata The DPD service URL.
   * @param telemetry The telemetry service URL.
   * @param projectbrowser The projectbrowser service URL. 
   */
  public HackystatService(String id, String sensorbase, String dailyprojectdata, String telemetry,
      String projectbrowser) {
   this.id = id;
   this.sensorbase = sensorbase;
   this.dailyprojectdata = dailyprojectdata;
   this.telemetry = telemetry;
   this.projectbrowser = projectbrowser;
  }
  
  /**
   * Constructs the instance from the passed JAXB service instance. 
   * @param service The JAXB service instance. 
   */
  public HackystatService(org.hackystat.tickertape.tickerlingua.jaxb.HackystatService service) {
    this(service.getId(), service.getSensorbase(), service.getDailyprojectdata(),  
        service.getTelemetry(), service.getProjectbrowser());
  }
  
  /** @return The unique id. */
  public String getId() {
    return this.id;
  }
  
  /** @return The sensorbase service URL. */
  public String getSensorbase() {
    return this.sensorbase;
  }
  
  /** @return The DPD service URL. */
  public String getDailyProjectData() {
    return this.dailyprojectdata;
  }
  
  /** @return The telemetry service URL. */
  public String getTelemetry() {
    return this.telemetry;
  }
  
  /** @return The projectbrowser URL. */
  public String getProjectBrowser() {
    return this.projectbrowser;
  }

}
