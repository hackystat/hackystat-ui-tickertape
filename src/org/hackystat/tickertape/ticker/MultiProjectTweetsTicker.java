package org.hackystat.tickertape.ticker;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataIndex;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataRef;
import org.hackystat.tickertape.notifier.twitter.TwitterNotifier;
import org.hackystat.tickertape.tickerlingua.HackystatUser;
import org.hackystat.tickertape.tickerlingua.NotificationService;
import org.hackystat.tickertape.tickerlingua.TickerLingua;
import org.hackystat.tickertape.tickerlingua.Tickertape;
import org.hackystat.tickertape.tickerlingua.HackystatProject;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * A Ticker. 
 * @author Philip Johnson
 */
public class MultiProjectTweetsTicker implements Ticker {
  
  private TwitterNotifier twitterNotifier;
  private Tickertape tickertape;
  private Logger logger;
  private TickerLingua tickerLingua;


  /**
   * The run method for this ticker. 
   * @param tickertape The Tickertape instance indicating what to do. 
   * @param tickerLingua The TickerLingua instance with global data info.
   * @param logger The logger to be used to communicate status. 
   */
  @Override
  public void run(Tickertape tickertape, TickerLingua tickerLingua, Logger logger) {
    logger.info("Running MultiProjectTweetsTicker");
    // Always do this every time. 
    this.tickertape = tickertape;
    this.logger = logger;
    this.tickerLingua = tickerLingua;
    notify(null);  // Force a tweet the first time we run this Ticker. 
    
    // We will send one tweet per project.
    for (HackystatProject project : tickertape.getHackystatProjects()) {
      this.logger.info("Checking status of project " + project.getName());
      HackystatUser authUser = project.getHackystatAuthUser();
      HackystatUser owner = project.getHackystatOwner();
      SensorBaseClient client = authUser.getSensorBaseClient();
      XMLGregorianCalendar endTime = Tstamp.makeTimestamp();
      int interval = (int) (tickertape.getIntervalHours() * 60 * 60);
      XMLGregorianCalendar startTime = Tstamp.incrementSeconds(endTime, (interval * -1));
      SensorDataIndex index = null;
      try {
        index = client.getProjectSensorData(owner.getHackystatUserAccount(),
            project.getName(), startTime, endTime);
      }
      catch (Exception e) {
        this.logger.warning("Project Sensor Data request failed: " + e.getMessage());
      }
      // If there is some sensor data.
      if ((index != null) && (!index.getSensorDataRef().isEmpty())) {
        Set<String> users = new HashSet<String>();
        for (SensorDataRef ref : index.getSensorDataRef()) {
          users.add(ref.getOwner());
        }
        StringBuffer buff = new StringBuffer(23);
        buff.append("For project ").append(project.getShortName()).append(", ");
        for (String email : users) {
          buff.append(getShortName(email)).append(", ");
        }
        // get rid of the last two characters (the ', ')
        buff.delete(buff.length() - 2, buff.length());
        buff.append(" are currently working.");
        notify(buff.toString());
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
