package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.Unary;
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

/**
 * This type verifies the behavior of a nary when it's created from an optional as source
 * <p>
 * Created by kfgodel on 06/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class NaryFromOptionalTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an optional based nary", () -> {

      it("behaves like an empty nary when the optional is empty", () -> {
        Unary<Object> nary = Nary.from(Optional.empty());
        assertThat((Stream) nary).isSameAs(Nary.empty());
      });

      context().unary( () -> Nary.from(Optional.of(3)));

      describe("as non empty optional", () -> {
        it("returns the value when get() is called", () -> {
          Integer result = context().unary().unique().get();
          assertThat(result).isEqualTo(3);
        });
        it("answers true to #isPresent()", () -> {
          assertThat(context().unary().isPresent()).isTrue();
        });
        it("answers false to #isAbsent()", () -> {
          assertThat(context().unary().isAbsent()).isFalse();
        });
        it("executes the #ifPresent() argument", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().unary().ifPresent((value) -> executed.set(true));

          assertThat(executed.get()).isTrue();
        });
        it("never executes the #ifAbsent() argument", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().unary().ifAbsent(() -> executed.set(true));

          assertThat(executed.get()).isFalse();
        });
        it("transforms the value when called to #mapFilteringNullResult()", () -> {
          Nary<Integer> result = context().unary().mapFilteringNullResult((value) -> value + 1);

          assertThat(result.unique().get()).isEqualTo(4);
        });
        it("transforms  the value when called to #flatmapOptional()", () -> {
          Nary<Integer> result = context().unary().flatMapOptional((value) -> Optional.of(5));

          assertThat(result.unique().get()).isEqualTo(5);
        });
        it("never returns the alternative value when #orElse() is called", () -> {
          Integer result = context().unary().orElse(4);
          assertThat(result).isEqualTo(3);
        });
        it("never executes the supplier argument when #orElseGet() is called", () -> {
          Integer result = context().unary().orElseGet(() -> 4);
          assertThat(result).isEqualTo(3);
        });
        it("never executes the supplier argument when #orElseUse() is called", () -> {
          final Nary<Integer> result = context().unary().orElseUse(() -> 4);
          assertThat(result.unique().get()).isEqualTo(3);
        });
        it("never throws the exception when #orElseThrow() is called", () -> {
          Integer result = context().unary().orElseThrow(() -> new RuntimeException("Kaboom"));
          assertThat(result).isEqualTo(3);
        });
        describe("#equals", () -> {
          it("is true if other optional has the same value", () -> {
            boolean result = context().unary().equals(Nary.ofNonNullable(3));
            assertThat(result).isTrue();
          });
          it("is false if the other optional has different value", () -> {
            boolean result = context().unary().equals(Nary.ofNonNullable(1));
            assertThat(result).isFalse();
          });
          it("is false if the other optional is empty", () -> {
            boolean result = context().unary().equals(Nary.empty());
            assertThat(result).isFalse();
          });
        });
        it("returns the same hashcode as a list with the same element", () -> {
          assertThat(context().unary().hashCode()).isEqualTo(Lists.newArrayList(3).hashCode());
        });
        it("returns a one element nary representation when #toString() is called", () -> {
          assertThat(context().unary().toString()).isEqualTo("OneElementNary{ 3 }");
        });
        it("returns an equivalent optional when asOptional() is called", () -> {
          assertThat(context().unary().asOptional()).isEqualTo(Optional.of(3));
        });
        it("returns an non empty unary when asUni() is called", () -> {
          assertThat((Stream)context().unary().unique()).containsExactly(3);
        });
        it("returns a one element container when #collect(supplier, accumulator) is called", () -> {
          List<Integer> result = context().unary().collect(ArrayList::new, ArrayList::add);
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns a one element stream when #asStream() is called", () -> {
          List<Integer> result = context().unary().collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        describe("#concat(Stream)", () -> {
          it("returns a one element nary if the stream is empty", () -> {
            List<Integer> result = context().unary().concat(Nary.empty())
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns a nary with the value and the stream elements if the stream is not empty", () -> {
            List<Integer> result = context().unary().concat(Nary.ofNonNullable(1, 2, 3))
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 1, 2, 3));
          });
        });
        describe("#concat(Optional)", () -> {
          it("returns a one element nary if the Optional is empty", () -> {
            List<Integer> result = context().unary().concat(Optional.empty())
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns a nary with the value and the element if the Optional is not empty", () -> {
            List<Integer> result = context().unary().concat(Optional.of(1))
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 1));
          });
        });
        describe("#add", () -> {
          it("returns a one element nary if no arguments are passed", () -> {
            List<Integer> result = context().unary().add()
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns a nary with the value and the elements passed as arguments", () -> {
            List<Integer> result = context().unary().add(1, 2, 3)
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 1, 2, 3));
          });
        });

        it("returns a list with only the value", () -> {
          List<Integer> oneElementList = context().unary().collectToList();
          assertThat(oneElementList).isEqualTo(Lists.newArrayList(3));
        });
        it("returns a set with only the value", () -> {
          Set<Integer> oneElementSet = context().unary().collectToSet();
          assertThat(oneElementSet).isEqualTo(Sets.newHashSet(3));
        });

      });

      describe("as one element stream", () -> {
        it("applies the predicate to filter when #filter() is called", () -> {
          List<Integer> result = context().unary().filter((value) -> value.equals(3))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("transforms the value when #map() is called", () -> {
          List<Integer> result = context().unary().map((value) -> value + 1)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(4));
        });
        it("transforms the value to int when #mapToInt() is called", () -> {
          List<Integer> result = context().unary().mapToInt((value) -> value + 2)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(5));
        });
        it("transforms the value to long when #mapToLong() is called", () -> {
          List<Long> result = context().unary().mapToLong((value) -> value + 3)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(6L));
        });
        it("transforms the value to double when #mapToDouble() is called", () -> {
          List<Double> result = context().unary().mapToDouble((value) -> value + 4)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(7.0));
        });
        it("transforms the value when #flatMap() is called", () -> {
          List<Integer> result = context().unary().flatMap((value) -> Nary.ofNonNullable(8))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(8));
        });
        it("transforms the value when #flatMapToInt() is called", () -> {
          List<Integer> result = context().unary().flatMapToInt((value) -> IntStream.of(value + 2))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(5));
        });
        it("transforms the value when #flatMapToLongs() is called", () -> {
          List<Long> result = context().unary().flatMapToLong((value) -> LongStream.of(value + 3))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(6L));
        });
        it("transforms the value when #flatMapToDouble() is called", () -> {
          List<Double> result = context().unary().flatMapToDouble((value) -> DoubleStream.of(value + 4))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(7.0));
        });
        it("returns same stream when #distinct() is called", () -> {
          List<Integer> result = context().unary().distinct()
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns same stream when #sorted() is called", () -> {
          List<Integer> result = context().unary().sorted()
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns same stream when #sorted(Comparator) is called", () -> {
          List<Integer> result = context().unary().sorted(Integer::compareTo)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("calls the consumer argument when #peek() is called", () -> {
          Variable<Boolean> executed = Variable.of(false);
          List<Integer> result = context().unary().peek((value) -> executed.set(true))
            .collect(Collectors.toList());

          assertThat(executed.get()).isTrue();
        });
        describe("#limit", () -> {
          it("returns the same stream if argument is bigger than 0", () -> {
            List<Integer> result = context().unary().limit(2)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns an empty stream if argument is 0", () -> {
            List<Integer> result = context().unary().limit(0)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList());
          });
        });
        describe("#skip", () -> {
          it("returns the same stream if argument is 0", () -> {
            List<Integer> result = context().unary().skip(0)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns an empty stream if argument is bigger than 0", () -> {
            List<Integer> result = context().unary().skip(1)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList());
          });
        });
        it("returns an empty spliterator when called to #spliterator", () -> {
          final Spliterator<Integer> spliterator = context().unary().spliterator();

          final List<Integer> collected = StreamSupport.stream(spliterator, false)
            .collect(Collectors.toList());

          assertThat(collected).containsExactly(3);
        });
        it("calls the consumer once when #forEach() is called", () -> {
          Variable<Integer> accumulated = Variable.of(0);

          context().unary().forEach((value) -> accumulated.set(accumulated.get() + 1));

          assertThat(accumulated.get()).isEqualTo(1);
        });
        it("calls the consumer when #forEachOrdered() is called", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().unary().forEachOrdered((value) -> executed.set(true));

          assertThat(executed.get()).isTrue();
        });
        it("returns a one element array when called to #toArray()", () -> {
          Object[] result = context().unary().toArray();

          assertThat(result).isEqualTo(new Object[]{3});
        });
        it("calls the with 1 as size the intFunction argument of #toArray(IntFunction)", () -> {
          Integer[] result = context().unary().toArray(Integer[]::new);

          assertThat(result).isEqualTo(new Integer[]{3});
        });
        it("accumulates the value with the identity when #reduce(identity,accumulator) is called", () -> {
          Integer result = context().unary().reduce(23, (a, b) -> a + b);
          assertThat(result).isEqualTo(26);
        });
        it("ignores the combiner when #reduce(identity,accumulator, combiner) is called", () -> {
          Integer result = context().unary().reduce(28, (a, b) -> a + b, (a, b) -> a + b);
          assertThat(result).isEqualTo(31);
        });
        it("returns the value when #reduce(accumulator) is called", () -> {
          Optional<Integer> result = context().unary().reduce((a, b) -> a + b);
          assertThat(result.get()).isEqualTo(3);
        });
        it("accumulates the value on the container when #collect(supplier, accumulator, combiner) is called ", () -> {
          List<Integer> result = context().unary().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("collects the value when #collect(collector) is called", () -> {
          List<Integer> result = context().unary().collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns the native optional when #min() is called", () -> {
          Optional<Integer> result = context().unary().min(Integer::compareTo);
          assertThat(result).isEqualTo(Optional.of(3));
        });
        it("returns the native optional when #max() is called", () -> {
          Optional<Integer> result = context().unary().max(Integer::compareTo);
          assertThat(result).isEqualTo(Optional.of(3));
        });
        it("returns 1 when #count() is called", () -> {
          long result = context().unary().count();
          assertThat(result).isEqualTo(1);
        });
        it("applies the predicate on the value and returns its result when #anyMatch() is called", () -> {
          boolean result = context().unary().anyMatch((value) -> value.equals(3));
          assertThat(result).isTrue();
        });
        it("applies the predicate on the value and returns its result when #allMatch() is called", () -> {
          boolean result = context().unary().allMatch((value) -> value.equals(4));
          assertThat(result).isFalse();
        });
        it("applies the predicate on the value and returns its negation when #noneMatch() is called", () -> {
          boolean result = context().unary().noneMatch((value) -> value.equals(3));
          assertThat(result).isFalse();
        });
        it("returns the native optional when #findFirst() is called", () -> {
          Optional<Integer> result = context().unary().findFirst();
          assertThat(result).isEqualTo(Optional.of(3));
        });
        it("returns the native optional when #findAny() is called", () -> {
          Optional<Integer> result = context().unary().findAny();
          assertThat(result).isEqualTo(Optional.of(3));
        });

      });

      describe("as one element nary", () -> {

        it("returns itself when #findLast() is called", () -> {
          List<Integer> result = context().unary().findLast()
            .collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });

        it("returns the value when #reduceNary(accumulator) is called", () -> {
          Unary<Integer> result = context().unary().reduceNary((a, b) -> a + b);
          assertThat(result.get()).isEqualTo(3);
        });

        it("returns itself when #findFirstNary() is called", () -> {
          Unary<Integer> result = context().unary().findFirstNary();
          assertThat((Object) result).isEqualTo(Nary.ofNonNullable(3));
        });
        it("returns itself when #findAnyNary() is called", () -> {
          Unary<Integer> result = context().unary().findAnyNary();
          assertThat((Object) result).isEqualTo(Nary.ofNonNullable(3));
        });
        it("returns itself when #minNary() is called", () -> {
          Nary<Integer> result = context().unary().minNary(Integer::compareTo);
          assertThat((Object) result).isEqualTo(Nary.ofNonNullable(3));
        });
        it("returns itself when #maxNary() is called", () -> {
          Nary<Integer> result = context().unary().maxNary(Integer::compareTo);
          assertThat((Object) result).isEqualTo(Nary.ofNonNullable(3));
        });

      });

    });

  }
}