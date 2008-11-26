package org.hackystat.tickertape.server;

import java.util.TimerTask;
import java.util.logging.Logger;

import org.hackystat.tickertape.ticker.Ticker;
import org.hackystat.tickertape.tickerlingua.TickerLingua;
import org.hackystat.tickertape.tickerlingua.Tickertape;

/**
 * A TimerTask that supports the periodic running of a Tickertape instance.
 * @author Philip Johnson
 */
public class TickertapeTask extends TimerTask {
  
  /** Holds the tickertape instance associated with this timer task. */
  private Tickertape tickertape;
  /** The logger that will record information about tickertape execution. */
  private Logger logger;
  
  private TickerLingua tickerLingua;

  /**
   * Creates a new task that will execute the associated tickertape processing. 
   * @param tickertape The tickertape definition. 
   * @param tickerLingua The tickerLingua definition.
   * @param logger The logger. 
   */
  public TickertapeTask(Tickertape tickertape, TickerLingua tickerLingua, Logger logger) {
    this.tickertape = tickertape;
    this.logger = logger;
    this.tickerLingua = tickerLingua;
  }

  /**
   * When this task wakes up, it will invoke the Ticker from this Tickertape instance. 
   */
  @Override
  public void run() {
    this.logger.info("Running tickertape: " + tickertape.getId());
    Ticker ticker = this.tickertape.getTicker();
    ticker.run(tickertape, tickerLingua, logger);
  }
}
