package org.hackystat.tickertape.tickerlingua;

import org.junit.Test;

public class TestTickerLingua {
  
  @Test
  public void testTickerLingua() throws Exception {
    String exampleFile = System.getProperty("user.dir") + "/xml/tickerlingua.example.xml";
    // will throw an exception if can't read in the file. 
    TickerLingua tickerLingua = new TickerLingua(exampleFile);
  }

}
