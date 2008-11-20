package org.hackystat.tickertape.tickerlingua;

import java.lang.reflect.Constructor;
import java.util.List;
import org.hackystat.tickertape.ticker.Ticker;

public class Tickertape { 
  
  private String id;
  private double intervalHours;
  private boolean enabled;
  private String starttime;
  private String description;
  private List<HackystatProject> projects;
  private List<NotificationService> services;
  private Class<? extends Ticker> classInstance; 
  
  /**
   * Creates and returns a new Tickertape instance, or throws a TickerLinguaException if the 
   * passed parameters are not valid.
   * @param id  A unique id (not validated).
   * @param intervalHoursString Must be a double.
   * @param enabledString  Must be a boolean.
   * @param starttime  A string indicating the start time. Not yet validated. 
   * @param description A description of this tickertape.
   * @param projects A non-empty list of HackystatProject instances. 
   * @param services A non-empty list of notification services. 
   * @param classNameString A fully qualified class name. 
   * @throws TickerLinguaException If intervalHoursString, enabledString, projects, services, or
   * classNameString is not valid.
   */
  public Tickertape(String id, String intervalHoursString, String enabledString, String starttime, 
      String description, List<HackystatProject> projects, List<NotificationService> services, 
      String classNameString) throws TickerLinguaException {
    this.id = id;
    try {
      this.intervalHours = Double.parseDouble(intervalHoursString);
    }
    catch (Exception e) {
      throw new TickerLinguaException("IntervalHours not a double: " + intervalHoursString, e);
    }
    try {
      this.enabled = Boolean.parseBoolean(enabledString);
    }
    catch (Exception e) {
      throw new TickerLinguaException("Enabled not a boolean: " + enabledString, e);
    }
    this.starttime = starttime;
    this.description = description;
    this.projects = projects;
    if (this.projects.isEmpty()) {
      throw new TickerLinguaException("At least one project must be defined.");
    }
    this.services = services;
    if (this.services.isEmpty()) {
      throw new TickerLinguaException("At least one service must be defined.");
    }
    try {
      Class<?> c = Class.forName(classNameString);
      this.classInstance = c.asSubclass(Ticker.class);
      // Make an instance now to ensure it's OK. If problems will throw an exception.
      Constructor<? extends Ticker> ctor = classInstance.getConstructor();
      ctor.newInstance();
    }
    catch (Exception e) {
      throw new TickerLinguaException("Problem defining ticker class. " + classNameString, e);
    }
  }
  
  public String getId() {
    return this.id;
  }
  
  public double getIntervalHours() {
    return this.intervalHours;
  }
  
  public boolean enabled() {
    return this.enabled;
  }
  
  public String getStartTime() {
    return this.starttime;
  }
  
  public List<HackystatProject> getHackystatProjects() {
    return this.projects;
  }
  
  public List<NotificationService> getNotificationServices() {
    return this.services;
  }
  
  public Ticker getTicker() {
    try {
      Constructor<? extends Ticker> ctor = classInstance.getConstructor();
      return ctor.newInstance();
    }
    catch (Exception e) {
      throw new RuntimeException("Ticker could not be instantiated. Shouldn't happen!", e);
    }
    
  }
}
