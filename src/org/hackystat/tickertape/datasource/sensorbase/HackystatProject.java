package org.hackystat.tickertape.datasource.sensorbase;

import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.CoverageDailyProjectData;
import org.hackystat.dailyprojectdata.resource.filemetric.jaxb.FileData;
import org.hackystat.dailyprojectdata.resource.filemetric.jaxb.FileMetricDailyProjectData;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataIndex;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataRef;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * A datasource corresponding to Hackystat services (sensorbase and dailyprojectdata). 
 * @author Philip Johnson
 */
public class HackystatProject {

  /** The sensorbase host URL, such as http://dasha.ics.hawaii.edu:9876/sensorbase. */
  private String sensorbaseHost;
  /** The dailyprojectdata host URL, such as http://dasha.ics.hawaii.edu:9877/dailyprojectdata. */
  private String dpdHost;
  /** The user used to get data, such as johnson@hawaii.edu. */
  private String user;
  /** The password for this user account. */
  private String password;
  /** The project whose data will be retrieved. */
  private String projectName;
  /** The owner of the project. */
  private String projectOwner;
  /** The interval in minutes for getting data. */
  private int interval;

  /**
   * Creates a new Hackystat instance, initializing the state. 
   * @param sensorbaseHost  The sensorbase host. 
   * @param dailyprojectdataHost  The dpd host.
   * @param user The sensorbase user. 
   * @param password The user's password.
   * @param projectName A project for which this user has access.
   * @param projectOwner The project's owner.  
   * @param interval The interval in minutes for which we should ask for data. 
   */
  public HackystatProject(String sensorbaseHost, String dailyprojectdataHost, String user, 
      String password, String projectName, String projectOwner, int interval) {
    this.sensorbaseHost = sensorbaseHost;
    this.dpdHost = dailyprojectdataHost;
    this.user = user;
    this.password = password;
    this.projectName = projectName;
    this.projectOwner = projectOwner;
    this.interval = interval;
  }
  
  /**
   * Contacts Hackystat services, gets data, and returns a summary as a string. 
   * @return The summary of Hackystat activity. 
   */
  public String getInfo() {
    // First, validate that we are authorized for these Hackystat services. 
    SensorBaseClient sensorbaseClient;
    DailyProjectDataClient dpdClient;
    try {
      sensorbaseClient = new SensorBaseClient(sensorbaseHost, user, password);
      dpdClient = new DailyProjectDataClient(dpdHost, user, password);
      sensorbaseClient.authenticate();
      dpdClient.authenticate();
    }
    catch (Exception e) {
      return "Error getting Hackystat info. Message is: " + e.getMessage();
    }
    // Second, validate that we can get info about the specified project. 
    try {
     sensorbaseClient.inProject(projectName, projectOwner);
    }
    catch (Exception e) {
      return String.format("%s is not in project %s", user, projectName);
    }
    
    // We're OK, so figure out the time interval of interest. 
    XMLGregorianCalendar endTime = Tstamp.makeTimestamp();
    XMLGregorianCalendar startTime = Tstamp.incrementMinutes(endTime, (-1 * interval));
    
    // Now get information.
    StringBuffer info = new StringBuffer();
    info.append(getDevEventInfo(sensorbaseClient, startTime, endTime)).append(' ');
    info.append(getCommitInfo(sensorbaseClient, startTime, endTime)).append(' ');
    info.append(getUnitTestInfo(sensorbaseClient, startTime, endTime)).append(' ');
    info.append(getBuildInfo(sensorbaseClient, startTime, endTime)).append(' ');
    info.append(getCoverageInfo(sensorbaseClient, dpdClient, startTime, endTime)).append(' ');
    info.append(getSizeInfo(sensorbaseClient, dpdClient, startTime, endTime)).append(' ');
    
    // Return the info we've found.
    return info.toString().trim().replace("  ", " ");
  }
  
  /**
   * Returns a summary string regarding DevEvent info, or the empty string if no info available.
   * @param client The SensorBaseClient. 
   * @param startTime The start time.
   * @param endTime The end time.
   * @return The summary string, or an empty string. 
   */
  private String getDevEventInfo(SensorBaseClient client, XMLGregorianCalendar startTime, 
      XMLGregorianCalendar endTime) {
    String message = ""; 
    try {
      SensorDataIndex index = client.getProjectSensorData(projectOwner, projectName, 
          startTime, endTime, "DevEvent");
      message = getWorkers(index, "been editing files.");
    }
    catch (Exception e) {
      message = "Error retrieving DevEvent data. ";
    }
    return message;
  }
  
  /**
   * Returns a summary string regarding Commit info, or the empty string if no info available.
   * @param client The SensorBaseClient. 
   * @param startTime The start time.
   * @param endTime The end time.
   * @return The summary string, or an empty string. 
   */
  private String getCommitInfo(SensorBaseClient client, XMLGregorianCalendar startTime, 
      XMLGregorianCalendar endTime) {
    String message = ""; 
    try {
      SensorDataIndex index = client.getProjectSensorData(projectOwner, projectName, 
          startTime, endTime, "Commit");
      message = getWorkers(index, "committed files.");
    }
    catch (Exception e) {
      message = "Error retrieving Commit data. ";
    }
    return message;
  }
  
  /**
   * Returns a summary string regarding Commit info, or the empty string if no info available.
   * @param client The SensorBaseClient. 
   * @param startTime The start time.
   * @param endTime The end time.
   * @return The summary string, or an empty string. 
   */
  private String getUnitTestInfo(SensorBaseClient client, XMLGregorianCalendar startTime, 
      XMLGregorianCalendar endTime) {
    String message = ""; 
    try {
      SensorDataIndex index = client.getProjectSensorData(projectOwner, projectName, 
          startTime, endTime, "UnitTest");
      message = getWorkers(index, "run tests.");
    }
    catch (Exception e) {
      message = "Error retrieving UnitTest data. ";
    }
    return message;
  }
  
  /**
   * Returns a summary string regarding Build info, or the empty string if no info available.
   * @param client The SensorBaseClient. 
   * @param startTime The start time.
   * @param endTime The end time.
   * @return The summary string, or an empty string. 
   */
  private String getBuildInfo(SensorBaseClient client, XMLGregorianCalendar startTime, 
      XMLGregorianCalendar endTime) {
    String message = ""; 
    try {
      SensorDataIndex index = client.getProjectSensorData(projectOwner, projectName, 
          startTime, endTime, "Build");
      message = getWorkers(index, "built the system.");
    }
    catch (Exception e) {
      message = "Error retrieving Build data. ";
    }
    return message;
  }
  
  /**
   * Returns a summary string regarding Build info, or the empty string if no info available.
   * @param client The SensorBaseClient. 
   * @param dpdClient The DailyProjectData client. 
   * @param startTime The start time.
   * @param endTime The end time.
   * @return The summary string, or an empty string. 
   */
  private String getCoverageInfo(SensorBaseClient client, DailyProjectDataClient dpdClient, 
      XMLGregorianCalendar startTime, XMLGregorianCalendar endTime) {
    String message = ""; 
    try {
      SensorDataIndex index = client.getProjectSensorData(projectOwner, projectName, 
          startTime, endTime, "Coverage");
      if (!index.getSensorDataRef().isEmpty()) {
        CoverageDailyProjectData dpd = dpdClient.getCoverage(projectOwner, projectName, startTime,
            "Line");
        double covered = 0.0;
        double total = 0.0;
        for (ConstructData data : dpd.getConstructData()) {
          covered += data.getNumCovered();
          total += data.getNumCovered() + data.getNumUncovered(); 
        }
        int percentCovered = (total == 0) ? 0 : (int)((covered / total) * 100);
        message = String.format("Line coverage is %s percent.", percentCovered);  
      }
    }
    catch (SensorBaseClientException e) {
      message = "Error retrieving Coverage sensor data. ";
    }
    catch (DailyProjectDataClientException e) {
      message = "Error retrieving Coverage DPD data. ";
    }
    return message;
  }
  
  /**
   * Returns a summary string regarding Size info, or the empty string if no info available.
   * @param client The SensorBaseClient. 
   * @param dpdClient The DailyProjectData client. 
   * @param startTime The start time.
   * @param endTime The end time.
   * @return The summary string, or an empty string. 
   */
  private String getSizeInfo(SensorBaseClient client, DailyProjectDataClient dpdClient, 
      XMLGregorianCalendar startTime, XMLGregorianCalendar endTime) {
    String message = ""; 
    try {
      SensorDataIndex index = client.getProjectSensorData(projectOwner, projectName, 
          startTime, endTime, "FileMetric");
      if (!index.getSensorDataRef().isEmpty()) {
        FileMetricDailyProjectData dpd = dpdClient.getFileMetric(projectOwner, projectName, 
            startTime, "TotalLines");
        int totalLines = 0;
        for (FileData data : dpd.getFileData()) {
          totalLines += data.getSizeMetricValue();
        }
        message = String.format("The system has %s lines of code.", totalLines);
      }
    }
    catch (SensorBaseClientException e) {
      message = "Error retrieving FileMetric sensor data. ";
    }
    catch (DailyProjectDataClientException e) {
      message = "Error retrieving FileMetric DPD data. ";
    }
    return message;
  }
  

  /**
   * Returns a string naming the users in the passed index who have been working on the specified
   * activity, or the empty string if there is nothing in the index.
   * @param index The index. 
   * @param activity the activity.
   * @return The string, possibly empty.
   */
  private String getWorkers (SensorDataIndex index, String activity) {
    // Find all of the developers who have been committing during this interval.
    Set<String> workers = new HashSet<String>();
    String message = "";
    for (SensorDataRef ref : index.getSensorDataRef()) {
      workers.add(ref.getOwner());
    }
    // Create a message indicating who has been working
    if (!workers.isEmpty()) {
      StringBuffer buff = new StringBuffer();
      for (String worker : workers) {
        String newWorker = worker.replace("@", "+at+").replace(".", "+dot+");
        buff.append(newWorker).append(' ');
      }
      message = buff.toString() + 
      ((workers.size() == 1) ? " has " : " have ") + activity + " ";
    }
    return message; 
  }
}
