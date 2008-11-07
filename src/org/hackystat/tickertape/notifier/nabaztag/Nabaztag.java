package org.hackystat.tickertape.notifier.nabaztag;

import java.util.logging.Logger;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;

/** A notifier for the Nabaztag ambient device. */
public class Nabaztag {
  
  /** The serial number for this device. */
  private String serialNumber = "0013D3844E5A";
  /** The token for this device. */
  private String token = "1202974551";
  /** The logger. */
  private Logger logger;
  /** The default Ambient service URL. */
  private String host = "http://api.nabaztag.com/vl/FR/api.jsp";

  /**
   * Create a new instance of a Nabaztag notifier.
   * @param serialNumber The serial number for this Nabaztag device. 
   * @param token The token for this device. 
   * @param logger The logger for status messages. 
   */
  public Nabaztag(String serialNumber, String token, Logger logger) {
    this.serialNumber = serialNumber;
    this.token = token;
    this.logger = logger;
  }

  /**
   * Contacts the Nabaztag and sends the passed string. 
   * @param message The message to be sent to the Nabaztag. 
   */
  public void notify(String message) {
    logger.info("Notifying Nabaztag: " + message);
    String url = String.format("%s?sn=%s&token=%s&tts=%s", host, serialNumber, token, 
        message.trim().replace(" ", "+").replace("@", "+at+"));
    logger.info("Posting the following URL: " + url);
    logger.info("Response is: " + post(url));
  }
  
  /**
   * Performs an HTTP POST of the passed URL.
   * 
   * @return The response from the server as a string. 
   */
  public String post(String url) {
    // Prepare the request
    Request request = new Request(Method.POST, url);
    request.setReferrerRef("http://www.hackystat.org");

    // Handle it using an HTTP client connector
    Client client = new Client(Protocol.HTTP);
    Response response = client.handle(request);

    if (response.getStatus().isSuccess()) {
      return Boolean.TRUE.toString();
    }
    else {
      return response.getStatus().getDescription();
    }
  }

}