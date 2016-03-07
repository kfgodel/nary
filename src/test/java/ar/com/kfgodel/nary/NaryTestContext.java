package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.optionals.Optional;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Interface to define the spec testing context for nary tests
 * Created by kfgodel on 04/04/15.
 */
public interface NaryTestContext extends TestContext {

  Nary<Integer> nary();
  void nary(Supplier<Nary<Integer>> definition);

  Stream<Integer> stream();
  void stream(Supplier<Stream<Integer>> definition);

  Object[] array();
  void array(Supplier<Object[]> definition);

  Iterator<Integer> iterator();
  void iterator(Supplier<Iterator<Integer>> definition);

  Spliterator<Integer> spliterator();
  void spliterator(Supplier<Spliterator<Integer>> definition);

  Optional<Integer> optional();
  void optional(Supplier<Optional<Integer>> definition);

}
