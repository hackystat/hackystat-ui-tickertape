package org.hackystat.tickertape.tickerlingua;

/**
 * Supports Facebook notification.
 * @author Philip Johnson
 */
public class FacebookAccount extends NotificationService {

  /**
   * Create the account.
   * @param id The unique ID for this account.
   * @param user The user name.
   * @param password Their password. 
   */
  public FacebookAccount(String id, String user, String password) {
    super(id, user, password);
  }
  
  /**
   * Create an account from the JAXB version. 
   * @param jaxb The JAXB account.
   */
  public FacebookAccount(org.hackystat.tickertape.tickerlingua.jaxb.FacebookAccount jaxb) {
    this(jaxb.getId(), jaxb.getUser(), jaxb.getPassword());
  }
}
