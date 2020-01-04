package ar.com.kfgodel.nary.api;

import ar.com.kfgodel.nary.api.arity.MonoElement;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This type represents an unknown amount of elements but with the certainty that is either 0 or 1.
 * It reduces uncertainty over {@link Nary} by putting an upper bound on the possible quantity of elements and thus
 * allowing to offer more operations as part of the api.<br>
 * It can be seen as an extended {@link java.util.Optional} api including {@link java.util.stream.Stream} operations.<br>
 * <br>
 * This interface allows using the concept of {@link java.util.Optional} but integrating it better with
 * {@link java.util.stream.Stream}s, and allows combining them without having to do intermediate conversions
 * (in contrast to native {@link java.util.stream.Stream} and {@link java.util.Optional} types).
 * Date: 4/1/20 - 12:14
 */
public interface Unary<T> extends Nary<T>, MonoElement<T> {

  @Override
  Unary<T> distinct();

  @Override
  Unary<T> sorted();

  @Override
  Unary<T> sorted(Comparator<? super T> comparator);

  @Override
  Unary<T> limit(long maxSize);

  @Override
  Unary<T> skip(long n);

  @Override
  Unary<T> peek(Consumer<? super T> action);

  @Override
  Unary<T> filter(Predicate<? super T> predicate);

  @Override
  <R> Unary<R> map(Function<? super T, ? extends R> mapper);

  @Override
  <U> Unary<U> mapFilteringNullResult(Function<? super T, ? extends U> mapper);

}
