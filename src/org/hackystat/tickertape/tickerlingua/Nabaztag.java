package org.hackystat.tickertape.tickerlingua;

/**
 * Supports Nabaztag notifications.
 * 
 * Note that the serialnumber is the user, and the token is the password. 
 * @author Philip Johnson
 */
public class Nabaztag extends NotificationService {

  /**
   * Create the account.
   * @param id The unique ID for this account.
   * @param user The user name.
   * @param password Their password. 
   */
  public Nabaztag(String id, String user, String password) {
    super(id, user, password);
  }
  
  /**
   * Create an account from the JAXB version. 
   * @param jaxb The JAXB account.
   */
  public Nabaztag(org.hackystat.tickertape.tickerlingua.jaxb.Nabaztag jaxb) {
    this(jaxb.getId(), jaxb.getSerialnumber(), jaxb.getToken());
  }

}
