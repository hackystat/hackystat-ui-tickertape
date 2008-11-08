package org.hackystat.tickertape.configuration;

import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.hackystat.tickertape.configuration.jaxb.Project;
import org.hackystat.tickertape.configuration.jaxb.Tickertape;
import org.hackystat.utilities.stacktrace.StackTrace;

/**
 * Encapsulates the tickertape.xml file. 
 * @author Philip Johnson
 */
public class ConfigurationManager {
  /** The tickertape configuration instance. */
  private Tickertape tickertape;

  /**
   * Creates a new instance that encapsulates the passed tickertape.xml file. 
   * @param configFile A tickertape.xml file. 
   */
  public ConfigurationManager(File configFile) {
    try {
      JAXBContext jaxbContext = 
        JAXBContext.newInstance(
            org.hackystat.tickertape.configuration.jaxb.ObjectFactory.class);
      if (configFile.exists()) {
        System.out.println("Loading configuration from " + configFile.getPath()); 
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        this.tickertape = (Tickertape) unmarshaller.unmarshal(configFile);
      }
    }
    catch (Exception e) {
      System.out.println("Error during Tickertape configuration: " + StackTrace.toString(e));
      throw new RuntimeException("Error in Tickertape configuration", e);
    }
  }
  
  /**
   * Returns the list of Project instances in this tickertape configuration. 
   * @return The list of projects. 
   */
  public List<Project> getProjects() {
    return this.tickertape.getProject();
  }
  
  /**
   * Returns the wakeup interval for this tickertape. 
   * @return The wakeup interval. 
   */
  public int getWakeupInterval() {
    return this.tickertape.getWakeupInterval();
  }

}
