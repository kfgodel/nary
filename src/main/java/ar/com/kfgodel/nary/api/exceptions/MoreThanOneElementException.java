package ar.com.kfgodel.nary.api.exceptions;

/**
 * This type represents an exception thrown when a nary that has more than 1 element is accessed as it had only 1.
 * Created by kfgodel on 06/11/14.
 */
public class MoreThanOneElementException extends NaryException {

    public MoreThanOneElementException(String message) {
        super(message);
    }

}
