package org.hackystat.tickertape.server;

import java.util.logging.Logger;

import org.hackystat.tickertape.datasource.sensorbase.HackystatProject;
import org.hackystat.tickertape.notifier.nabaztag.Nabaztag;
import org.hackystat.utilities.logger.HackystatLogger;

/**
 * The main point of entry for this system. 
 * Reads in the tickertape.xml file, sets up the timer-based processes. 
 * @author Philip Johnson
 */
public class Server {
  /** The logger for this service. */
  private Logger logger = HackystatLogger.getLogger("org.hackystat.tickertape", "tickertape");
  
  /**
   * Constructs a new instance of a Tickertape server. 
   * Sends a log message to indicate the server is running. 
   */
  public Server () {
    logger.info("Starting up tickertape server");
  }
  
  /**
   * Creates a Nabaztag instance and sends it a message.
   * @param message The message to send. 
   */
  private void notifyNabaztag(String message) {
    Nabaztag nabaztag = new Nabaztag("0013D3844E5A", "1202974551", logger);
    nabaztag.notify(message);
  }

  /**
   * @param args If provided, the location of the tickertape.xml file. 
   */
  public static void main(String[] args) {
    // Create the tickertape server.
    Server server = new Server();
    
    // Now get Hackystat Project Info.
    String sensorbase = "http://dasha.ics.hawaii.edu:9876/sensorbase";
    String dpd = "http://dasha.ics.hawaii.edu:9877/dailyprojectdata";
    String user = "johnson@hawaii.edu";
    String password = "foobar";
    String projectName = "Default";
    String projectOwner = "johnson@hawaii.edu";
    int interval = 10;
    HackystatProject project = 
      new HackystatProject(sensorbase, dpd, user, password, projectName, projectOwner, interval);
    
    // Notify the Nabaztag if there is project info. 
    String projectInfo = project.getInfo();
    if ((projectInfo != null) && (projectInfo.length() > 0)) {
      String message = String.format("Latest news for project %s during the past %s minutes. %s", 
          projectName, interval, projectInfo);
      server.notifyNabaztag(message);
    }
    
  }
}
