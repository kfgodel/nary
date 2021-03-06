package ar.com.kfgodel.nary.api.arity;

import ar.com.kfgodel.nary.api.Nary;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This type represents the extended api from {@link java.util.stream.Stream} modified to return
 * instances of {@link ar.com.kfgodel.nary.api.Nary}.<br>
 * The purpose of this type is just to organize code so the methods from {@link java.util.stream.Stream}
 * that have covariant return types are separated from the ones added by {@link Nary}.<br>
 *
 * Date: 13/10/19 - 14:35
 *
 * @param <T> Type of contained elements
 */
public interface NaryStream<T> extends Stream<T> {

  @Override
  Nary<T> distinct();

  @Override
  Nary<T> sorted();

  @Override
  Nary<T> sorted(Comparator<? super T> comparator);

  @Override
  Nary<T> limit(long maxSize);

  @Override
  Nary<T> skip(long n);

  @Override
  Nary<T> peek(Consumer<? super T> action);

  @Override
  Nary<T> filter(Predicate<? super T> predicate);

  @Override
  <R> Nary<R> map(Function<? super T, ? extends R> mapper);

  @Override
  <R> Nary<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

}
