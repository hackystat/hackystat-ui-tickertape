package org.hackystat.tickertape.tickerlingua;

public abstract class NotificationService {
  
  private String id;
  private String user;
  private String password;
  
  public NotificationService(String id, String user, String password) {
    this.id = id;
    this.user = user;
    this.password = password;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public String getPassword() {
    return this.password;
  }

}
