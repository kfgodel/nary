package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.dgarcia.javaspec.api.Variable;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.optionals.Optional;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This type tests the behavior of an empty nary
 * Created by kfgodel on 06/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class EmptyNaryTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an empty nary", () -> {
      context().nary(Nary::empty);

      describe("as optional", () -> {
        it("throws an exception if get() is called",()->{
            try{
              context().nary().get();
              failBecauseExceptionWasNotThrown(NoSuchElementException.class);
            }catch(NoSuchElementException e){
              assertThat(e).hasMessage("No value present");
            }
        });
        it("answers false to #isPresent()",()->{
            assertThat(context().nary().isPresent()).isFalse();
        });
        it("answers true to #isAbsent()",()->{
          assertThat(context().nary().isAbsent()).isTrue();
        });
        it("nevers executes the #ifPresent() argument",()->{
          Variable<Boolean> executed = Variable.of(false);

          context().nary().ifPresent((value)-> executed.set(true));

          assertThat(executed.get()).isFalse();
        });   
        it("always executes the #ifAbsent() argument",()->{
          Variable<Boolean> executed = Variable.of(false);

          context().nary().ifAbsent(()-> executed.set(true));

          assertThat(executed.get()).isTrue();
        });
        it("always returns an empty optional when called to #filterOptional()",()->{
          Optional<Integer> result = context().nary().filterOptional((value) -> true);

          assertThat(result.isAbsent()).isTrue();
        });
        it("always returns an empty optional when called to #mapOptional()",()->{
          Optional<Integer> result = context().nary().mapOptional((value) -> value);

          assertThat(result.isAbsent()).isTrue();
        });
        it("always returns an empty optional when called to #flatmapOptional()",()->{
          Optional<Integer> result = context().nary().flatMapOptional((value)-> Nary.of(value).asNativeOptional());

          assertThat(result.isAbsent()).isTrue();
        });
        it("always returns the alternative value when #orElse() is called",()->{
          Integer result = context().nary().orElse(4);
          assertThat(result).isEqualTo(4);
        });
        it("always executes the supplier argument when #orElseGet() is called",()->{
          Integer result = context().nary().orElseGet(()-> 4);
          assertThat(result).isEqualTo(4);
        });
        it("always throws the exception when #orElseThrow() is called",()->{
          try{
            context().nary().orElseThrow(()-> new RuntimeException("Kaboom"));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
          }catch(RuntimeException e){
            assertThat(e).hasMessage("Kaboom");
          }
        });
        describe("#equals", () -> {
          it("is true if other empty optional is passed",()->{
            boolean result = context().nary().equals(Nary.empty());
            assertThat(result).isTrue();
          }); 
          it("is false if a non empty optional is passed",()->{
            boolean result = context().nary().equals(Nary.of(1));
            assertThat(result).isFalse();
          });   
        });
        it("always returns 1 when #hashcode() is called, as an empty list",()->{
          assertThat(context().nary().hashCode()).isEqualTo(new ArrayList<>().hashCode());
        });
        it("returns an empty nary representation when #toString() is called",()->{
            assertThat(context().nary().toString()).isEqualTo("NaryFromNative{stream: null, optional: Optional.empty}");
        });
        it("always returns the empty optional when #asNativeOptional() is called",()->{
            assertThat(context().nary().asNativeOptional()).isEqualTo(java.util.Optional.empty());
        });   

      });

      describe("as stream", () -> {
        it("returns an empty stream when #filter() is called",()->{
          List<Integer> result = context().nary().filter((value) -> true)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("returns an empty stream when #map() is called",()->{
          List<Integer> result = context().nary().map((value) -> value)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("returns an empty stream when #mapToInt() is called",()->{
          long result = context().nary().mapToInt((value) -> 1).count();

          assertThat(result).isEqualTo(0);
        });
        it("returns an empty stream when #mapToLong() is called",()->{
          long result = context().nary().mapToLong((value) -> 1).count();

          assertThat(result).isEqualTo(0);
        });
        it("returns an empty stream when #mapToDouble() is called",()->{
          long result = context().nary().mapToDouble((value) -> 1.0).count();

          assertThat(result).isEqualTo(0);
        });
        it("returns an empty stream when #flatMap() is called",()->{
          List<Integer> result = context().nary().flatMap((value) -> Nary.of(value))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("returns an empty stream when #flatMapToInt() is called",()->{
          long result = context().nary().flatMapToInt((value) -> IntStream.of(1))
            .count();

          assertThat(result).isEqualTo(0);
        });
        it("returns an empty stream when #flatMapToLongs() is called",()->{
          long result = context().nary().flatMapToLong((value) -> LongStream.of(1))
            .count();

          assertThat(result).isEqualTo(0);
        });
        it("returns an empty stream when #flatMapToDouble() is called",()->{
          long result = context().nary().flatMapToDouble((value) -> DoubleStream.of(1))
            .count();

          assertThat(result).isEqualTo(0);
        });
        it("returns an empty stream when #distinct() is called",()->{
          List<Integer> result = context().nary().distinct()
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("returns an empty stream when #sorted() is called",()->{
          List<Integer> result = context().nary().sorted()
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("returns an empty stream when #sorted(Comparator) is called",()->{
          List<Integer> result = context().nary().sorted(Integer::compareTo)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("never calls the consumer when #peek() is called",()->{
          Variable<Boolean> executed = Variable.of(false);
          List<Integer> result = context().nary().peek((value)-> executed.set(true))
            .collect(Collectors.toList());

          assertThat(executed.get()).isFalse();
        });
        it("ignores calls to #limit() returning an empty stream",()->{
          List<Integer> result = context().nary().limit(20)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("ignores calls to #skip() returning an empty stream",()->{
          List<Integer> result = context().nary().skip(0)
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("never calls the consumer when #forEach() is called",()->{
          Variable<Boolean> executed = Variable.of(false);

          context().nary().forEach((value)-> executed.set(true));

          assertThat(executed.get()).isFalse();
        });
        it("never calls the consumer when #forEachOrdered() is called",()->{
          Variable<Boolean> executed = Variable.of(false);

          context().nary().forEachOrdered((value)-> executed.set(true));

          assertThat(executed.get()).isFalse();
        });
        it("returns an empty object array when called to #toArray()",()->{
          Object[] result = context().nary().toArray();

          assertThat(result).isEqualTo(new Object[0]);
        });
        it("calls with 0 as size the intFunction argument of #toArray(IntFunction)",()->{
          IntFunction function = mock(IntFunction.class);
          when(function.apply(0)).thenReturn(new Object[0]);

          context().nary().toArray(function);

          verify(function).apply(0);
        });
        it("returns the identity when #reduce(identity,accumulator) is called",()->{
          Integer result = context().nary().reduce(23, (a, b) -> a + b);
          assertThat(result).isEqualTo(23);
        });
        it("returns the identity when #reduce(identity,accumulator, combiner) is called",()->{
          Integer result = context().nary().reduce(28, (a, b) -> a + b, (a, b)-> a+b);
          assertThat(result).isEqualTo(28);
        });
        it("returns an empty optional when #reduce(accumulator) is called",()->{
          java.util.Optional<Integer> result = context().nary().reduce((a, b) -> a + b);
          assertThat(result.isPresent()).isFalse();
        });
        it("returns the supplier result untouched when #collect(supplier, accumulator, combiner) is called ",()->{
          List<Integer> result = context().nary().collect(ArrayList::new, (list, value)-> list.add(value), (list1, list2)-> list1.addAll(list2) );
          assertThat(result).isEmpty();
        });
        it("returns an empty collection when #collect(collector) is called",()->{
          List<Integer> result = context().nary().collect(Collectors.toList());
          assertThat(result).isEmpty();
        });
        it("returns an empty optional when #min() is called",()->{
          java.util.Optional<Integer> result = context().nary().min(Integer::compareTo);
          assertThat(result).isEqualTo(java.util.Optional.empty());
        });
        it("returns an empty optional when #max() is called",()->{
          java.util.Optional<Integer> result = context().nary().max(Integer::compareTo);
          assertThat(result).isEqualTo(java.util.Optional.empty());
        });
        it("returns 0 when #count() is called",()->{
          long result = context().nary().count();
          assertThat(result).isEqualTo(0);
        });
        it("returns false when #anyMatch() is called",()->{
          boolean result = context().nary().anyMatch((value)-> true);
          assertThat(result).isFalse();
        });
        it("returns true when #allMatch() is called",()->{
          boolean result = context().nary().allMatch((value)-> true);
          assertThat(result).isTrue();
        });
        it("returns true when #noneMatch() is called",()->{
          boolean result = context().nary().noneMatch((value)-> true);
          assertThat(result).isTrue();
        });
        it("returns an empty optional when #findFirst() is called",()->{
          java.util.Optional<Integer> result = context().nary().findFirst();
          assertThat(result).isEqualTo(java.util.Optional.empty());
        });
        it("returns an empty optional when #findAny() is called",()->{
          java.util.Optional<Integer> result = context().nary().findAny();
          assertThat(result).isEqualTo(java.util.Optional.empty());
        });


      });


    });


  }
}