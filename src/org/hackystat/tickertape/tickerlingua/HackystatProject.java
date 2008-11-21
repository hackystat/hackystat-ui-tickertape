package org.hackystat.tickertape.tickerlingua;

/**
 * Represents a Hackystat Project. 
 * @author Philip Johnson
 */
public class HackystatProject {
  
  private String id;
  private String name;
  private String shortname;
  private HackystatService service;
  private HackystatUser user;
  private String mailinglist;

  /**
   * Create a new Hackystat Project representation. 
   * @param id The id, which must be unique. 
   * @param name The name of this project. 
   * @param shortname A nickname.
   * @param service The HackystatService for this Project.
   * @param user The owner of this project. 
   * @param mailinglist The mailing list for this project. 
   */
  public HackystatProject(String id, String name, String shortname, 
      HackystatService service, HackystatUser user, String mailinglist) {
    this.id = id;
    this.name = name;
    this.shortname = shortname;
    this.service = service;
    this.user = user;
    this.mailinglist = mailinglist;
  }
  
  /**
   * Get the id. 
   * @return The id.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Get the name.
   * @return The name.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Get the shortname. 
   * @return The shortname.
   */
  public String getShortName() {
    return this.shortname;
  }
  
  /**
   * Get the Hackystat Service. 
   * @return The service. 
   */
  public HackystatService getHackystatService() {
    return this.service;
  }
  
  /**
   * Get the HackystatUser that owns this project. 
   * @return The user. 
   */
  public HackystatUser getHackystatUser() {
    return this.user;
  }

  /**
   * Get the mailing list for this project. 
   * @return The mailing list. 
   */
  public String getMailingList() {
    return this.mailinglist;
  }
}
