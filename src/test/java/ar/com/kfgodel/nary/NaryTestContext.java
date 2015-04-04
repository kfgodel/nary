package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.kfgodel.nary.api.Nary;

import java.util.function.Supplier;

/**
 * Interface to define the spec testing context for nary tests
 * Created by kfgodel on 04/04/15.
 */
public interface NaryTestContext extends TestContext {

    Nary<Integer> nary();
    void nary(Supplier<Nary<Integer>> definition);

}
