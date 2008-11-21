package org.hackystat.tickertape.tickerlingua;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;

/**
 * Provides simple test cases for the TickerLingua XML configuration language.
 * Uses the xml/tickerlingua.example.xml file in the current directory. 
 * @author Philip Johnson
 *
 */
public class TestTickerLingua {
  
  /**
   * Ensure that the example file can be read in and validated. 
   * @throws Exception If problems occur. 
   */
  @Test
  public void testTickerLingua() throws Exception {
    String exampleFile = System.getProperty("user.dir") + "/xml/tickerlingua.example.xml";
    // will throw an exception if can't read in the file. 
    TickerLingua tickerLingua = new TickerLingua(exampleFile);
    assertEquals("Checking service definitions",  1, tickerLingua.getServices().size());
    assertEquals("Checking user definitions",  2, tickerLingua.getUsers().size());
    assertEquals("Checking project definitions",  2, tickerLingua.getProjects().size());
    List<Tickertape> tickertapes = tickerLingua.getTickertapes();
    assertEquals("Checking tickertape defs", 7, tickertapes.size());
  }
}
