package org.hackystat.tickertape.server;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.hackystat.tickertape.configuration.ConfigurationManager;
import org.hackystat.tickertape.configuration.jaxb.Project;
import org.hackystat.tickertape.datasource.hackystat.HackystatProject;
import org.hackystat.tickertape.notifier.nabaztag.Nabaztag;
import org.hackystat.tickertape.notifier.twitter.Twitter;
import org.hackystat.utilities.logger.HackystatLogger;

/**
 * The main point of entry for this system. 
 * Reads in the tickertape.xml file, sets up the timer-based processes. 
 * @author Philip Johnson
 */
public class Server extends TimerTask {
  /** The logger for this service. */
  private Logger logger = HackystatLogger.getLogger("org.hackystat.tickertape", "tickertape");
  private ConfigurationManager configurationManager;
  
  /**
   * Constructs a new instance of a Tickertape server. 
   * Sends a log message to indicate the server is running. 
   * @param manager The configuration Manager, which encapsulates the tickertape.xml file. 
   */
  public Server (ConfigurationManager manager) {
    logger.info("Starting up tickertape server");
    this.configurationManager = manager;
  }
  
  /**
   * Gather data from Hackystat and send to Nabaztag. 
   */
  public void run() {
    logger.info("Waking up to check projects");
    int interval = this.configurationManager.getWakeupInterval();
    for (Project configProject : this.configurationManager.getProjects()) {
      
      // First, make a HackystatProject instance from the configuration information.
      String sensorbase = configProject.getHackystat().getSensorbaseHost();
      String dpd = configProject.getHackystat().getDailyProjectDataHost();
      String user = configProject.getHackystat().getUser();
      String password = configProject.getHackystat().getPassword();
      String projectName = configProject.getHackystat().getProjectName();
      String projectOwner = configProject.getHackystat().getProjectOwner();
      logger.info("Checking info for project: " + projectName);
      HackystatProject project = 
        new HackystatProject(sensorbase, dpd, user, password, projectName, projectOwner, interval);

      // Get info about what's happened during the recent interval of time.
      String projectInfo = project.getInfo();
      
      // Now, if there's some project info, do our notifications.
      if ((projectInfo != null) && (projectInfo.length() > 1)) {
        
        // Only notify Nabaztag if the user has specified Nabaztag as part of this project. 
        if (configProject.getNabaztag() != null) {
          String message = String.format("Latest news for project %s during the past %s minutes. %s", 
              projectName, interval, projectInfo.replace("@", "+at+").replace(".", "+dot+"));
          String serialNumber = configProject.getNabaztag().getSerialNumber();
          String token = configProject.getNabaztag().getToken();
          Nabaztag nabaztag = new Nabaztag(serialNumber, token, logger);
          nabaztag.notify(message);
        }
        
        // Only notify Twitter if the user has specified Twitter as part of this project.
        if (configProject.getTwitter() != null) {
          String message = String.format("Project %s (last %s mins.) %s", 
              projectName, interval, projectInfo);
          String twitterUser = configProject.getTwitter().getUser();
          String twitterPassword = configProject.getTwitter().getPassword();
          Twitter twitter = new Twitter (twitterUser, twitterPassword, logger);
          twitter.notify(message);
        }
      }
    }
    logger.info("Finished checking projects for this wakeup interval.");
  }
  
  /**
   * Logs the passed message. 
   * @param message The message. 
   */
  public void log(String message) {
    this.logger.info(message);
  }

  /**
   * Starts up this tickertape. 
   * @param args If provided, the location of the tickertape.xml file. 
   */
  public static void main(String[] args) {
    // Create the tickertape server.
    File dotTickerTape = new File(System.getProperty("user.home"), 
        ".hackystat/tickertape/tickertape.xml");
    File configFile = (args.length == 0) ? dotTickerTape : new File(args[0]);
    if (!configFile.exists()) {
      System.out.printf("%s not found. Exiting.", configFile);
      return;
    }
    ConfigurationManager configManager = new ConfigurationManager(configFile);
    int wakeupInterval = configManager.getWakeupInterval();
    Server server = new Server(configManager);

    server.log("Starting up timer-based execution every " + wakeupInterval + " minutes.");
    Timer timer = new Timer();
    timer.schedule(server, 0, 1000L * 60 * wakeupInterval); 
  }
}
