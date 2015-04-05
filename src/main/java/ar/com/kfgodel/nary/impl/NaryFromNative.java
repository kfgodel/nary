package ar.com.kfgodel.nary.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.exceptions.MoreThanOneElementException;
import ar.com.kfgodel.optionals.Optional;
import ar.com.kfgodel.optionals.OptionalFromStream;
import ar.com.kfgodel.optionals.StreamFromOptional;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

/**
 * This type implements a Nary from an optional or a stream instance.<br>
 *     Created from an optional, when used as stream, an internal stream is created from the optional.<br>
 *     Created from a stream, when used as Optional, an attempt is made to create an optional that could fail (if more than one element present in the stream)
 * Created by kfgodel on 06/11/14.
 */
public class NaryFromNative<T> implements Nary<T> {

    private Stream<T> nativeStream;
    private java.util.Optional<T> nativeOptional;

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        return asNativeStream().filter(predicate);
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return asNativeStream().map(mapper);
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return asNativeStream().mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return asNativeStream().mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return asNativeStream().mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return asNativeStream().flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return asNativeStream().flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return asNativeStream().flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return asNativeStream().flatMapToDouble(mapper);
    }

    @Override
    public Stream<T> distinct() {
        return asNativeStream().distinct();
    }

    @Override
    public Stream<T> sorted() {
        return asNativeStream().sorted();
    }

    @Override
    public Stream<T> sorted(Comparator<? super T> comparator) {
        return asNativeStream().sorted(comparator);
    }

    @Override
    public Stream<T> peek(Consumer<? super T> action) {
        return asNativeStream().peek(action);
    }

    @Override
    public Stream<T> limit(long maxSize) {
        return asNativeStream().limit(maxSize);
    }

    @Override
    public Stream<T> skip(long n) {
        return asNativeStream().skip(n);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        asNativeStream().forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        asNativeStream().forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return asNativeStream().toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return asNativeStream().toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return asNativeStream().reduce(identity, accumulator);
    }

    @Override
    public java.util.Optional<T> reduce(BinaryOperator<T> accumulator) {
        return asNativeStream().reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return asNativeStream().reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return asNativeStream().collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return asNativeStream().collect(collector);
    }

    @Override
    public java.util.Optional<T> min(Comparator<? super T> comparator) {
        return asNativeStream().min(comparator);
    }

    @Override
    public java.util.Optional<T> max(Comparator<? super T> comparator) {
        return asNativeStream().max(comparator);
    }

    @Override
    public long count() {
        return asNativeStream().count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return asNativeStream().anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return asNativeStream().allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return asNativeStream().noneMatch(predicate);
    }

    @Override
    public java.util.Optional<T> findFirst() {
        return asNativeStream().findFirst();
    }

    @Override
    public java.util.Optional<T> findAny() {
        return asNativeStream().findAny();
    }

    @Override
    public Iterator<T> iterator() {
        return asNativeStream().iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return asNativeStream().spliterator();
    }

    @Override
    public boolean isParallel() {
        return asNativeStream().isParallel();
    }

    @Override
    public Stream<T> sequential() {
        return asNativeStream().sequential();
    }

    @Override
    public Stream<T> parallel() {
        return asNativeStream().parallel();
    }

    @Override
    public Stream<T> unordered() {
        return asNativeStream().unordered();
    }

    @Override
    public Stream<T> onClose(Runnable closeHandler) {
        return asNativeStream().onClose(closeHandler);
    }

    @Override
    public void close() {
        asNativeStream().close();
    }


    public static<T> NaryFromNative<T> of(T element) {
        return create(Stream.of(element));
    }

    public static<T> NaryFromNative<T> ofNullable(T nullableElement) {
        return create(java.util.Optional.ofNullable(nullableElement));
    }

    public static<T> NaryFromNative<T> create(Stream<T> nativeStream) {
        return create(nativeStream, null);
    }

    public static<T> NaryFromNative<T> create(Spliterator<T> spliterator) {
        return create(StreamSupport.stream(spliterator, false));
    }


    public static<T> NaryFromNative<T> create(java.util.Optional<T> nativeOptional) {
        return create(null, nativeOptional);
    }

    private static<T> NaryFromNative<T> create(Stream<T> nativeStream, java.util.Optional<T> nativeOptional){
        NaryFromNative nary = new NaryFromNative();
        nary.nativeOptional = nativeOptional;
        nary.nativeStream = nativeStream;
        return nary;
    }

    /**
     * Returns an empty Nary
     * @param <T> Type of expected element types
     * @return The empty Nary
     */
    public static<T> Nary<T> empty(){
        return create(java.util.Optional.empty());
    }

    @Override
    public T get() throws NoSuchElementException {
        return asNativeOptional().get();
    }

    @Override
    public boolean isPresent() {
        return asNativeOptional().isPresent();
    }

    @Override
    public boolean isAbsent() throws MoreThanOneElementException {
        return !isPresent();
    }

    @Override
    public void ifPresent(Consumer<? super T> consumer) {
        asNativeOptional().ifPresent(consumer);
    }

    @Override
    public void ifAbsent(Runnable runnable) {
        if(!asNativeOptional().isPresent()){
            runnable.run();
        }
    }

    @Override
    public Optional<T> filterOptional(Predicate<? super T> predicate) {
        return create(asNativeOptional().filter(predicate));
    }

    @Override
    public <U> Optional<U> mapOptional(Function<? super T, ? extends U> mapper) {
        return  create(asNativeOptional().map(mapper));
    }

    @Override
    public <U> Optional<U> flatMapOptional(Function<? super T, java.util.Optional<U>> mapper) {
        return create(asNativeOptional().flatMap(mapper));
    }

    @Override
    public T orElse(T other) {
        return asNativeOptional().orElse(other);
    }

    @Override
    public T orElseGet(Supplier<? extends T> other) {
        return asNativeOptional().orElseGet(other);
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return asNativeOptional().orElseThrow(exceptionSupplier);
    }

    @Override
    public java.util.Optional<T> asNativeOptional() throws MoreThanOneElementException {
        if (nativeOptional == null) {
            nativeOptional = OptionalFromStream.using(this.asNativeStream());
        }
        return nativeOptional;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{stream: " + this.nativeStream + ", optional: " + this.nativeOptional + "}";
    }

    @Override
    public<S> Nary<S> joinedWith(Stream<? extends S> otherNary) {
        return NaryFromNative.create(Stream.concat((Stream) this, otherNary));
    }



    private Stream<T> asNativeStream() {
        if (nativeStream == null) {
            nativeStream = StreamFromOptional.create(this.asNativeOptional());
        }
        return nativeStream;
    }

}
