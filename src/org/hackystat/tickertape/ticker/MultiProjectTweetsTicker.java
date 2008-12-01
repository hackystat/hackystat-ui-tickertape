package org.hackystat.tickertape.ticker;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.tickertape.notifier.twitter.TwitterNotifier;
import org.hackystat.tickertape.tickerlingua.HackystatUser;
import org.hackystat.tickertape.tickerlingua.NotificationService;
import org.hackystat.tickertape.tickerlingua.TickerLingua;
import org.hackystat.tickertape.tickerlingua.Tickertape;
import org.hackystat.tickertape.tickerlingua.HackystatProject;
import org.hackystat.tickertape.ticker.data.ProjectSensorDataLog;

/**
 * A Ticker. 
 * @author Philip Johnson
 */
public class MultiProjectTweetsTicker implements Ticker {
  
  private TwitterNotifier twitterNotifier;
  private Tickertape tickertape;
  private Logger logger;
  private TickerLingua tickerLingua;
  private Map<String, ProjectSensorDataLog> project2log = 
    new HashMap<String, ProjectSensorDataLog>();


  /**
   * The run method for this ticker. 
   * @param tickertape The Tickertape instance indicating what to do. 
   * @param tickerLingua The TickerLingua instance with global data info.
   * @param logger The logger to be used to communicate status. 
   */
  public void run(Tickertape tickertape, TickerLingua tickerLingua, Logger logger) {
    logger.info("Running MultiProjectTweetsTicker");
    // Always do this every time. 
    this.tickertape = tickertape;
    this.logger = logger;
    this.tickerLingua = tickerLingua;
    notify(null);  // Force a tweet the first time we run this Ticker. 
    
    // Process each project.
    for (HackystatProject project : tickertape.getHackystatProjects()) {
      this.logger.info("Checking status of project " + project.getName());
      
      // Find or create the ProjectSensorDataLog.
      String projectName = project.getName();
      String projectOwner = project.getHackystatOwner().getHackystatUserAccount();
      HackystatUser authUser = project.getHackystatAuthUser();
      SensorBaseClient client = authUser.getSensorBaseClient();
      double maxLife = this.tickertape.getIntervalHours();
      ProjectSensorDataLog log = this.getProjectSensorDataLog(client, maxLife, projectOwner, 
          projectName, logger);
      
      // Now get any new data.
      log.update();
          
      for (String user : log.getProjectParticipants()) {
        if (log.hasRecentSensorData(user) && !log.hasRecentTweet(user)) {
          String mostWorkedFile = log.mostWorkedOnFile(user);
          if (mostWorkedFile != null) {
            String msg = 
              String.format("%s is working on %s files, including %s, in project %s.",
                  this.getShortName(user), log.getNumFilesWorkedOn(user),
                  mostWorkedFile, project.getShortName());
            notify(msg);
          }
        }
      }
    }
  }
  
  /**
   * Returns the shortname associated with this email, or the email if the shortname could not be 
   * found.
   * @param email The email corresponding to a Hackystat user account.
   * @return The short name or the email. 
   */
  private String getShortName(String email) {
    for (HackystatUser user : this.tickerLingua.getHackystatUsers()) {
      if (email.equals(user.getHackystatUserAccount())) {
        return user.getShortName();
      }
    }
    return email;
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
  private ProjectSensorDataLog getProjectSensorDataLog(SensorBaseClient client, double maxLife,
      String projectOwner, String projectName, Logger logger) {
    // If we can't find it, create it.
    if (!this.project2log.containsKey(projectName)) {
      ProjectSensorDataLog log = new ProjectSensorDataLog(client, maxLife, projectOwner,
          projectName, logger);
      this.project2log.put(projectName, log);
    }
    return this.project2log.get(projectName);    
  }
  

  /**
   * Sends a message to the Twitter account associated with this ticker, if the message is not null.
   * @param message The message to be sent. 
   */
  private void notify(String message) {
    if (this.twitterNotifier == null) {
      NotificationService notifier = tickertape.getNotificationServices().get(0);
      this.twitterNotifier = new TwitterNotifier(notifier.getId(), notifier.getPassword(), logger);
      this.twitterNotifier.notify("Starting up a new TwitterNotifier at: " + new Date());
    }
    if (message != null) {
      this.twitterNotifier.notify(message);
    }
  }
}
