package org.hackystat.tickertape.tickerlingua;

import java.lang.reflect.Constructor;
import java.util.List;
import org.hackystat.tickertape.ticker.Ticker;

/**
 * Represents a Tickertape, which is a notification. 
 * @author Philip Johnson
 */
public class Tickertape { 
  
  private String id;
  private double intervalHours;
  private boolean enabled;
  private String starttime;
  private String description;
  private List<HackystatProject> projects;
  private List<NotificationService> services;
  private Class<? extends Ticker> tickerClass; 
  
  /**
   * Creates and returns a new Tickertape instance, or throws a TickerLinguaException if the 
   * passed parameters are not valid.
   * @param id  A unique id (not validated).
   * @param intervalHours The interval as hours.
   * @param enabled If this ticker is to be instantiated or not. 
   * @param starttime  A string indicating the start time. Not yet validated. 
   * @param description A description of this tickertape.
   * @param projects A non-empty list of HackystatProject instances. 
   * @param services A possibly empty list of notification services. (One might use email). 
   * @param tickerClass The ticker class instance. 
   * @throws TickerLinguaException If there is not at least one project and notification service. 
   */
  public Tickertape(String id, double intervalHours, boolean enabled, String starttime, 
      String description, List<HackystatProject> projects, List<NotificationService> services, 
      Class<? extends Ticker> tickerClass) throws TickerLinguaException {
    this.id = id;
    this.intervalHours = intervalHours;
    this.enabled = enabled;
    this.starttime = starttime;
    this.description = description;
    this.projects = projects;
    if (this.projects.isEmpty()) {
      throw new TickerLinguaException("At least one project must be defined.");
    }
    this.services = services;
    this.tickerClass = tickerClass;
  }
  
  /**
   * Return the unique ID.
   * @return The id.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Return the interval in hours for wakeups of this notification. 
   * @return The wakeup interval.
   */
  public double getIntervalHours() {
    return this.intervalHours;
  }
  
  /**
   * True if this tickertape is enabled.
   * @return True if enabled.
   */
  public boolean enabled() {
    return this.enabled;
  }
  
  /**
   * The description of this tickertape.
   * @return The description.
   */
  public String getDescription() {
    return this.description;
  }
  
  /**
   * The String representting the time when this should wake up, or null if immediately.
   * @return The time to wakeup, or null if immediate.
   */
  public String getStartTime() {
    return this.starttime;
  }
  
  /**
   * The set of projects involved in this notification. 
   * @return The projects. 
   */
  public List<HackystatProject> getHackystatProjects() {
    return this.projects;
  }
  
  /**
   * The notification services for this notification. 
   * @return The notification services. 
   */
  public List<NotificationService> getNotificationServices() {
    return this.services;
  }
  
  /**
   * The class that implements this notification behavior. 
   * @return The notification class. 
   */
  public Ticker getTicker() {
    try {
      Constructor<? extends Ticker> ctor = tickerClass.getConstructor();
      return ctor.newInstance();
    }
    catch (Exception e) {
      throw new RuntimeException("Ticker could not be instantiated. Shouldn't ever happen!", e);
    }
    
  }
}
