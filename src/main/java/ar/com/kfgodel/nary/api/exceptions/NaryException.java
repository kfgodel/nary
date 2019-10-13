package ar.com.kfgodel.nary.api.exceptions;

/**
 * This class is the super type of all nary related exceptions
 *
 * Date: 12/10/19 - 21:24
 */
public class NaryException extends RuntimeException {

  public NaryException(String message) {
    super(message);
  }

  public NaryException(String message, Throwable cause) {
    super(message, cause);
  }

  public NaryException(Throwable cause) {
    super(cause);
  }
}
