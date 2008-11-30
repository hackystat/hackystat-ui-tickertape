package org.hackystat.tickertape.ticker.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.logging.Logger;

import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataRef;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataIndex;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides a sliding window of recent project data, along with a record of the last time 
 * a tweet was generated from this data. 
 * @author Philip Johnson
 */
public class ProjectSensorDataLog {
  
  /** Maps the time when data was retrieved the list of data of interest. */
  Map<XMLGregorianCalendar, List<SensorDataRef>> timestamp2SensorDatas = 
    new TreeMap<XMLGregorianCalendar, List<SensorDataRef>>();
  Map<XMLGregorianCalendar, Project> timestamp2Project =
    new TreeMap<XMLGregorianCalendar, Project>();
  
  private SensorBaseClient client = null;
  private long maxLifeInMillis;
  private XMLGregorianCalendar lastUpdate = null;
  private String projectOwner;
  private String projectName;
  private Logger logger;

  private List<SensorDataRef> emptyDataList;

  private Map<String, XMLGregorianCalendar> user2lastTweet =
    new TreeMap<String, XMLGregorianCalendar>();

  /**
   * Creates a new ProjectSensorDataLog that maintains a sliding window of data.
   * @param client The SensorBaseClient used to retrieve the data. 
   * @param maxLife The window size, in hours. 
   * @param projectOwner The project owner. 
   * @param projectName The project name.
   * @param logger The logger to be used if problems occur.
   */
  public ProjectSensorDataLog(SensorBaseClient client, double maxLife, String projectOwner,
      String projectName, Logger logger) {
    this.client = client;
    this.maxLifeInMillis = (long) (60 * 60 * 1000 * maxLife);
    this.projectOwner = projectOwner;
    this.projectName = projectName;
    this.logger = logger;
    this.emptyDataList = new ArrayList<SensorDataRef>();
  }
  
  /**
   * Returns the timestamp corresponding to (Current time - maxLife).
   * @return The maxlife timestamp.
   */
  private XMLGregorianCalendar getMaxLifeTimestamp() {
    XMLGregorianCalendar currTime = Tstamp.makeTimestamp();
    return Tstamp.incrementMilliseconds(currTime, -1 * this.maxLifeInMillis); 
  }
  
  /**
   * Retrieves project SensorDataRefs since the last time it was called. 
   * If this is the first invocation, then retrieves data for an interval corresponding
   * to maxLife. 
   */
  public void update() {
    // If this is the first update call, set lastUpdate to (now - maxlife).
    if (lastUpdate == null) {
      this.lastUpdate = getMaxLifeTimestamp(); 
    }
    // Now retrieve all sensordata for the interval.
    XMLGregorianCalendar currTime = Tstamp.makeTimestamp();
    SensorDataIndex index = null;
    try {
      index = client.getProjectSensorData(projectOwner, projectName, lastUpdate, currTime);
    }
    catch (Exception e) {
      this.logger.warning("Project Sensor Data request failed: " + e.getMessage());
    }
    // Now update lastUpdate.
    this.lastUpdate = currTime;
    
    // Update our data structure with the SensorDataRefs, if any.
    if ((index == null) || (index.getSensorDataRef() == null)) {
      this.timestamp2SensorDatas.put(currTime, this.emptyDataList);
    }
    else {
      this.timestamp2SensorDatas.put(currTime, index.getSensorDataRef());
    }
    
    // Update the project data structure with the current project definition.
    Project project;
    try {
      project = client.getProject(this.projectOwner, this.projectName);
      this.timestamp2Project.put(this.lastUpdate, project);
    }
    catch (Exception e) {
      this.logger.warning("Project definition request failed: " + e.getMessage()); 
    }
  }
  
  /**
   * Indicate that a tweet was generated based upon the last received sensor data for the 
   * specified user.
   * @param user The user who had a tweet generated. 
   */
  public void setTweet(String user) {
    this.user2lastTweet.put(user, this.lastUpdate);
  }
  
  /**
   * Returns the list consisting of the project owner and all members from the last update, 
   * or an empty list if problems occurred.
   * 
   * @return The (possibly empty) list of project members.
   */
  public List<String> getProjectParticipants() {
    List<String> participants = new ArrayList<String>();
    if (this.timestamp2Project.containsKey(this.lastUpdate)) {
      Project project = this.timestamp2Project.get(this.lastUpdate);
      participants.add(project.getOwner());
      if (!(project.getMembers() == null)) {
        for (String member : project.getMembers().getMember()) {
          participants.add(member);
        }
      }
    }
    return participants;
  }
  
  /**
   * Indicate that a tweet has been generated at least once based upon data received within 
   * the maxLife interval for the specified user. 
   * @param user The user of interest.
   * @return True if a tweet has been generated recently.
   */
  public boolean hasRecentTweet(String user) {
    if (!this.user2lastTweet.containsKey(user)) {
      return false;
    }
    else {
      // Return true if lastTweet is greater than or equal to the maxLifeTimestamp.
      return !Tstamp.lessThan(this.getMaxLifeTimestamp(), this.user2lastTweet.get(user));
    }
  }
  
  /**
   * Returns the (potentially empty) list of sensordatarefs received during the last update 
   * for the specified user. 
   * @return The list of sensordatarefs, possibly empty.
   */
  public List<SensorDataRef> getRecentSensorDataRefs(String user) {
    if (this.timestamp2SensorDatas.containsKey(this.lastUpdate)) {
      List<SensorDataRef> refs = new ArrayList<SensorDataRef>();
      for (SensorDataRef ref : this.timestamp2SensorDatas.get(this.lastUpdate)) {
        if (ref.getOwner().equals(user)) {
          refs.add(ref);
        }
      }
      return refs;
    }
    else {
      // Should never happen, but just in case. 
      this.logger.warning("Could not find data for last update");
      return this.emptyDataList;
    }
  }
  
  /**
   * Returns true if this user has data from the last update. 
   * @return True if there is sensor data for this user.
   */
  public boolean hasRecentSensorData(String user) {
    if (this.timestamp2SensorDatas.containsKey(this.lastUpdate)) {
      for (SensorDataRef ref : this.timestamp2SensorDatas.get(this.lastUpdate)) {
        if (ref.getOwner().equals(user)) {
          return true;
        }
      }
    }
    return false;
  }
}
