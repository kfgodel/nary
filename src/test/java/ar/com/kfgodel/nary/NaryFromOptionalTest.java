package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
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
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of a nary when it's created from an optional as source
 * 
 * Created by kfgodel on 06/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class NaryFromOptionalTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an optional based nary", () -> {

      it("behaves like an empty nary when the optional is empty",()->{
        Nary<Object> nary = Nary.create(Optional.empty());
        assertThat((Stream)nary).isSameAs(Nary.empty());
      });

      context().nary(()-> Nary.create(Optional.of(3)));

      describe("as non empty optional", () -> {
        it("returns the value when get() is called",()->{
          Integer result = context().nary().get();
          assertThat(result).isEqualTo(3);
        });
        it("answers true to #isPresent()",()->{
          assertThat(context().nary().isPresent()).isTrue();
        });
        it("answers false to #isAbsent()",()->{
          assertThat(context().nary().isAbsent()).isFalse();
        });
        it("executes the #ifPresent() argument",()->{
          Variable<Boolean> executed = Variable.of(false);

          context().nary().ifPresent((value)-> executed.set(true));

          assertThat(executed.get()).isTrue();
        });
        it("never executes the #ifAbsent() argument",()->{
          Variable<Boolean> executed = Variable.of(false);

          context().nary().ifAbsent(()-> executed.set(true));

          assertThat(executed.get()).isFalse();
        });
        it("transforms the value when called to #mapFilteringNullResult()",()->{
          Nary<Integer> result = context().nary().mapFilteringNullResult((value) -> value + 1);

          assertThat(result.get()).isEqualTo(4);
        });
        it("transforms  the value when called to #flatmapOptional()",()->{
          Nary<Integer> result = context().nary().flatMapOptional((value)-> Optional.of(5));

          assertThat(result.get()).isEqualTo(5);
        });
        it("never returns the alternative value when #orElse() is called",()->{
          Integer result = context().nary().orElse(4);
          assertThat(result).isEqualTo(3);
        });
        it("never executes the supplier argument when #orElseGet() is called",()->{
          Integer result = context().nary().orElseGet(()-> 4);
          assertThat(result).isEqualTo(3);
        });
        it("never throws the exception when #orElseThrow() is called",()->{
          Integer result = context().nary().orElseThrow(() -> new RuntimeException("Kaboom"));
          assertThat(result).isEqualTo(3);
        });
        it("never throws the exception when #orElseThrowRuntime() is called", () -> {
          Integer result = context().nary().orElseThrowRuntime(() -> new RuntimeException("Kaboom"));
          assertThat(result).isEqualTo(3);
        });
        describe("#equals", () -> {
          it("is true if other optional has the same value",()->{
            boolean result = context().nary().equals(Nary.of(3));
            assertThat(result).isTrue();
          });
          it("is false if the other optional has different value",()->{
            boolean result = context().nary().equals(Nary.of(1));
            assertThat(result).isFalse();
          });
          it("is false if the other optional is empty",()->{
            boolean result = context().nary().equals(Nary.empty());
            assertThat(result).isFalse();
          });
        });
        it("returns the same hashcode as a list with the same element",()->{
          assertThat(context().nary().hashCode()).isEqualTo(Lists.newArrayList(3).hashCode());
        });
        it("returns a one element nary representation when #toString() is called",()->{
          assertThat(context().nary().toString()).isEqualTo("OneElementNary{ 3 }");
        });
        it("returns an equivalent optional when #asNativeOptional() is called",()->{
          assertThat(context().nary().asOptional()).isEqualTo(java.util.Optional.of(3));
        });
        it("returns a one element container when #collect(supplier, accumulator) is called",()->{
          List<Integer> result = context().nary().collect(ArrayList::new, ArrayList::add);
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns a one element stream when #asStream() is called",()->{
          List<Integer> result = context().nary().asStream().collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        describe("#concatStream", () -> {
          it("returns a one element nary if the stream is empty",()->{
            List<Integer> result = context().nary().concatStream(Nary.empty())
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns a nary with the value and the stream elements if the stream is not empty",()->{
            List<Integer> result = context().nary().concatStream(Nary.of(1,2, 3))
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 1, 2, 3));
          });
        });

        it("applies the action to the value when #peekNary() is called", () -> {
          Variable<Object> variable = Variable.create();
          List<Integer> result = context().nary().peekNary(variable::set)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
          assertThat(variable.get()).isEqualTo(3);
        });
        it("applies the predicate to filter when #filterNary() is called",()->{
          List<Integer> result = context().nary().filterNary((value) -> value.equals(3))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("transforms the value when #mapNary() is called",()->{
          List<Integer> result = context().nary().mapNary((value) -> value + 1)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(4));
        });
        it("transforms the value when #flatMapNary() is called",()->{
          List<Integer> result = context().nary().flatMapNary((value) -> Nary.of(8))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(8));
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

      describe("as one element stream", () -> {
        it("applies the predicate to filter when #filter() is called",()->{
          List<Integer> result = context().nary().filter((value) -> value.equals(3))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("transforms the value when #map() is called",()->{
          List<Integer> result = context().nary().map((value) -> value + 1)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(4));
        });
        it("transforms the value to int when #mapToInt() is called",()->{
          List<Integer> result = context().nary().mapToInt((value) -> value + 2)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(5));
        });
        it("transforms the value to long when #mapToLong() is called",()->{
          List<Long> result = context().nary().mapToLong((value) -> value + 3)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(6L));
        });
        it("transforms the value to double when #mapToDouble() is called",()->{
          List<Double> result = context().nary().mapToDouble((value) -> value + 4)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(7.0));
        });
        it("transforms the value when #flatMap() is called",()->{
          List<Integer> result = context().nary().flatMap((value) -> Nary.of(8))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(8));
        });
        it("transforms the value when #flatMapToInt() is called",()->{
          List<Integer> result = context().nary().flatMapToInt((value) -> IntStream.of(value + 2))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(5));
        });
        it("transforms the value when #flatMapToLongs() is called",()->{
          List<Long> result = context().nary().flatMapToLong((value) -> LongStream.of(value + 3))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(6L));
        });
        it("transforms the value when #flatMapToDouble() is called",()->{
          List<Double> result = context().nary().flatMapToDouble((value) -> DoubleStream.of(value + 4))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

          assertThat(result).isEqualTo(Lists.newArrayList(7.0));
        });
        it("returns same stream when #distinct() is called",()->{
          List<Integer> result = context().nary().distinct()
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns same stream when #sorted() is called",()->{
          List<Integer> result = context().nary().sorted()
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns same stream when #sorted(Comparator) is called",()->{
          List<Integer> result = context().nary().sorted(Integer::compareTo)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("calls the consumer argument when #peek() is called",()->{
          Variable<Boolean> executed = Variable.of(false);
          List<Integer> result = context().nary().peek((value)-> executed.set(true))
            .collect(Collectors.toList());

          assertThat(executed.get()).isTrue();
        });
        describe("#limit", () -> {
          it("returns the same stream if argument is bigger than 0",()->{
            List<Integer> result = context().nary().limit(2)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns an empty stream if argument is 0",()->{
            List<Integer> result = context().nary().limit(0)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList());
          });
        });
        describe("#skip", () -> {
          it("returns the same stream if argument is 0",()->{
            List<Integer> result = context().nary().skip(0)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("returns an empty stream if argument is bigger than 0",()->{
            List<Integer> result = context().nary().skip(1)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList());
          });
        });
        it("calls the consumer once when #forEach() is called",()->{
          Variable<Integer> accumulated = Variable.of(0);

          context().nary().forEach((value)-> accumulated.set(accumulated.get() + 1));

          assertThat(accumulated.get()).isEqualTo(1);
        });
        it("calls the consumer when #forEachOrdered() is called",()->{
          Variable<Boolean> executed = Variable.of(false);

          context().nary().forEachOrdered((value)-> executed.set(true));

          assertThat(executed.get()).isTrue();
        });
        it("returns a one element array when called to #toArray()",()->{
          Object[] result = context().nary().toArray();

          assertThat(result).isEqualTo(new Object[]{3});
        });
        it("calls the with 1 as size the intFunction argument of #toArray(IntFunction)",()->{
          Integer[] result = context().nary().toArray(Integer[]::new);

          assertThat(result).isEqualTo(new Integer[]{3});
        });
        it("accumulates the value with the identity when #reduce(identity,accumulator) is called",()->{
          Integer result = context().nary().reduce(23, (a, b) -> a + b);
          assertThat(result).isEqualTo(26);
        });
        it("ignores the combiner when #reduce(identity,accumulator, combiner) is called",()->{
          Integer result = context().nary().reduce(28, (a, b) -> a + b, (a, b)-> a+b);
          assertThat(result).isEqualTo(31);
        });
        it("returns the value when #reduce(accumulator) is called",()->{
          java.util.Optional<Integer> result = context().nary().reduce((a, b) -> a + b);
          assertThat(result.get()).isEqualTo(3);
        });
        it("accumulates the value on the container when #collect(supplier, accumulator, combiner) is called ",()->{
          List<Integer> result = context().nary().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("collects the value when #collect(collector) is called",()->{
          List<Integer> result = context().nary().collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });
        it("returns the native optional when #min() is called",()->{
          java.util.Optional<Integer> result = context().nary().min(Integer::compareTo);
          assertThat(result).isEqualTo(java.util.Optional.of(3));
        });
        it("returns the native optional when #max() is called",()->{
          java.util.Optional<Integer> result = context().nary().max(Integer::compareTo);
          assertThat(result).isEqualTo(java.util.Optional.of(3));
        });
        it("returns 1 when #count() is called",()->{
          long result = context().nary().count();
          assertThat(result).isEqualTo(1);
        });
        it("applies the predicate on the value and returns its result when #anyMatch() is called",()->{
          boolean result = context().nary().anyMatch((value)-> value.equals(3));
          assertThat(result).isTrue();
        });
        it("applies the predicate on the value and returns its result when #allMatch() is called",()->{
          boolean result = context().nary().allMatch((value)-> value.equals(4));
          assertThat(result).isFalse();
        });
        it("applies the predicate on the value and returns its negation when #noneMatch() is called",()->{
          boolean result = context().nary().noneMatch((value)-> value.equals(3));
          assertThat(result).isFalse();
        });
        it("returns the native optional when #findFirst() is called",()->{
          java.util.Optional<Integer> result = context().nary().findFirst();
          assertThat(result).isEqualTo(java.util.Optional.of(3));
        });
        it("returns the native optional when #findAny() is called",()->{
          java.util.Optional<Integer> result = context().nary().findAny();
          assertThat(result).isEqualTo(java.util.Optional.of(3));
        });

      });

      describe("as one element nary", () -> {

        it("returns itself when #findLast() is called",()->{
          List<Integer> result = context().nary().findLast()
            .collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList(3));
        });

        it("returns the value when #reduceNary(accumulator) is called",()->{
          Nary<Integer> result = context().nary().reduceNary((a, b) -> a + b);
          assertThat(result.get()).isEqualTo(3);
        });

        it("returns itself when #findFirstNary() is called",()->{
          Nary<Integer> result = context().nary().findFirstNary();
          assertThat((Object)result).isEqualTo(Nary.of(3));
        });
        it("returns itself when #findAnyNary() is called",()->{
          Nary<Integer> result = context().nary().findAnyNary();
          assertThat((Object)result).isEqualTo(Nary.of(3));
        });
        it("returns itself when #minNary() is called",()->{
          Nary<Integer> result = context().nary().minNary(Integer::compareTo);
          assertThat((Object)result).isEqualTo(Nary.of(3));
        });
        it("returns itself when #maxNary() is called",()->{
          Nary<Integer> result = context().nary().maxNary(Integer::compareTo);
          assertThat((Object)result).isEqualTo(Nary.of(3));
        });

      });

    });

  }
}