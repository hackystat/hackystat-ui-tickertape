package org.hackystat.tickertape.ticker;

import java.util.logging.Logger;

import org.hackystat.tickertape.tickerlingua.TickerLingua;
import org.hackystat.tickertape.tickerlingua.Tickertape;

/**
 * A Ticker. 
 * @author Philip Johnson
 */
public class PortfolioTransitionTicker implements Ticker {
  
  /**
   * The run method for this ticker. 
   * @param tickertape The Tickertape instance indicating what to do. 
   * @param tickerLingua The TickerLingua instance with global data info.
   * @param logger The logger to be used to communicate status. 
   */
  @Override
  public void run(Tickertape tickertape, TickerLingua tickerLingua, Logger logger) {
    logger.info("Running PortfolioTransitionTicker");
  }

}
