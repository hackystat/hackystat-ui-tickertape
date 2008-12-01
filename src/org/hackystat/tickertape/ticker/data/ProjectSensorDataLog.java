package org.hackystat.tickertape.ticker.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.logging.Logger;

import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataRef;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorData;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataIndex;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides a sliding window of recent project data, along with a record of the last time 
 * a tweet was generated from this data. 
 * @author Philip Johnson
 */
public class ProjectSensorDataLog {
  
  /** Maps the time when data was retrieved the list of data of interest. */
  Map<XMLGregorianCalendar, List<SensorData>> timestamp2SensorDatas = 
    new HashMap<XMLGregorianCalendar, List<SensorData>>();
  Map<XMLGregorianCalendar, Project> timestamp2Project =
    new HashMap<XMLGregorianCalendar, Project>();
  
  private SensorBaseClient client = null;
  private long maxLifeInMillis;
  private XMLGregorianCalendar lastUpdate = null;
  private String projectOwner;
  private String projectName;
  private Logger logger;

  private List<SensorData> emptyDataList;

  private Map<String, XMLGregorianCalendar> user2lastTweet =
    new HashMap<String, XMLGregorianCalendar>();

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
    this.emptyDataList = new ArrayList<SensorData>();
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
      List<SensorData> sensordata = new ArrayList<SensorData>();
      for (SensorDataRef ref : index.getSensorDataRef()) {
        try {
          SensorData data = this.client.getSensorData(ref);
          sensordata.add(data);
        }
        catch (Exception e) {
          this.logger.warning("Failed to retrieve sensor data: " + e.getMessage());
        }
      }
      this.timestamp2SensorDatas.put(currTime, sensordata);
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
    
    // Remove any entries that are older than maxLife
    XMLGregorianCalendar maxLifeTimestamp = this.getMaxLifeTimestamp();
    for (XMLGregorianCalendar tstamp : this.timestamp2SensorDatas.keySet()) {
      if (Tstamp.lessThan(tstamp, maxLifeTimestamp)) {
        this.timestamp2SensorDatas.remove(tstamp);
        this.timestamp2Project.remove(tstamp);
      }
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
   * Indicate if a tweet has been generated at least once based upon data received within 
   * the maxLife interval for the specified user. 
   * @param user The user of interest.
   * @return True if a tweet has been generated recently.
   */
  public boolean hasRecentTweet(String user) {
    if (this.user2lastTweet.containsKey(user)) {
      return !Tstamp.lessThan(this.getMaxLifeTimestamp(), this.user2lastTweet.get(user));
    }
    return false;
  }
  
  /**
   * Returns the (potentially empty) list of sensordatarefs received during the last update 
   * for the specified user. 
   * @param user The user.
   * @return The list of sensordatarefs, possibly empty.
   */
  public List<SensorData> getRecentSensorData(String user) {
    if (this.timestamp2SensorDatas.containsKey(this.lastUpdate)) {
      List<SensorData> datas = new ArrayList<SensorData>();
      for (SensorData data : this.timestamp2SensorDatas.get(this.lastUpdate)) {
        if (data.getOwner().equals(user)) {
          datas.add(data);
        }
      }
      return datas;
    }
    else {
      // Should never happen, but just in case. 
      this.logger.warning("Could not find data for last update");
      return this.emptyDataList;
    }
  }
  
  /**
   * Returns true if this user has data from the last update. 
   * @param user The user.
   * @return True if there is sensor data for this user.
   */
  public boolean hasRecentSensorData(String user) {
    if (this.timestamp2SensorDatas.containsKey(this.lastUpdate)) {
      for (SensorData data : this.timestamp2SensorDatas.get(this.lastUpdate)) {
        if (data.getOwner().equals(user)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns a list of all SensorData in our sliding window of data that was generated by the 
   * given user and is of the given SensorDataType. 
   * @param user The user of interest. 
   * @param sdt The sensor data type of interest.
   * @return A list of matching SensorData.
   */
  public List<SensorData> getSensorData(String user, String sdt) {
    List<SensorData> datas = new ArrayList<SensorData>();
    for (Map.Entry<XMLGregorianCalendar, List<SensorData>> entry : 
      this.timestamp2SensorDatas.entrySet()) {
      for (SensorData data : entry.getValue()) {
        if ((user.equals(data.getOwner())) &&
            (sdt.equals(data.getSensorDataType()))) {
          datas.add(data);
        }
      }
    }
    return datas;
  }

  /**
   * Returns true if there is at least one SensorDataRef in our sliding window of data that 
   * was generated by the given user and is of the given SensorDataType.
   * @param user The user of interest. 
   * @param sdt The sensor data type of interest.
   * @return True if data of the specified type is present. 
   */
  public boolean hasSensorData(String user, String sdt) {
    for (Map.Entry<XMLGregorianCalendar, List<SensorData>> entry : 
      this.timestamp2SensorDatas.entrySet()) {
      for (SensorData data : entry.getValue()) {
        if ((user.equals(data.getOwner())) &&
            (sdt.equals(data.getSensorDataType()))) {
          return true;
        }
      }
    }
    return false;
  }
  

  /**
   * Returns a count of the number of files for which DevEvent sensor data has been generated
   * in the current sliding window of data.
   * Requires one HTTP call per DevEvent. 
   * @param user The user of interest. 
   * @return The number of files associated with DevEvents during this interval.
   */
  public int getNumFilesWorkedOn(String user) {
    List<SensorData> datas = getSensorData(user, "DevEvent");
    Set<String> files = new HashSet<String>();
    if (!datas.isEmpty()) {
      try {
        for (SensorData data : datas) {
          files.add(data.getResource());
        }
      }
      catch (Exception e) {
        this.logger.warning("Error occurred retrieving sensor data: " + e.getMessage());
      }
    }
    return files.size();
  }
  
  /**
   * Returns the file that the user worked on the most during the sliding window of data. 
   * Requires one HTTP call per DevEvent. 
   * @param user The user.
   * @return The file they worked on most, or null if no DevEvent data. 
   */
  public String mostWorkedOnFile(String user) {
    List<SensorData> datas = getSensorData(user, "DevEvent");
    Map<String, Integer> file2NumOccurrences = new HashMap<String, Integer>();
    if (datas.isEmpty()) {
      return null;
    }
    try {
      for (SensorData data : datas) {
        String file = data.getResource();
        if (!file2NumOccurrences.containsKey(file)) {
          file2NumOccurrences.put(file, 0);
        }
        int currOccurrences = file2NumOccurrences.get(file);
        file2NumOccurrences.put(file, currOccurrences++);
      }
    }
    catch (Exception e) {
      this.logger.warning("Error occurred retrieving sensor data: " + e.getMessage());
    }
    // Now find the one that occurred most frequently. 
    int mostOccurred = 0;
    String mostOccurredFile = null;
    for (Map.Entry<String, Integer> entry : file2NumOccurrences.entrySet()) {
      if (entry.getValue() > mostOccurred) {
        mostOccurred = entry.getValue();
        mostOccurredFile = entry.getKey();
      }
    }
    // Now return the mostOccurredFile's file name.
    return getFileName(mostOccurredFile);
  }


  /**
   * Returns the file name associated with filePath.
   * @param filePath The file path. 
   * @return The file name. 
   */
  private String getFileName(String filePath) {
    int sepPos = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
    return filePath.substring(sepPos);
  }
}
