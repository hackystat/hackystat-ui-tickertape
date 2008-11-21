package org.hackystat.tickertape.tickerlingua;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.telemetry.service.client.TelemetryClient;

/**
 * Represents a Hackystat User. 
 * @author Philip Johnson
 */
public class HackystatUser {
  
  private String id;
  private String fullname;
  private String shortname;
  private String emailaccount;
  private HackystatService hackystatService;
  private String hackystatUserEmail;
  private String hackystatPassword;
  private TwitterAccount twitterAccount;
  private FacebookAccount facebookAccount;
  private String smsAccount;

  /**
   * Construct a new Hackystat User.  
   * Assumes all fields are valid.
   * @param id The id.
   * @param fullname The full name. 
   * @param shortname The nickname.
   * @param emailaccount The email address.
   * @param hackystatService Their HackystatService.
   * @param hackystatUserEmail The email address for their hackystat account.
   * @param hackystatPassword Their hackystat account password, or null.
   * @param twitterAccount Their twitter account, or null.
   * @param facebookAccount Their facebook account, or null.
   * @param smsAccount Their sms account, or null.
   */
  public HackystatUser(String id, String fullname, String shortname, String emailaccount, 
      HackystatService hackystatService, String hackystatUserEmail, String hackystatPassword,
      TwitterAccount twitterAccount, FacebookAccount
      facebookAccount, String smsAccount) {
    this.id = id;
    this.fullname = fullname;
    this.shortname = shortname;
    this.emailaccount = emailaccount;
    this.hackystatService = hackystatService;
    this.hackystatUserEmail = hackystatUserEmail;
    this.hackystatPassword = hackystatPassword;
    this.twitterAccount = twitterAccount;
    this.facebookAccount = facebookAccount;
    this.smsAccount = smsAccount;
  }

  /**
   * The unique id. 
   * @return The id.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * The full name for this user. 
   * @return The full name. 
   */
  public String getFullName() {
    return this.fullname;
  }
  
  /**
   * The nick name for this user. 
   * @return The nick name.
   */
  public String getShortName() {
    return this.shortname;
  }
  
  /**
   * This user's email account. 
   * @return Their email address.
   */
  public String getEmailAccount() {
    return this.emailaccount;
  }
  
  /**
   * This user's twitter account, or null if they don't have one.  
   * @return  The twitter account or null.
   */
  public TwitterAccount getTwitterAccount() {
    return this.twitterAccount;
  }
  
  /**
   * This user's facebook account, or null if they don't have one.  
   * @return  The facebook account or null.
   */
  public FacebookAccount getFacebookAccount() {
    return this.facebookAccount;
  }
  /**
   * This user's text message number, or null if they don't have one.  
   * @return  The sms account or null.
   */
  public String getSmsAccount() {
    return this.smsAccount;
  }
  
  /**
   * Creates a new SensorBaseClient for this user. 
   * @return A sensorbaseclient instance.
   */
  public SensorBaseClient getSensorBaseClient() {
    return new SensorBaseClient(this.hackystatService.getSensorbase(), this.hackystatUserEmail, 
        this.hackystatPassword);
  }
  
  /**
   * Creates a new DailyProjectDataClient for this user. 
   * @return A DPD client instance.
   */
  public DailyProjectDataClient getDailyProjectDataClient() {
    return new DailyProjectDataClient(this.hackystatService.getDailyProjectData(),
        this.hackystatUserEmail, this.hackystatPassword);
  }

  /**
   * Creates a new TelemetryClient for this user. 
   * @return A telemetryclient instance.
   */
  public TelemetryClient getTelemetryClient() {
    return new TelemetryClient(this.hackystatService.getTelemetry(),
        this.hackystatUserEmail, this.hackystatPassword);
  }
}

