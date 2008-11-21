package org.hackystat.tickertape.tickerlingua;

/**
 * Supports Twitter notifications.
 * @author Philip Johnson
 */
public class TwitterAccount extends NotificationService {
  
  /**
   * Create the account.
   * @param id The unique ID for this account.
   * @param user The user name.
   * @param password Their password. 
   */
  public TwitterAccount(String id, String user, String password) {
    super(id, user, password);
  }
  
  /**
   * Create an account from the JAXB version. 
   * @param jaxb The JAXB account.
   */
  public TwitterAccount(org.hackystat.tickertape.tickerlingua.jaxb.TwitterAccount jaxb) {
    this(jaxb.getId(), jaxb.getUser(), jaxb.getPassword());
  }

}
