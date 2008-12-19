package org.hackystat.tickertape.ticker;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.tickertape.notifier.nabaztag.NabaztagNotifier;
import org.hackystat.tickertape.ticker.data.MultiProjectSensorDataLog;
import org.hackystat.tickertape.ticker.data.ProjectSensorDataLog;
import org.hackystat.tickertape.tickerlingua.HackystatProject;
import org.hackystat.tickertape.tickerlingua.HackystatUser;
import org.hackystat.tickertape.tickerlingua.NotificationService;
import org.hackystat.tickertape.tickerlingua.TickerLingua;
import org.hackystat.tickertape.tickerlingua.Tickertape;

/**
 * Provides a ticker that monitors a set of Hackystat Projects and creates a "News Bulletin" 
 * once an hour on the hour that summarizes activity that is read by the Nabaztag Rabbit.  
 * @author Philip Johnson
 */
public class NabaztagNewsTicker implements Ticker {
  
  private Tickertape tickertape;
  private Logger logger;
  private MultiProjectSensorDataLog multiLog = new MultiProjectSensorDataLog();
  private NabaztagNotifier nabaztagNotifier;
  

  /**
   * The run method for this ticker. 
   * @param tickertape The Tickertape instance indicating what to do. 
   * @param tickerLingua The TickerLingua instance with global data info.
   * @param logger The logger to be used to communicate status. 
   */
  public void run(Tickertape tickertape, TickerLingua tickerLingua, Logger logger) {
    logger.info("Running NabaztagNewsTicker");
    this.tickertape = tickertape;
    this.logger = logger;
    notify(null); // Send a message to the rabbit indicating we're starting up. 
    
    // Update the MultiProjectSensorDataLog for every project we're interested in. 
    for (HackystatProject project : tickertape.getHackystatProjects()) {
      this.logger.info("\n\nChecking status of project " + project.getName());
      
      // Find or create the ProjectSensorDataLog.
      String projectName = project.getName();
      String projectOwner = project.getHackystatOwner().getHackystatUserAccount();
      HackystatUser authUser = project.getHackystatAuthUser();
      SensorBaseClient client = authUser.getSensorBaseClient();
      ProjectSensorDataLog log = multiLog.get(client, 1.0, projectOwner, projectName, logger);
      // Now get any new data.
      log.update();
    }
    
    // If there's any sensor data from the time interval, then make a report. 
    if (multiLog.hasSensorData()) {
      // Initially, simply report on who was working on what.
      StringBuffer buff = new StringBuffer();
      for (ProjectSensorDataLog log : multiLog) {
        if (log.hasSensorData()) {
          Set<String> workers = new HashSet<String>();
          for (String owner : log.getOwners()) {
            String fullName = tickerLingua.findFullName(owner);
            workers.add((fullName == null) ? owner : fullName);
          }
          
          for (String worker : workers) {
            buff.append(worker).append(" and ");
          }
          buff.append(" were working on project").append(log.getProjectName()).append(". ");
        }
      }
      
      // Now send the notification to the rabbit.
      notify(buff.toString());
    }
  }
  
  /**
   * Sends a message to the Rabbit associated with this ticker.
   * Sends a "startup" message to the rabbit the first time it is invoked. 
   * @param message The message to be sent. 
   */
  private void notify(String message) {
    if (this.nabaztagNotifier == null) {
      NotificationService notifier = tickertape.getNotificationServices().get(0);
      this.nabaztagNotifier = 
        new NabaztagNotifier(notifier.getId(), notifier.getPassword(), logger);
      this.nabaztagNotifier.notify("Starting up a new Nabaztag News Ticker.");
    }
    if (message != null) {
      this.nabaztagNotifier.notify(message);
    }
  }
}
