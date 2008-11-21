package org.hackystat.tickertape.tickerlingua;

/**
 * The exception class for all TickerLingua definition errors. 
 * @author Philip Johnson
 *
 */
public class TickerLinguaException extends Exception {

  /** Serial version id. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs this instance with an exception message.
   * 
   * @param message The exception message.
   */
  public TickerLinguaException(String message) {
    super(message);
  }

  /**
   * Constructs this instance with a wrapped exception.
   * @param message The message.
   * @param throwable The wrapped exception.
   */
  public TickerLinguaException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
