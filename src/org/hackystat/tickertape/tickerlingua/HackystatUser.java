package org.hackystat.tickertape.tickerlingua;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.telemetry.service.client.TelemetryClient;

public class HackystatUser {
  
  private String id;
  private String fullname;
  private String shortname;
  private String emailaccount;
  private HackystatService hackystatService;
  private String hackystatUser;
  private String hackystatPassword;
  private TwitterAccount twitterAccount;
  private FacebookAccount facebookAccount;
  private String smsAccount;
  
  public HackystatUser(String id, String fullname, String shortname, String emailaccount, 
      HackystatService hackystatService, String hackystatUser, String hackystatPassword,
      TwitterAccount twitterAccount, FacebookAccount
      facebookAccount, String smsAccount) {
    this.id = id;
    this.fullname = fullname;
    this.shortname = shortname;
    this.emailaccount = emailaccount;
    this.hackystatService = hackystatService;
    this.hackystatUser = hackystatUser;
    this.hackystatPassword = hackystatPassword;
    this.twitterAccount = twitterAccount;
    this.facebookAccount = facebookAccount;
    this.smsAccount = smsAccount;
  }

  public String getId() {
    return this.id;
  }
  
  public String getFullName() {
    return this.fullname;
  }
  
  public String getShortName() {
    return this.shortname;
  }
  
  public String getEmailAccount() {
    return this.emailaccount;
  }
  
  public TwitterAccount getTwitterAccount() {
    return this.twitterAccount;
  }
  
  public FacebookAccount getFacebookAccount() {
    return this.facebookAccount;
  }
  
  public String getSmsAccount() {
    return this.smsAccount;
  }
  
  public SensorBaseClient getSensorBaseClient() {
    return new SensorBaseClient(this.hackystatService.getSensorbase(), this.hackystatUser, 
        this.hackystatPassword);
  }
  
  public DailyProjectDataClient getDailyProjectDataClient() {
    return new DailyProjectDataClient(this.hackystatService.getDailyProjectData(),
        this.hackystatUser, this.hackystatPassword);
  }

  public TelemetryClient getTelemetryClient() {
    return new TelemetryClient(this.hackystatService.getTelemetry(),
        this.hackystatUser, this.hackystatPassword);
  }
}

