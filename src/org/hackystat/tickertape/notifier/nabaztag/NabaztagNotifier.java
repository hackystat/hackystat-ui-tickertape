package org.hackystat.tickertape.notifier.nabaztag;

import java.util.logging.Logger;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * A notifier for the Nabaztag rabbit.
 * @author Philip Johnson
 */
public class NabaztagNotifier {
  
  /** The serial number for this device. */
  private String serialNumber;
  /** The token for this device. */
  private String token;
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
  public NabaztagNotifier(String serialNumber, String token, Logger logger) {
    this.serialNumber = serialNumber;
    this.token = token;
    this.logger = logger;
  }

  /**
   * Contacts the Nabaztag and sends the passed string. 
   * All spaces are replaced by '+'.
   * Also, all '@' are replaced by '+at+'.
   * @param message The message to be sent to the Nabaztag. 
   */
  public void notify(String message) {
    String newMessage = message.replace(' ', '+').replace("@", "+at+");
    logger.info("Notifying Nabaztag: " + newMessage);
    String url = String.format("%s?sn=%s&token=%s&tts=%s", host, serialNumber, token, newMessage); 
    post(url);
  }
  
  /**
   * Performs an HTTP POST of the passed URL.
   * @param url The URL. 
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
