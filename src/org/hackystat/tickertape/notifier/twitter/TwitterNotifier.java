package org.hackystat.tickertape.notifier.twitter;

import java.util.logging.Logger;
import org.hackystat.utilities.stacktrace.StackTrace;
import twitter4j.TwitterException;

/**
 * Creates a Twitter notifier. 
 * @author Philip Johnson
 */
public class TwitterNotifier {

  /** The twitter user. */
  private String user;
  /** The logger for this notifier. */
  private Logger logger;
  /** The underlying twitter API. */
  private twitter4j.Twitter twitter4j;

  /**
   * Creates a Twitter notifier. 
   * @param user The user. 
   * @param password The password.
   * @param logger The logger. 
   */
  public TwitterNotifier(String user, String password, Logger logger) {
    this.user = user;
    this.logger = logger;
    this.twitter4j = new twitter4j.Twitter(user, password);
    this.twitter4j.setSource("Tickertape");

  }
 
  /**
   * Notify Twitter with the passed message.
   * @param message The message. 
   */
  public void notify(String message) {
    this.logger.info(String.format("Notifying Twitter user %s: %s", this.user, message));
    try {
      twitter4j.update(message);
    }
    catch (TwitterException e) {
      this.logger.warning("Tweet failed: " + StackTrace.toString(e));
    }    
  }
}
