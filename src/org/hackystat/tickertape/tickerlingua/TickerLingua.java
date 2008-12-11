package org.hackystat.tickertape.tickerlingua;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.tickertape.ticker.Ticker;
import org.hackystat.tickertape.tickerlingua.jaxb.Properties;
import org.hackystat.utilities.logger.HackystatLogger;

/**
 * Reads a TickerLingua definition file, validates it, and provides access to its components. 
 * <p>
 * A valid TickerLingua definition file is both syntactically and semantically valid. 
 * <p>
 * Syntactic validity means that it satisfies the XmlSchema definition for a TickerLingua file.
 * See the tickerlingua.definition.xsd file for details on syntactic validity.  This is assessed
 * by the JAXB parser when the file is read in. 
 * <p>
 * Semantic validity involves ensuring the following:
 * <ul>
 * <li> All HackystatAccountRef refid attributes name a defined HackystatAccount. 
 * <li> All HackystatProjectRef refid attributes name a defined HackystatProject.
 * <li> All HackystatUserRef refid attributes name a defined HackystatUser.
 * <li> All TwitterAccountRef refid attributes name a defined TwitterAccount. 
 * <li> All FacebookAccountRef refid attributes name a defined FacebookAccount. 
 * <li> All NabaztagRef refid attributes name a defined Nabaztag. 
 * <li> All SmsAccountRef refid attributes name a defined SmsAccount. 
 * <li> All hackystatservice-refid attributes name a defined HackystatService.
 * <li> All Ticker class attributes name a defined Java class implementing the Ticker interface. 
 * <li> All HackystatAccountRef refid attributes name a defined HackystatAccount. 
 * <li> All intervalhours attributes specify a double.
 * <li> All enabled attributes specify a boolean.
 * </ul>
 * <p>
 * In addition, if a HackystatProject refers to a sensorbase that cannot be contacted, or a 
 * user/password combination that is not legal, then a warning message is logged.
 * <p> 
 * If a tickerlingua.xml definition file cannot be found, or is not both syntactically and 
 * semantically valid, then a RuntimeException is thrown. 
 * 
 * @author Philip Johnson
 *
 */
public class TickerLingua {
  
  /** Holds the instance of the XML TickerLingua definition. */
  private org.hackystat.tickertape.tickerlingua.jaxb.TickerLingua jaxbTickerLingua;
  
  // Here's where we collect all the objects. 
  private Map<String, HackystatService> services = new HashMap<String, HackystatService>();
  private Map<String, HackystatUser> users = new HashMap<String, HackystatUser>();
  private Map<String, HackystatProject> projects = new HashMap<String, HackystatProject>();
  private Map<String, Nabaztag> nabaztags = new HashMap<String, Nabaztag>();
  private Map<String, TwitterAccount> twitters = new HashMap<String, TwitterAccount>();
  private Map<String, FacebookAccount> facebooks = new HashMap<String, FacebookAccount>();
  private Map<String, Tickertape> tickertapes = new HashMap<String, Tickertape>();

  private Logger logger = HackystatLogger.getLogger("TickerLinguaLogger", "tickertape", true);
  
  private String smtpServer = null;
  private String loggingLevel = "INFO";
  
  /**
   * Creates a TickerLingua instance from default location ~/.hackystat/tickertape/tickertape.xml.
   * @throws TickerLinguaException If problems occur while getting or processing the file. 
   */
  public TickerLingua() throws TickerLinguaException {
    this(System.getProperty("user.home") + "/.hackystat/tickertape/tickertape.xml"); 
  }
  
  /**
   * Creates a TickerLingua instance from the passed file.
   * @param filePath A path to the file. 
   * @throws TickerLinguaException If problems occur while getting or processing the file. 
   */
  public TickerLingua(String filePath) throws TickerLinguaException {
    File definitionFile = new File(filePath);
    
    // Return if we can't find the passed file.
    if (!definitionFile.exists()) {
      throw new TickerLinguaException("Tickertape definition file missing: " + filePath);
    }
    
    // Now initialize our jaxbTickerLingua instance variable with the contents of the XML file. 
    try {
      JAXBContext jaxbContext = JAXBContext
      .newInstance(org.hackystat.tickertape.tickerlingua.jaxb.ObjectFactory.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      this.jaxbTickerLingua =  (org.hackystat.tickertape.tickerlingua.jaxb.TickerLingua) 
      unmarshaller.unmarshal(definitionFile);
    } 
    catch (Exception e) {
      throw new TickerLinguaException("Tickertape definition file invalid: " + filePath, e);
    }
    
    // Now start processing the JAXB instances and creating our validated entities. 
    // This means (a) checking that certain strings are valid, and (b) resolving "refids".
    // Order is important in the following. Need to go bottom up. 
    processHackystatServices();
    processNabaztags();
    processFacebookAccounts();
    processTwitterAccounts();
    processHackystatUsers();
    processHackystatProjects();
    processTickertapes();
    processGlobals();
    
  }
  
  
  /**
   * Creates instances of all HackystatServices.
   * Logs a warning if the services cannot be reached. 
   * @throws TickerLinguaException If duplicate ID encountered.
   */
  private void processHackystatServices() throws TickerLinguaException {
    for (org.hackystat.tickertape.tickerlingua.jaxb.HackystatService jaxb :
      this.jaxbTickerLingua.getHackystatServices().getHackystatService()) {
      String id = jaxb.getId();
      if (this.services.containsKey(id)) {
        throw new TickerLinguaException("Duplicate HackystatService id: " + id);
      }
      HackystatService service = new HackystatService(jaxb);
      if (!SensorBaseClient.isHost(service.getSensorbase())) {
        logger.warning("Sensorbase not found: " + service.getSensorbase());
      }
      if (!DailyProjectDataClient.isHost(service.getDailyProjectData())) {
        logger.warning("DailyProjectData service not found: " + service.getDailyProjectData());
      }
      if (!TelemetryClient.isHost(service.getTelemetry())) {
        logger.warning("Telemetry service not found: " + service.getTelemetry());
      }
      this.services.put(id, new HackystatService(jaxb));
    }
  }
  
  /**
   * Processes the Globals section of the Tickertape definition.
   */
  private void processGlobals() {
    try {
      if (this.jaxbTickerLingua.getGlobals() == null) {
        return;
      }
      if (this.jaxbTickerLingua.getGlobals().getMail() != null) {
        this.smtpServer = this.jaxbTickerLingua.getGlobals().getMail().getSmtpServer();
      }
      if (this.jaxbTickerLingua.getGlobals().getLoggingLevel() != null) {
        this.loggingLevel = this.jaxbTickerLingua.getGlobals().getLoggingLevel().getLevel();
      }
    }
    catch (Exception e) {
      this.logger.warning("Errors processing Globals section: " + e.getMessage());
    }
  }
  
  /**
   * Returns the smtp server specified in the Globals section, or null if none specified.
   * @return The smtp server, or null.
   */
  public String getSmtpServer() {
    return this.smtpServer;
  }


  /**
   * Returns the logging level specified in the globals section, or "INFO" if none specified.
   * @return The logging level, or "INFO" if not specified.
   */
  public String getLoggingLevel () {
    return this.loggingLevel;
  }
  
  
  /**
   * Defines Nabaztag instances from the JAXB.
   * @throws TickerLinguaException If a duplicate ID is found.
   */
  private void processNabaztags() throws TickerLinguaException {
    if (this.jaxbTickerLingua.getNabaztags() == null) {
      return;
    }
    for (org.hackystat.tickertape.tickerlingua.jaxb.Nabaztag jaxb :
      this.jaxbTickerLingua.getNabaztags().getNabaztag()) {
      String id = jaxb.getId();
      if (this.nabaztags.containsKey(id)) {
        throw new TickerLinguaException("Duplicate Nabaztag id: " + id);
      }
      this.nabaztags.put(id, new Nabaztag(jaxb));
    }
  }
  
  /**
   * Defines FacebookAccounts from the JAXB.
   * @throws TickerLinguaException If a duplicate ID is found.
   */
  private void processFacebookAccounts() throws TickerLinguaException {
    if (this.jaxbTickerLingua.getFacebookAccounts() == null) {
      return;
    }
    for (org.hackystat.tickertape.tickerlingua.jaxb.FacebookAccount jaxb :
      this.jaxbTickerLingua.getFacebookAccounts().getFacebookAccount()) {
      String id = jaxb.getId();
      if (this.facebooks.containsKey(id)) {
        throw new TickerLinguaException("Duplicate Facebook id: " + id);
      }
      this.facebooks.put(id, new FacebookAccount(jaxb));
    }
  }
  
  /**
   * Defines TwitterAccounts from the JAXB.
   * @throws TickerLinguaException If a duplicate ID is found.
   */
  private void processTwitterAccounts() throws TickerLinguaException {
    for (org.hackystat.tickertape.tickerlingua.jaxb.TwitterAccount jaxb :
      this.jaxbTickerLingua.getTwitterAccounts().getTwitterAccount()) {
      String id = jaxb.getId();
      if (this.twitters.containsKey(id)) {
        throw new TickerLinguaException("Duplicate Twitter id: " + id);
      }
      this.twitters.put(id, new TwitterAccount(jaxb));
    }
  }
  
  /**
   * Defines HackystatUser instances from the JAXB.
   * Logs warnings if the sensorbase cannot be reached or the user is not registered. 
   * 
   * @throws TickerLinguaException If there is a duplicate ID, or if the references
   * to the hackystat service, twitter, or facebook cannot be resolved. 
   */
  private void processHackystatUsers() throws TickerLinguaException {
    // Process HackystatUsers. Validate and resolve Service, Twitter, and Facebook references.
    for (org.hackystat.tickertape.tickerlingua.jaxb.HackystatUser jaxb :
      this.jaxbTickerLingua.getHackystatUsers().getHackystatUser()) {
      String id = jaxb.getId();
      if (this.users.containsKey(id)) {
        throw new TickerLinguaException("Duplicate HackystatUser id: " + id);
      }
      // Validate and resolve HackystatServiceRef
      String serviceId = jaxb.getHackystatAccount().getHackystatserviceRefid();
      HackystatService service = this.services.get(serviceId);
      if (service == null) {
        throw new TickerLinguaException("Invalid hackystatservice-refid: " + serviceId);
      }
      
      // Check to see if user/password/service are OK.
      String sensorbase = service.getSensorbase();
      String hackystatUser = jaxb.getHackystatAccount().getUser();
      String hackystatPassword = jaxb.getHackystatAccount().getPassword();
      if (!SensorBaseClient.isHost(sensorbase)) {
        this.logger.warning("Warning: Sensorbase not found: " + sensorbase);
      }
      if ((hackystatPassword != null) && 
          (!SensorBaseClient.isRegistered(sensorbase, hackystatUser, hackystatPassword))) {
        this.logger.warning("Warning: Hackystat credentials not OK: " + hackystatUser);
      }
      
      // Validate and resolve TwitterAccountRef (if any).
      // Either twitterAccount will be null or will contain a valid TwitterAccount instance. 
      String twitterId = null;
      TwitterAccount twitterAccount = null;
      if (jaxb.getTwitterAccountRef() != null) {
        twitterId = jaxb.getTwitterAccountRef().getRefid();
      }
      if (twitterId != null) {
        twitterAccount = this.twitters.get(twitterId);
        if (twitterAccount == null) {
          throw new TickerLinguaException("Invalid TwitterAccountRef: " + twitterId);
        }
      }
      // Validate and resolve FacebookAccountRef (if any).
      // Either facebookAccount will be null or will contain a valid FacebookAccount instance. 
      String facebookId = null;
      FacebookAccount facebookAccount = null;
      if (jaxb.getFacebookAccountRef() != null) {
        facebookId = jaxb.getFacebookAccountRef().getRefid();
      }
      if (facebookId != null) {
        facebookAccount = this.facebooks.get(facebookId);
        if (facebookAccount == null) {
          throw new TickerLinguaException("Invalid FacebookAccountRef: " + facebookId);
        }
      }
      String smsNumber = null;
      if (jaxb.getSmsAccount() != null) {
        smsNumber = jaxb.getSmsAccount().getNumber();
      }
      
      String emailAccount = null;
      if (jaxb.getEmailAccount() != null) {
        emailAccount = jaxb.getEmailAccount().getAccount();
      }
      // References are resolved, everything is OK, so define this user. 
      HackystatUser user = new HackystatUser(id, jaxb.getFullname(), jaxb.getShortname(), 
          emailAccount, service, jaxb.getHackystatAccount().getUser(),
          jaxb.getHackystatAccount().getPassword(), twitterAccount, facebookAccount, 
          smsNumber);
      // If that was successful, then add it to the users list. 
      this.users.put(id, user);
    }
  }
  
  /**
   * Defines a new HackystatProject. 
   * @throws TickerLinguaException If the id is a duplicate, or if the references to 
   * the HackystatService or HackystatUser cannot be resolved. 
   */
  private void processHackystatProjects() throws TickerLinguaException {
    // Process HackystatProjects. Validate and resolve service and user references. 
    for (org.hackystat.tickertape.tickerlingua.jaxb.HackystatProject jaxb :
      this.jaxbTickerLingua.getHackystatProjects().getHackystatProject()) {
      String id = jaxb.getId();
      if (this.projects.containsKey(id)) {
        throw new TickerLinguaException("Duplicate HackystatProject id: " + id);
      }
      String serviceId = jaxb.getHackystatserviceRefid();
      HackystatService service = this.services.get(serviceId);
      if (service == null) {
        throw new TickerLinguaException("Invalid HackystatService-refid: " + serviceId);
      }
      String ownerId = jaxb.getProjectownerRefid();
      HackystatUser owner = this.users.get(ownerId);
      if (owner == null) {
        throw new TickerLinguaException("Invalid ProjectOwner-refid: " + ownerId);
      }
      String authUserId = jaxb.getAuthuserRefid();
      HackystatUser authUser = this.users.get(authUserId);
      if (authUser == null) {
        throw new TickerLinguaException("Invalid AuthUser-refid: " + authUserId);
      }
      if (!authUser.hasPassword()) {
        throw new TickerLinguaException("AuthUser must have a password: " + authUserId);
      }
      
      // Should be OK, so create it.
      HackystatProject project = new HackystatProject(id, jaxb.getName(), jaxb.getShortname(),
          service, owner, authUser, jaxb.getMailinglist());
      this.projects.put(id, project);
    }
  }
  
  /**
   * Defines the Tickertape instances from the JAXB.
   * 
   * @throws TickerLinguaException If the id is a duplicate, or if the interval hours is not a
   * double, or if enabled is not a boolean, or if a reference to a HackystatProject, HackystatUser,
   * TwitterAccount, FacebookAccount, or Nabaztag cannot be resolved, or if the Ticker class
   * cannot be found.
   */
  private void processTickertapes() throws TickerLinguaException {
    // Process HackystatProjects. Validate and resolve service and user references. 
    for (org.hackystat.tickertape.tickerlingua.jaxb.Tickertape jaxb :
      this.jaxbTickerLingua.getTickertapes().getTickertape()) {
      String id = jaxb.getId();
      if (this.tickertapes.containsKey(id)) {
        throw new TickerLinguaException("Duplicate Tickertape id: " + id);
      }
      double intervalHours;
      try {
        intervalHours = Double.parseDouble(jaxb.getIntervalhours());
      }
      catch (Exception e) {
        throw new TickerLinguaException("Invalid intervalhours: " + jaxb.getIntervalhours(), e);
      }
      boolean enabled;
      try {
        enabled = Boolean.parseBoolean(jaxb.getEnabled());
      }
      catch (Exception e) {
        throw new TickerLinguaException("Invalid enabled: " + jaxb.getEnabled(), e);
      }
      // Create a list of all HackystatProjects listed.
      List<HackystatProject> projects = new ArrayList<HackystatProject>();
      for (org.hackystat.tickertape.tickerlingua.jaxb.HackystatProjectRef projectRef : 
        jaxb.getHackystatProjectRef()) {
        String refId = projectRef.getRefid();
        if (!this.projects.containsKey(refId)) {
          throw new TickerLinguaException("Invalid HackystatProject refid: " + refId);
        }
        projects.add(this.projects.get(refId));
      }
      
      // Create a list of all HackystatUsers listed.
      List<HackystatUser> users = new ArrayList<HackystatUser>();
      for (org.hackystat.tickertape.tickerlingua.jaxb.HackystatUserRef userRef : 
        jaxb.getHackystatUserRef()) {
        String refId = userRef.getRefid();
        if (!this.users.containsKey(refId)) {
          throw new TickerLinguaException("Invalid HackystatUser refid: " + refId);
        }
        users.add(this.users.get(refId));
      }
      
      // Create the notification services. 
      List<NotificationService> services = new ArrayList<NotificationService>();
      for (org.hackystat.tickertape.tickerlingua.jaxb.TwitterAccountRef ref : 
        jaxb.getTwitterAccountRef()) {
        String refId = ref.getRefid();
        if (!this.twitters.containsKey(refId)) {
          throw new TickerLinguaException("Invalid TwitterAccount refid: " + refId);
        }
        services.add(this.twitters.get(refId));
      }
      for (org.hackystat.tickertape.tickerlingua.jaxb.FacebookAccountRef ref : 
        jaxb.getFacebookAccountRef()) {
        String refId = ref.getRefid();
        if (!this.facebooks.containsKey(refId)) {
          throw new TickerLinguaException("Invalid FacebookAccount refid: " + refId);
        }
        services.add(this.facebooks.get(refId));
      }
      for (org.hackystat.tickertape.tickerlingua.jaxb.NabaztagRef ref : 
        jaxb.getNabaztagRef()) {
        String refId = ref.getRefid();
        if (!this.nabaztags.containsKey(refId)) {
          throw new TickerLinguaException("Invalid Nabaztag refid: " + refId);
        }
        services.add(this.nabaztags.get(refId));
      }
      
      // Check the Ticker class.
      String className = jaxb.getTicker().getClazz();
      Class<? extends Ticker> tickerClass;
      try {
        Class<?> c = Class.forName(className);
        tickerClass = c.asSubclass(Ticker.class);
        // Make an instance now to ensure it's OK. If problems will throw an exception.
        Constructor<? extends Ticker> ctor = tickerClass.getConstructor();
        ctor.newInstance();
      }
      catch (Exception e) {
        throw new TickerLinguaException("Problem defining ticker class. " + className, e);
      }
      
      // Pass in a ticker properties instance.  
      Properties properties = new Properties();
      if ((jaxb.getTicker() != null) && (jaxb.getTicker().getProperties() != null)) {
        properties = jaxb.getTicker().getProperties();
      }
      
      Tickertape tickertape = new Tickertape(id, intervalHours, enabled, jaxb.getStarttime(),
          jaxb.getDescription(), projects, services, tickerClass, properties);
      this.tickertapes.put(id, tickertape);
    }
  }

  /**
   * Return the services.
   * Package-private for testing. 
   * @return The services. 
   */
  Map<String, HackystatService> getServices() {
    return this.services;
  }
  
  /**
   * Return the services.
   * Package-private for testing. 
   * @return The services. 
   */
  Map<String, HackystatUser> getUsers() {
    return this.users;
  }
  
  /**
   * Return the services.
   * Package-private for testing. 
   * @return The services. 
   */
  Map<String, HackystatProject> getProjects() {
    return this.projects;
  }
  
  /**
   * Return the Tickertape instance with the specified ID, or null if not found.
   * @param id The id. 
   * @return The Tickertape instance, or null.
   */
  public Tickertape getTickertape(String id) {
    return this.tickertapes.get(id);
  }
  
  /**
   * Return a list of all Tickertape instances. 
   * @return The list of Tickertape instances. 
   */
  public List<Tickertape> getTickertapes () {
    List<Tickertape> tickertapeList = new ArrayList<Tickertape>();
    tickertapeList.addAll(this.tickertapes.values());
    return tickertapeList;
  }
  
  /**
   * Returns the set of defined HackystatUsers. 
   * @return The hackystat user definitions. 
   */
  public Collection<HackystatUser> getHackystatUsers() {
    return this.users.values();
  }
  
}
