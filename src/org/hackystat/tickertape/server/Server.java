package org.hackystat.tickertape.server;

import java.io.File;
import java.util.Timer;
import java.util.logging.Logger;
import org.hackystat.tickertape.tickerlingua.TickerLingua;
import org.hackystat.tickertape.tickerlingua.Tickertape;
import org.hackystat.utilities.logger.HackystatLogger;

/**
 * The main point of entry for this system. 
 * Reads in the tickertape.xml file, sets up the timer-based processes. 
 * @author Philip Johnson
 */
public class Server {
  /** The logger for this service. */
  private static Logger logger = 
    HackystatLogger.getLogger("org.hackystat.tickertape", "tickertape");
  
  /**
   * Starts up this tickertape instance. 
   * Reads in the tickerlingua file and sets up timer tasks for each defined and enabled tickertape.
   * Looks in ~/.hackystat/tickertape/tickerlingua.xml by default. 
   * @param args If provided, the location of the tickerlingua.xml file. 
   * @throws Exception if problems occur.
   */
  public static void main(String[] args) throws Exception {
    // Create the tickertape server.
    File dotTickerTape = new File(System.getProperty("user.home"), 
        ".hackystat/tickertape/tickertape.xml");
    File configFile = (args.length == 0) ? dotTickerTape : new File(args[0]);
    if (!configFile.exists()) {
      System.out.printf("%s not found. Exiting.", configFile);
      return;
    }
    System.out.println("Configuring system from: " + configFile.getAbsolutePath());
    TickerLingua tickerLingua = new TickerLingua(configFile.getAbsolutePath());
    // Start up all enabled tickertapes. 
    for (Tickertape tickertape : tickerLingua.getTickertapes()) {
      // Sleep for a few seconds between startups.  
      String id = tickertape.getId();
      if (tickertape.enabled()) {
        Timer timer = new Timer();
        double wakeupIntervalHours = tickertape.getIntervalHours();
        logger.info(String.format("Starting tickertape %s to wakeup every %s hours.", id, 
            wakeupIntervalHours)); 
        timer.schedule(new TickertapeTask(tickertape, tickerLingua, logger), 0, 
            (long)(1000L * 60 * 60 * wakeupIntervalHours));
        // Sleep for a few seconds to let this task complete its setup.
        Thread.sleep(2 * 1000);
      }
      else {
        logger.info(String.format("Tickertape %s not enabled.  Skipping.", id));
      }
    }
    System.out.println("Finished starting up all of the Tickertape instances.");
  }
}
