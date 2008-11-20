package org.hackystat.tickertape.tickerlingua;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Reads a TickerLingua definition file, validates it, and provides access to its components. 
 * <p>
 * A valid TickerLinguq definition file is both syntactically and semantically valid. 
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
 * If a tickerlingua.xml definition file cannot be found, or is not both syntactically and 
 * semantically valid, then a RuntimeException is thrown. 
 * 
 * @author Philip Johnson
 *
 */
public class TickerLingua {
  
  /** Holds the instance of the XML TickerLingua definition. */
  private org.hackystat.tickertape.tickerlingua.jaxb.TickerLingua jaxbTickerLingua;
  
  /**
   * Creates a TickerLingua instance from default location ~/.hackystat/tickertape/tickertape.xml.
   */
  public TickerLingua() {
    this(System.getProperty("user.home") + "/.hackystat/tickertape/tickertape.xml"); 
  }
  
  /**
   * Creates a TickerLingua instance from the passed file.
   * Throws a RuntimeException if the file cannot be found or is not a valid TickerLingua file.  
   * @param filePath A path to the file. 
   */
  public TickerLingua(String filePath) {
    File definitionFile = new File(filePath);
    
    // Return if we can't find the passed file.
    if (!definitionFile.exists()) {
      throw new RuntimeException("Tickertape definition file missing: " + filePath);
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
      throw new RuntimeException("Tickertape definition file invalid: " + filePath, e);
    }
    validate(this.jaxbTickerLingua);
  }
  

  /**
   * Validates that the passed JAXB TickerLingua definition is valid, and throws a 
   * RuntimeException if it is not. 
   * See the class-level JavaDoc for the definition of a valid TickerLingua definition. 
   *   
   * @param definition The TickerLingua definition. 
   */
  private void validate(org.hackystat.tickertape.tickerlingua.jaxb.TickerLingua definition) {
    
    
    
  }

}
