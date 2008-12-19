package org.hackystat.tickertape.ticker.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.sensorbase.client.SensorBaseClient;

/**
 * Provides an abstract data type for managing a set of ProjectSensorDataLogs. 
 * @author Philip Johnson
 *
 */
public class MultiProjectSensorDataLog implements Iterable<ProjectSensorDataLog> {
  
  /** A map of projects to the associated SensorDataLog.  The key is {project}:{owner} */
  private Map<String, ProjectSensorDataLog> project2log = 
    new HashMap<String, ProjectSensorDataLog>();
  
  /**
   * Constructs a new MultiProjectSensorDataLog.
   */
  public MultiProjectSensorDataLog() {
    // nothing needed yet. 
  }
  
  /**
   * Return the ProjectSensorDataLog for this project, creating it if it does not yet exist. 
   * @param client The SensorBaseClient.
   * @param maxLife The maxLife for sensor data entries in this log.
   * @param projectOwner The owner.
   * @param projectName The name.
   * @param logger The logger to be used if things go wrong. 
   * @return The ProjectSensorDataLog.
   */
  public ProjectSensorDataLog get(SensorBaseClient client, double maxLife,
      String projectOwner, String projectName, Logger logger) {
    String key = projectName + ":" + projectOwner;
    // If we can't find it, create it.
    if (!this.project2log.containsKey(key)) {
      ProjectSensorDataLog log = new ProjectSensorDataLog(client, maxLife, projectOwner,
          projectName, logger);
      this.project2log.put(key, log);
    }
    return this.project2log.get(key);    
  }
 
  /**
   * Returns true if any of the ProjectSensorDataLogs have data. 
   * @return True if there is data. 
   */
  public boolean hasSensorData() {
    for (ProjectSensorDataLog log : this.project2log.values()) {
      if (log.hasSensorData()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns an iterator over the ProjectSensorDataLogs in this collection.
   * @return An iterator.
   */
  public Iterator<ProjectSensorDataLog> iterator() {
    return this.project2log.values().iterator();
  }
  
  

}
