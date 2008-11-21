package org.hackystat.tickertape.tickerlingua;

/**
 * An abstract class representing all possible Notification services in Tickertape.
 * @author Philip Johnson
 */
public class NotificationService {
  
  private String id;
  private String user;
  private String password;

  /**
   * Creates a new notification service. 
   * @param id The unique ID. 
   * @param user The user name.
   * @param password Their password. 
   */
  public NotificationService(String id, String user, String password) {
    this.id = id;
    this.user = user;
    this.password = password;
  }
  
  /**
   * Return the id. 
   * @return The id. 
   */
  public String getId() {
    return this.id;
  }

  /**
   * Return the user. 
   * @return The user. 
   */
  public String getUser() {
    return this.user;
  }
  
  /**
   * Return the password.
   * @return The password.
   */
  public String getPassword() {
    return this.password;
  }

}
