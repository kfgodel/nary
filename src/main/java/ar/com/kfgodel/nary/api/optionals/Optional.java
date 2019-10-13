package ar.com.kfgodel.nary.api.optionals;

import ar.com.kfgodel.nary.api.arity.MonoElement;
import ar.com.kfgodel.nary.api.arity.MultiElement;

/**
 * This type defines the protocol Nary has to operate with its element, when we know it has just 1 element.<br>
 *   It's based on {@link java.util.Optional} to be as compatible and intuitive as possible but it departs
 *   from its concepts adding own.<br>
 *
 * Created by kfgodel on 06/11/14.
 */
@Deprecated
public interface Optional<T> extends MultiElement<T>, MonoElement<T> {

}
