package ar.com.kfgodel.nary.api.exceptions;

/**
 * This class is the super type of all nary related exceptions
 * <p>
 * Date: 12/10/19 - 21:24
 */
public class NaryException extends RuntimeException {

  /**
   * Creates a new instance without a previous cause indicating the error message
   *
   * @param message A description of what went wrong
   */
  public NaryException(String message) {
    super(message);
  }

}
