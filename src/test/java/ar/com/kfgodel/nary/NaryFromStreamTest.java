package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import com.google.common.collect.Sets;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.variable.Variable;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * Created by kfgodel on 06/03/16.
 */
@SuppressWarnings("Duplicates")
@RunWith(JavaSpecRunner.class)
public class NaryFromStreamTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("a stream based nary", () -> {

      it("behaves like an empty nary when the stream is empty", () -> {
        Nary<Object> nary = Nary.from(Stream.empty());
        assertThat((Object) nary).isEqualTo(Nary.empty());
      });

      describe("when having exactly 1 element", () -> {
        context().nary(() -> Nary.from(Stream.of(3)));

        it("behaves like an optional based nary", () -> {
          assertThat((Object) context().nary()).isEqualTo(Nary.from(Optional.of(3)));
        });

        describe("as non empty optional", () -> {
          it("returns the value when get() is called", () -> {
            Integer result = context().nary().get();
            assertThat(result).isEqualTo(3);
          });
          it("answers true to #isPresent()", () -> {
            assertThat(context().nary().isPresent()).isTrue();
          });
          it("answers false to #isAbsent()", () -> {
            assertThat(context().nary().isAbsent()).isFalse();
          });
          it("executes the #ifPresent() argument", () -> {
            Variable<Boolean> executed = Variable.of(false);

            context().nary().ifPresent((value) -> executed.set(true));

            assertThat(executed.get()).isTrue();
          });
          it("never executes the #ifAbsent() argument", () -> {
            Variable<Boolean> executed = Variable.of(false);

            context().nary().ifAbsent(() -> executed.set(true));

            assertThat(executed.get()).isFalse();
          });
          it("transforms the value when called to #mapFilteringNullResult()", () -> {
            Nary<Integer> result = context().nary().mapFilteringNullResult((value) -> value + 1);

            assertThat(result.get()).isEqualTo(4);
          });
          it("transforms  the value when called to #flatmapOptional()", () -> {
            Nary<Integer> result = context().nary().flatMapOptional((value) -> Optional.of(5));

            assertThat(result.get()).isEqualTo(5);
          });
          it("never returns the alternative value when #orElse() is called", () -> {
            Integer result = context().nary().orElse(4);
            assertThat(result).isEqualTo(3);
          });
          it("never executes the supplier argument when #orElseGet() is called", () -> {
            Integer result = context().nary().orElseGet(() -> 4);
            assertThat(result).isEqualTo(3);
          });
          it("never executes the supplier argument when #orElseUse() is called", () -> {
            final Nary<Integer> result = context().nary().orElseUse(() -> 4);
            assertThat(result.get()).isEqualTo(3);
          });
          it("never throws the exception when #orElseThrow() is called", () -> {
            Integer result = context().nary().orElseThrow(() -> new RuntimeException("Kaboom"));
            assertThat(result).isEqualTo(3);
          });
          it("never throws the exception when #orElseThrowRuntime() is called", () -> {
            Integer result = context().nary().orElseThrowRuntime(() -> new RuntimeException("Kaboom"));
            assertThat(result).isEqualTo(3);
          });
          describe("#equals", () -> {
            it("is true if other optional has the same value", () -> {
              boolean result = context().nary().equals(Nary.ofNonNullable(3));
              assertThat(result).isTrue();
            });
            it("is false if the other optional has different value", () -> {
              boolean result = context().nary().equals(Nary.ofNonNullable(1));
              assertThat(result).isFalse();
            });
            it("is false if the other optional is empty", () -> {
              boolean result = context().nary().equals(Nary.empty());
              assertThat(result).isFalse();
            });
          });
          it("returns the same hashcode as a list with the same element", () -> {
            assertThat(context().nary().hashCode()).isEqualTo(Lists.newArrayList(3).hashCode());
          });
          it("returns an equivalent optional when asOptional() is called", () -> {
            assertThat(context().nary().asOptional()).isEqualTo(Optional.of(3));
          });
          it("returns a one element container when #collect(supplier, accumulator) is called", () -> {
            List<Integer> result = context().nary().collect(ArrayList::new, ArrayList::add);
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns a one element stream when #asStream() is called", () -> {
            List<Integer> result = context().nary().asStream().collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          describe("#concat(Stream)", () -> {
            it("returns a one element nary if the stream is empty", () -> {
              List<Integer> result = context().nary().concat(Nary.empty())
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3));
            });
            it("returns a nary with the value and the stream elements if the stream is not empty", () -> {
              List<Integer> result = context().nary().concat(Nary.ofNonNullable(1, 2, 3))
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 1, 2, 3));
            });
          });
          describe("#concat(Optional)", () -> {
            it("returns a one element nary if the Optional is empty", () -> {
              List<Integer> result = context().nary().concat(Optional.empty())
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3));
            });
            it("returns a nary with the value and the element if the Optional is not empty", () -> {
              List<Integer> result = context().nary().concat(Optional.of(1))
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 1));
            });
          });
          describe("#add", () -> {
            it("returns a one element nary if no arguments are passed", () -> {
              List<Integer> result = context().nary().add()
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3));
            });
            it("returns a nary with the value and the elements passed as arguments", () -> {
              List<Integer> result = context().nary().add(1, 2, 3)
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 1, 2, 3));
            });
          });

          it("returns a list with only the value", () -> {
            List<Integer> oneElementList = context().nary().collectToList();
            assertThat(oneElementList).isEqualTo(Lists.newArrayList(3));
          });
          it("returns a set with only the value", () -> {
            Set<Integer> oneElementSet = context().nary().collectToSet();
            assertThat(oneElementSet).isEqualTo(Sets.newHashSet(3));
          });

        });

      });

      describe("when having more than one element", () -> {
        context().nary(() -> Nary.from(Stream.of(3, 2, 1, 3)));

        describe("as overflowed optional", () -> {
          it("throws an exception when get() is called", () -> {
            try {
              context().nary().get();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #isPresent() is called", () -> {
            try {
              context().nary().isPresent();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #isAbsent() is called", () -> {
            try {
              context().nary().isAbsent();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #ifPresent() is called", () -> {
            try {
              context().nary().ifPresent((value) -> {
                throw new RuntimeException("never happens");
              });
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #ifAbsent() is called", () -> {
            try {
              context().nary().ifAbsent(() -> {
                throw new RuntimeException("never happens");
              });
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #orElse() is called", () -> {
            try {
              context().nary().orElse(200);
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #orElseGet() is called", () -> {
            try {
              context().nary().orElseGet(() -> {
                throw new RuntimeException("never happens");
              });
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #orElseUse() is called", () -> {
            try {
              context().nary().orElseUse(() -> {
                throw new RuntimeException("never happens");
              });
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #orElseThrow() is called", () -> {
            try {
              context().nary().orElseThrow(() -> new RuntimeException("never happens"));
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("throws an exception when #orElseThrowRuntime() is called", () -> {
            try {
              context().nary().orElseThrowRuntime(() -> new RuntimeException("never happens"));
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          describe("#equals", () -> {
            it("is true if other stream has the same values and order", () -> {
              boolean result = context().nary().equals(Nary.ofNonNullable(3, 2, 1, 3));
              assertThat(result).isTrue();
            });
            it("is false if the other stream has different values", () -> {
              boolean result = context().nary().equals(Nary.ofNonNullable(4, 5));
              assertThat(result).isFalse();
            });
            it("is false if the other stream has different order", () -> {
              boolean result = context().nary().equals(Nary.ofNonNullable(1, 2, 3, 3));
              assertThat(result).isFalse();
            });
            it("is false if the other optional is empty", () -> {
              boolean result = context().nary().equals(Nary.empty());
              assertThat(result).isFalse();
            });
          });
          it("returns the same hashcode as a list with the same elements", () -> {
            assertThat(context().nary().hashCode()).isEqualTo(Lists.newArrayList(3, 2, 1, 3).hashCode());
          });
          it("returns a nary representation when #toString() is called", () -> {
            assertThat(context().nary().toString())
              .startsWith("StreamBasedNary{cachedOptional=null, sourceStream=java.util.stream.ReferencePipeline")
              .endsWith("}");
          });
          it("throws an exception when asOptional() is called", () -> {
            try {
              context().nary().asOptional();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            } catch (MoreThanOneElementException e) {
              assertThat(e).hasMessage("Expecting 1 element in the stream to create an optional but found at least 2: [3, 2]");
            }
          });
          it("returns a container with the values when #collect(supplier, accumulator) is called", () -> {
            List<Integer> result = context().nary().collect(ArrayList::new, ArrayList::add);
            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("returns the native stream when #asStream() is called", () -> {
            List<Integer> result = context().nary().asStream().collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          describe("#concat(Stream)", () -> {
            it("returns itself if the stream is empty", () -> {
              List<Integer> result = context().nary().concat(Nary.empty())
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
            });
            it("returns a nary with concatenated elements if the stream is not empty", () -> {
              List<Integer> result = context().nary().concat(Nary.ofNonNullable(4, 5, 6))
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3, 4, 5, 6));
            });
          });
          describe("#concat(Optional)", () -> {
            it("returns itself if the Optional is empty", () -> {
              List<Integer> result = context().nary().concat(Optional.empty())
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
            });
            it("returns a nary with concatenated elements if the Optional is not empty", () -> {
              List<Integer> result = context().nary().concat(Optional.of(4))
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3, 4));
            });
          });
          describe("#add", () -> {
            it("returns itself if no arguments are passed", () -> {
              List<Integer> result = context().nary().add()
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
            });
            it("returns a nary with concatenated elements passed as arguments", () -> {
              List<Integer> result = context().nary().add(4, 5, 6)
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3, 4, 5, 6));
            });
          });

          it("returns a list with the values in the order they are present", () -> {
            List<Integer> list = context().nary().collectToList();
            assertThat(list).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("returns a set with the values of the nary without duplicates or an order", () -> {
            Set<Integer> set = context().nary().collectToSet();
            assertThat(set).isEqualTo(Sets.newHashSet(1, 2, 3));
          });


        });

        describe("as a normal stream", () -> {
          it("applies the predicate to filter when #filter() is called", () -> {
            List<Integer> result = context().nary().filter((value) -> value.equals(3))
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 3));
          });
          it("transforms the values when #map() is called", () -> {
            List<Integer> result = context().nary().map((value) -> value + 1)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(4, 3, 2, 4));
          });
          it("transforms the values when #mapFilteringNullResult is called, and discards null results", () -> {
            List<Integer> result = context().nary()
              .mapFilteringNullResult((value) -> value.equals(1) ? null : value + 1)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(4, 3, 4));
          });
          it("transforms the values to int when #mapToInt() is called", () -> {
            List<Integer> result = context().nary().mapToInt((value) -> value + 2)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(5, 4, 3, 5));
          });
          it("transforms the values to long when #mapToLong() is called", () -> {
            List<Long> result = context().nary().mapToLong((value) -> value + 3)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(6L, 5L, 4L, 6L));
          });
          it("transforms the values to double when #mapToDouble() is called", () -> {
            List<Double> result = context().nary().mapToDouble((value) -> value + 4)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(7.0, 6.0, 5.0, 7.0));
          });
          it("transforms the values when #flatMap() is called", () -> {
            List<Integer> result = context().nary().flatMap((value) -> Nary.ofNonNullable(8))
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(8, 8, 8, 8));
          });
          it("transforms the values #flatmapOptional() allowing Optionals to be used as 1 element streams", () -> {
            List<Integer> result = context().nary()
              .flatMapOptional((value) -> value.equals(1) ? Optional.empty() : Optional.of(value))
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 3));
          });
          it("transforms the values when #flatMapToInt() is called", () -> {
            List<Integer> result = context().nary().flatMapToInt((value) -> IntStream.of(value + 2))
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(5, 4, 3, 5));
          });
          it("transforms the value when #flatMapToLongs() is called", () -> {
            List<Long> result = context().nary().flatMapToLong((value) -> LongStream.of(value + 3))
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(6L, 5L, 4L, 6L));
          });
          it("transforms the value when #flatMapToDouble() is called", () -> {
            List<Double> result = context().nary().flatMapToDouble((value) -> DoubleStream.of(value + 4))
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(7.0, 6.0, 5.0, 7.0));
          });
          it("returns a stream without duplicates when #distinct() is called", () -> {
            List<Integer> result = context().nary().distinct()
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1));
          });
          it("returns a naturally ordered stream when #sorted() is called", () -> {
            List<Integer> result = context().nary().sorted()
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3, 3));
          });
          it("returns a comparator base ordered stream when #sorted(Comparator) is called", () -> {
            List<Integer> result = context().nary().sorted(Integer::compareTo)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3, 3));
          });
          it("calls the consumer argument when #peek() is called", () -> {
            List<Integer> peeked = new ArrayList<Integer>();
            context().nary().peek(peeked::add)
              .collect(Collectors.toList());

            assertThat(peeked).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("returns a stream with only the first N elements when called to #limit()", () -> {
            List<Integer> result = context().nary().limit(2)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 2));
          });
          it("returns a stream without the first N elements when called to #skip()", () -> {
            List<Integer> result = context().nary().skip(2)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(1, 3));
          });
          it("returns an empty spliterator when called to #spliterator", () -> {
            final Spliterator<Integer> spliterator = context().nary().spliterator();

            final List<Integer> collected = StreamSupport.stream(spliterator, false)
              .collect(Collectors.toList());

            assertThat(collected).containsExactly(3, 2, 1, 3);
          });
          it("calls the consumer for each value when #forEach() is called", () -> {
            List<Integer> list = new ArrayList<Integer>();
            context().nary().forEach(list::add);

            assertThat(list).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("calls the consumer for each value in the stream order when #forEachOrdered() is called", () -> {
            List<Integer> list = new ArrayList<Integer>();

            context().nary().forEachOrdered(list::add);

            assertThat(list).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("returns an object with the values array when called to #toArray()", () -> {
            Object[] result = context().nary().toArray();

            assertThat(result).isEqualTo(new Object[]{3, 2, 1, 3});
          });
          it("calls the with stream count as size the intFunction argument of #toArray(IntFunction)", () -> {
            Integer[] result = context().nary().toArray(Integer[]::new);

            assertThat(result).isEqualTo(new Integer[]{3, 2, 1, 3});
          });
          it("honors the contract when #reduce(identity,accumulator) is called", () -> {
            Integer result = context().nary().reduce(1, (a, b) -> a + b);
            assertThat(result).isEqualTo(1 + 3 + 2 + 1 + 3);
          });
          it("honors the contract when #reduce(identity,accumulator, combiner) is called", () -> {
            Integer result = context().nary().reduce(2, (a, b) -> a + b, (a, b) -> a + b);
            assertThat(result).isEqualTo(2 + 3 + 2 + 1 + 3);
          });
          it("honors the contract when #reduce(accumulator) is called", () -> {
            java.util.Optional<Integer> result = context().nary().reduce((a, b) -> a + b);
            assertThat(result.get()).isEqualTo(3 + 2 + 1 + 3);
          });
          it("honors the contract when #collect(supplier, accumulator, combiner) is called ", () -> {
            List<Integer> result = context().nary().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("honors the contract when #collect(collector) is called", () -> {
            List<Integer> result = context().nary().collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("honors the contract when #min() is called", () -> {
            java.util.Optional<Integer> result = context().nary().min(Integer::compareTo);
            assertThat(result).isEqualTo(java.util.Optional.of(1));
          });
          it("honors the contract when #max() is called", () -> {
            java.util.Optional<Integer> result = context().nary().max(Integer::compareTo);
            assertThat(result).isEqualTo(java.util.Optional.of(3));
          });
          it("returns the amount of values when #count() is called", () -> {
            long result = context().nary().count();
            assertThat(result).isEqualTo(4);
          });
          it("honors the contract when #anyMatch() is called", () -> {
            boolean result = context().nary().anyMatch((value) -> value.equals(1));
            assertThat(result).isTrue();
          });
          it("honors the contract when #allMatch() is called", () -> {
            boolean result = context().nary().allMatch((value) -> value > 1);
            assertThat(result).isFalse();
          });
          it("honors the contract when #noneMatch() is called", () -> {
            boolean result = context().nary().noneMatch((value) -> value.equals(4));
            assertThat(result).isTrue();
          });
          it("returns the native optional when #findFirst() is called", () -> {
            java.util.Optional<Integer> result = context().nary().findFirst();
            assertThat(result).isEqualTo(java.util.Optional.of(3));
          });
          it("returns the native optional when #findAny() is called", () -> {
            java.util.Optional<Integer> result = context().nary().findAny();
            assertThat(result).isEqualTo(java.util.Optional.of(3));
          });

        });

        describe("as nary with elements", () -> {

          it("returns the last element when #findLast() is called", () -> {
            List<Integer> result = context().nary().findLast()
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("honors the contract when #reduceNary(accumulator) is called", () -> {
            Nary<Integer> result = context().nary().reduceNary((a, b) -> a + b);
            assertThat(result.get()).isEqualTo(3 + 2 + 1 + 3);
          });
          it("honors the contract when #findFirst() is called", () -> {
            Nary<Integer> result = context().nary().findFirstNary();
            assertThat((Object) result).isEqualTo(Nary.ofNonNullable(3));
          });
          it("honors the contract when #findAny() is called", () -> {
            Nary<Integer> result = context().nary().findAnyNary();
            assertThat((Object) result).isEqualTo(Nary.ofNonNullable(3));
          });
          it("honors the contract when #minNary() is called", () -> {
            Nary<Integer> result = context().nary().minNary(Integer::compareTo);
            assertThat((Object) result).isEqualTo(Nary.ofNonNullable(1));
          });
          it("honors the contract when #maxNary() is called", () -> {
            Nary<Integer> result = context().nary().maxNary(Integer::compareTo);
            assertThat((Object) result).isEqualTo(Nary.ofNonNullable(3));
          });

        });
      });
    });
  }
}