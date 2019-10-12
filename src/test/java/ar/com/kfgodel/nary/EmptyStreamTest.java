package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.others.EmptyStream;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.variable.Variable;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of an empty stream
 * Created by kfgodel on 07/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class EmptyStreamTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an empty stream", () -> {
      context().stream(EmptyStream::instance);

      it("returns an empty stream when #filter() is called",()->{
        List<Integer> result = context().stream().filter((value) -> true)
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("returns an empty stream when #map() is called",()->{
        List<Integer> result = context().stream().map((value) -> value)
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("returns an empty stream when #mapToInt() is called",()->{
        long result = context().stream().mapToInt((value) -> 1).count();

        assertThat(result).isEqualTo(0);
      });
      it("returns an empty stream when #mapToLong() is called",()->{
        long result = context().stream().mapToLong((value) -> 1).count();

        assertThat(result).isEqualTo(0);
      });
      it("returns an empty stream when #mapToDouble() is called",()->{
        long result = context().stream().mapToDouble((value) -> 1.0).count();

        assertThat(result).isEqualTo(0);
      });
      it("returns an empty stream when #flatMap() is called",()->{
        List<Integer> result = context().stream().flatMap((value) -> Nary.of(value))
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("returns an empty stream when #flatMapToInt() is called",()->{
        long result = context().stream().flatMapToInt((value) -> IntStream.of(1))
          .count();

        assertThat(result).isEqualTo(0);
      });
      it("returns an empty stream when #flatMapToLongs() is called",()->{
        long result = context().stream().flatMapToLong((value) -> LongStream.of(1))
          .count();

        assertThat(result).isEqualTo(0);
      });
      it("returns an empty stream when #flatMapToDouble() is called",()->{
        long result = context().stream().flatMapToDouble((value) -> DoubleStream.of(1))
          .count();

        assertThat(result).isEqualTo(0);
      });
      it("returns an empty stream when #distinct() is called",()->{
        List<Integer> result = context().stream().distinct()
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("returns an empty stream when #sorted() is called",()->{
        List<Integer> result = context().stream().sorted()
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("returns an empty stream when #sorted(Comparator) is called",()->{
        List<Integer> result = context().stream().sorted(Integer::compareTo)
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("never calls the consumer when #peek() is called",()->{
        Variable<Boolean> executed = Variable.of(false);
        List<Integer> result = context().stream().peek((value)-> executed.set(true))
          .collect(Collectors.toList());

        assertThat(executed.get()).isFalse();
      });
      it("ignores calls to #limit() returning an empty stream",()->{
        List<Integer> result = context().stream().limit(20)
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("ignores calls to #skip() returning an empty stream",()->{
        List<Integer> result = context().stream().skip(0)
          .collect(Collectors.toList());

        assertThat(result).isEqualTo(Lists.newArrayList());
      });
      it("never calls the consumer when #forEach() is called",()->{
        Variable<Boolean> executed = Variable.of(false);

        context().stream().forEach((value)-> executed.set(true));

        assertThat(executed.get()).isFalse();
      });
      it("never calls the consumer when #forEachOrdered() is called",()->{
        Variable<Boolean> executed = Variable.of(false);

        context().stream().forEachOrdered((value)-> executed.set(true));

        assertThat(executed.get()).isFalse();
      });
      it("returns an empty object array when called to #toArray()",()->{
        Object[] result = context().stream().toArray();

        assertThat(result).isEqualTo(new Object[0]);
      });
      it("calls with 0 as size the intFunction argument of #toArray(IntFunction)",()->{
        Variable<Integer> sizeUsed = Variable.create();

        context().stream().toArray((size)->{
          sizeUsed.set(size);
          return new Object[size];
        });

        assertThat(sizeUsed.get()).isEqualTo(0);
      });
      it("returns the identity when #reduce(identity,accumulator) is called",()->{
        Integer result = context().stream().reduce(23, (a, b) -> a + b);
        assertThat(result).isEqualTo(23);
      });
      it("returns the identity when #reduce(identity,accumulator, combiner) is called",()->{
        Integer result = context().stream().reduce(28, (a, b) -> a + b, (a, b)-> a+b);
        assertThat(result).isEqualTo(28);
      });
      it("returns an empty optional when #reduce(accumulator) is called",()->{
        java.util.Optional<Integer> result = context().stream().reduce((a, b) -> a + b);
        assertThat(result.isPresent()).isFalse();
      });
      it("returns the supplier result untouched when #collect(supplier, accumulator, combiner) is called ",()->{
        List<Integer> result = context().stream().collect(ArrayList::new, (list, value)-> list.add(value), (list1, list2)-> list1.addAll(list2) );
        assertThat(result).isEmpty();
      });
      it("returns an empty collection when #collect(collector) is called",()->{
        List<Integer> result = context().stream().collect(Collectors.toList());
        assertThat(result).isEmpty();
      });
      it("returns an empty optional when #min() is called",()->{
        java.util.Optional<Integer> result = context().stream().min(Integer::compareTo);
        assertThat(result).isEqualTo(java.util.Optional.empty());
      });
      it("returns an empty optional when #max() is called",()->{
        java.util.Optional<Integer> result = context().stream().max(Integer::compareTo);
        assertThat(result).isEqualTo(java.util.Optional.empty());
      });
      it("returns 0 when #count() is called",()->{
        long result = context().stream().count();
        assertThat(result).isEqualTo(0);
      });
      it("returns false when #anyMatch() is called",()->{
        boolean result = context().stream().anyMatch((value)-> true);
        assertThat(result).isFalse();
      });
      it("returns true when #allMatch() is called",()->{
        boolean result = context().stream().allMatch((value)-> true);
        assertThat(result).isTrue();
      });
      it("returns true when #noneMatch() is called",()->{
        boolean result = context().stream().noneMatch((value)-> true);
        assertThat(result).isTrue();
      });
      it("returns an empty optional when #findFirst() is called",()->{
        java.util.Optional<Integer> result = context().stream().findFirst();
        assertThat(result).isEqualTo(java.util.Optional.empty());
      });
      it("returns an empty optional when #findAny() is called",()->{
        java.util.Optional<Integer> result = context().stream().findAny();
        assertThat(result).isEqualTo(java.util.Optional.empty());
      });

    });

  }
}