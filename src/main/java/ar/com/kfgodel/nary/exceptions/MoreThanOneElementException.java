package ar.com.kfgodel.nary.exceptions;

/**
 * This type represents an exception due to a Nary used as an Optional but having more than one element to return
 * Created by kfgodel on 06/11/14.
 */
public class MoreThanOneElementException extends RuntimeException {

    public MoreThanOneElementException(String message) {
        super(message);
    }

}
