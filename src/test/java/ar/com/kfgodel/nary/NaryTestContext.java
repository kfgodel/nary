package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.Unary;
import info.kfgodel.jspek.api.contexts.TestContext;

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

  Unary<Integer> unary();
  void unary(Supplier<Unary<Integer>> definition);

  Stream<Integer> stream();
  void stream(Supplier<Stream<Integer>> definition);

  Object[] array();
  void array(Supplier<Object[]> definition);

  Iterator<Integer> iterator();
  void iterator(Supplier<Iterator<Integer>> definition);

  Spliterator<Integer> spliterator();
  void spliterator(Supplier<Spliterator<Integer>> definition);

  Supplier<Integer> supplier();
  void supplier(Supplier<Supplier<Integer>> definition);

}
