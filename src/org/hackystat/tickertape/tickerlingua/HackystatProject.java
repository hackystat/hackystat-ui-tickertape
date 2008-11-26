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
  private HackystatUser owner;
  private String mailinglist;
  private HackystatUser authUser;

  /**
   * Create a new Hackystat Project representation. 
   * @param id The id, which must be unique. 
   * @param name The name of this project. 
   * @param shortname A nickname.
   * @param service The HackystatService for this Project.
   * @param owner The owner of this project. We don't need a password for this user.  
   * @param authUser The authorized user for accessing this project. We need the password for them.
   * @param mailinglist The mailing list for this project. 
   */
  public HackystatProject(String id, String name, String shortname, 
      HackystatService service, HackystatUser owner, HackystatUser authUser, String mailinglist) {
    this.id = id;
    this.name = name;
    this.shortname = shortname;
    this.service = service;
    this.owner = owner;
    this.authUser = authUser;
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
   * @return The owner. 
   */
  public HackystatUser getHackystatOwner() {
    return this.owner;
  }
  
  /**
   * Get the HackystatUser that can access this project data. 
   * @return The authorized user of this project data. 
   */
  public HackystatUser getHackystatAuthUser() {
    return this.authUser;
  }

  /**
   * Get the mailing list for this project. 
   * @return The mailing list. 
   */
  public String getMailingList() {
    return this.mailinglist;
  }
}
