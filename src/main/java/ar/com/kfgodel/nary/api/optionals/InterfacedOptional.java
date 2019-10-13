package ar.com.kfgodel.nary.api.optionals;

import ar.com.kfgodel.nary.api.arity.MonoElement;
import ar.com.kfgodel.nary.api.arity.MultiElement;

/**
 * This type represents the interface definition of an optional.<br>
 * Copied from concrete class java.util.Optional, and modified to be compatible with Stream<br>
 * It conserves the same semantics but it's slightly modified to be extended by a stream compatible type
 * <br>
 * Created by kfgodel on 07/03/16.
 */
public interface InterfacedOptional<T> extends MultiElement<T>, MonoElement<T> {

}
