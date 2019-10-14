package ar.com.kfgodel.nary.api.exceptions;

/**
 * This type represents an exception thrown when a nary that has more than 1 element is accessed as it had only 1.
 * Created by kfgodel on 06/11/14.
 */
public class MoreThanOneElementException extends NaryException {

  /**
   * Creates a new instances without a previous cause
   *
   * @param message The error message describing what went wrong
   */
  public MoreThanOneElementException(String message) {
    super(message);
  }

}
