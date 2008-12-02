package org.hackystat.tickertape.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
  private Logger logger = HackystatLogger.getLogger("org.hackystat.tickertape", "tickertape");
  /** The tickertape.xml config file. */
  private File configFile = null;
  /** The timers associated with the tickers. */
  List<Timer> timers = new ArrayList<Timer>();
  
  
  /**
   * Instantiates a new Tickertape server.
   * Uses either the passed configFile, or the default in ~/.hackystat/tickertape/tickertape.xml
   * if null is passed.
   * @param configFile The configFile to use, or null if the default should be used. 
   */
  public Server(String configFile) {
    // Create the tickertape server.
    File dotTickerTape = new File(System.getProperty("user.home"), 
        ".hackystat/tickertape/tickertape.xml");
    this.configFile = (configFile == null) ? dotTickerTape : new File(configFile);
    if (!this.configFile.exists()) {
      System.out.printf("%s not found.", configFile);
    }
  }
  
  /**
   * Starts up the Tickertape instances. 
   * @throws Exception If problems occur. 
   */
  public void start() throws Exception {
    System.out.println("Configuring system from: " + configFile.getAbsolutePath());
    TickerLingua tickerLingua = new TickerLingua(configFile.getAbsolutePath());
    String loggingLevel = tickerLingua.getLoggingLevel();
    System.out.println("Setting logging level to: " + loggingLevel);
    HackystatLogger.setLoggingLevel(this.logger, loggingLevel);
    
    // Start up all enabled tickertapes. 
    for (Tickertape tickertape : tickerLingua.getTickertapes()) {
      // Sleep for a few seconds between startups.  
      String id = tickertape.getId();
      if (tickertape.enabled()) {
        Timer timer = new Timer();
        this.timers.add(timer);
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

  /**
   * Stops all timers that have been started.
   */
  public void stop() {
    logger.info("Cancelling all timers.");
    for (Timer timer : timers) {
      timer.cancel();
    }
  }
  
  
  /**
   * Starts up this tickertape instance. 
   * Reads in the tickerlingua file and sets up timer tasks for each defined and enabled tickertape.
   * Looks in ~/.hackystat/tickertape/tickerlingua.xml by default. 
   * @param args If provided, the location of the tickerlingua.xml file. 
   * @throws Exception if problems occur.
   */
  public static void main(String[] args) throws Exception {
    String configFile = (args.length == 0) ? null : args[0];
    Server server = new Server(configFile);
    try {
      server.start();
      System.out.println("Tickertape is running. Press return to stop server.");
      while (System.in.available() == 0) {
        Thread.sleep(5000);
      }
      server.stop();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
