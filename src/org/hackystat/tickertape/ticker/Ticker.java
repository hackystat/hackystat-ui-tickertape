package org.hackystat.tickertape.ticker;

import java.util.logging.Logger;

import org.hackystat.tickertape.tickerlingua.TickerLingua;
import org.hackystat.tickertape.tickerlingua.Tickertape;

/**
 * The interface that all Tickers must implement.
 * @author Philip Johnson
 *
 */
public interface Ticker {
  
  /**
   * Invoked each time the ticker task wakes up.
   * @param tickertape The tickertape indicating what to do.
   * @param tickerLingua The tickerlingua instance with global data. 
   * @param logger The logger.
   */
  public void run(Tickertape tickertape, TickerLingua tickerLingua, Logger logger);

}
