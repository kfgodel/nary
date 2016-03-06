package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.*;

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

      it("behaves like an empty nary when the stream is empty",()->{
        Nary<Object> nary = Nary.create(Stream.empty());
        assertThat(nary).isEqualTo(Nary.empty());
      });
      
      it("behaves like an optional based nary when the stream has only one element",()->{
        Nary<Integer> nary = Nary.create(Stream.of(1));
        assertThat(nary).isEqualTo(Nary.create(Optional.of(1)));
      });   

      describe("when having more than one element", () -> {
        context().nary(()-> Nary.create(Stream.of(3,2,1,3)));

        describe("as overflowed optional", () -> {
          it("throws an exception when get() is called",()->{
            try{
              context().nary().get();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when #isPresent() is called",()->{
            try{
              context().nary().isPresent();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when #isAbsent() is called",()->{
            try{
              context().nary().isAbsent();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when #ifPresent() is called",()->{
            try{
              context().nary().ifPresent((value)-> { throw new RuntimeException("never happens");});
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when #ifAbsent() is called",()->{
            try{
              context().nary().ifAbsent(()-> { throw new RuntimeException("never happens");});
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when called to #filterOptional()",()->{
            try{
              context().nary().filterOptional((value)-> { throw new RuntimeException("never happens");});
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when called to #mapOptional()",()->{
            try{
              context().nary().mapOptional((value)-> { throw new RuntimeException("never happens");});
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception  when called to #flatmapOptional()",()->{
            try{
              context().nary().flatMapOptional((value)-> { throw new RuntimeException("never happens");});
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when #orElse() is called",()->{
            try{
              context().nary().orElse(200);
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when #orElseGet() is called",()->{
            try{
              context().nary().orElseGet(()-> { throw new RuntimeException("never happens");});
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("throws an exception when #orElseThrow() is called",()->{
            try{
              context().nary().orElseThrow(()-> new RuntimeException("never happens"));
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          describe("#equals", () -> {
            it("is true if other stream has the same values and order",()->{
              boolean result = context().nary().equals(Nary.of(3, 2, 1, 3));
              assertThat(result).isTrue();
            });
            it("is false if the other stream has different values",()->{
              boolean result = context().nary().equals(Nary.of(4, 5));
              assertThat(result).isFalse();
            });
            it("is false if the other stream has different order",()->{
              boolean result = context().nary().equals(Nary.of(1, 2, 3, 3));
              assertThat(result).isFalse();
            });
            it("is false if the other optional is empty",()->{
              boolean result = context().nary().equals(Nary.empty());
              assertThat(result).isFalse();
            });
          });
          it("returns the same hashcode as a list with the same elements",()->{
            assertThat(context().nary().hashCode()).isEqualTo(Lists.newArrayList(3, 2, 1, 3).hashCode());
          });
          it("returns a nary representation when #toString() is called",()->{
            assertThat(context().nary().toString())
              .startsWith("NaryFromNative{stream: java.util.stream.ReferencePipeline")
              .endsWith(", optional: null}");
          });
          it("throws an exception when #asNativeOptional() is called",()->{
            try{
              context().nary().asNativeOptional();
              failBecauseExceptionWasNotThrown(MoreThanOneElementException.class);
            }catch (MoreThanOneElementException e){
              assertThat(e).hasMessage("There's more than one element in the stream to create an optional: [3, 2]");
            }
          });
          it("returns a container with the values when #collect(supplier, accumulator) is called",()->{
            List<Integer> result = context().nary().collect(ArrayList::new, ArrayList::add);
            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("returns the native stream when #asStream() is called",()->{
            List<Integer> result = context().nary().asStream().collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          describe("#concatOptional", () -> {
            it("returns itself if the other optional is empty",()->{
              List<Integer> result = context().nary().concatOptional(Nary.empty())
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
            });
            it("returns a nary with the extra element if the other optional is not empty",()->{
              List<Integer> result = context().nary().concatOptional(Nary.of(4))
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3, 4));
            });
          });
          describe("#concatStream", () -> {
            it("returns itself if the stream is empty",()->{
              List<Integer> result = context().nary().concatStream(Nary.empty())
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
            });
            it("returns a nary with concatenated elements if the stream is not empty",()->{
              List<Integer> result = context().nary().concatOptional(Nary.of(4, 5, 6))
                .collect(Collectors.toList());
              assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3, 4, 5, 6));
            });
          });

          it("returns itself when #asNary() is called",()->{
            List<Integer> result = context().nary().asNary().collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3,2,1,3));
          });
          it("applies the predicate to filter when #filterNary() is called",()->{
            List<Integer> result = context().nary().filterNary((value) -> value.equals(3))
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 3));
          });
          it("transforms the values when #mapNary() is called",()->{
            List<Integer> result = context().nary().mapNary((value) -> value + 1)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(4, 3, 2, 4));
          });
          it("transforms the values when #flatMapNary() is called",()->{
            List<Integer> result = context().nary().flatMapNary((value) -> Nary.of(8))
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(8, 8, 8, 8));
          });

        });

        describe("as a normal stream", () -> {
          it("applies the predicate to filter when #filter() is called",()->{
            List<Integer> result = context().nary().filter((value) -> value.equals(3))
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 3));
          });
          it("transforms the values when #map() is called",()->{
            List<Integer> result = context().nary().map((value) -> value + 1)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(4, 3, 2, 4));
          });
          it("transforms the values to int when #mapToInt() is called",()->{
            List<Integer> result = context().nary().mapToInt((value) -> value + 2)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(5, 4, 3, 5));
          });
          it("transforms the values to long when #mapToLong() is called",()->{
            List<Long> result = context().nary().mapToLong((value) -> value + 3)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(6L, 5L, 4L, 6L));
          });
          it("transforms the values to double when #mapToDouble() is called",()->{
            List<Double> result = context().nary().mapToDouble((value) -> value + 4)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(7.0, 6.0, 5.0, 7.0));
          });
          it("transforms the values when #flatMap() is called",()->{
            List<Integer> result = context().nary().flatMap((value) -> Nary.of(8))
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(8, 8, 8, 8));
          });
          it("transforms the values when #flatMapToInt() is called",()->{
            List<Integer> result = context().nary().flatMapToInt((value) -> IntStream.of(value + 2))
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(5, 4, 3, 5));
          });
          it("transforms the value when #flatMapToLongs() is called",()->{
            List<Long> result = context().nary().flatMapToLong((value) -> LongStream.of(value + 3))
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(6L, 5L, 4L, 6L));
          });
          it("transforms the value when #flatMapToDouble() is called",()->{
            List<Double> result = context().nary().flatMapToDouble((value) -> DoubleStream.of(value + 4))
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            assertThat(result).isEqualTo(Lists.newArrayList(7.0, 6.0, 5.0, 7.0));
          });
          it("returns a stream without duplicates when #distinct() is called",()->{
            List<Integer> result = context().nary().distinct()
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1));
          });
          it("returns a naturally ordered stream when #sorted() is called",()->{
            List<Integer> result = context().nary().sorted()
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3, 3));
          });
          it("returns a comparator base ordered stream when #sorted(Comparator) is called",()->{
            List<Integer> result = context().nary().sorted(Integer::compareTo)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3, 3));
          });
          it("calls the consumer argument when #peek() is called",()->{
            List<Integer> peeked = new ArrayList<Integer>();
            context().nary().peek(peeked::add)
              .collect(Collectors.toList());

            assertThat(peeked).isEqualTo(Lists.newArrayList(3,2,1,3));
          });
          it("returns a stream with only the first N elements when called to #limit()",()->{
            List<Integer> result = context().nary().limit(2)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(3, 2));
          });
          it("returns a stream without the first N elements when called to #skip()",()->{
            List<Integer> result = context().nary().skip(2)
              .collect(Collectors.toList());

            assertThat(result).isEqualTo(Lists.newArrayList(1, 3));
          });
          it("calls the consumer for each value when #forEach() is called",()->{
            List<Integer> list = new ArrayList<Integer>();
            context().nary().forEach(list::add);

            assertThat(list).isEqualTo(Lists.newArrayList(3,2,1,3));
          });
          it("calls the consumer for each value in the stream order when #forEachOrdered() is called",()->{
            List<Integer> list = new ArrayList<Integer>();

            context().nary().forEachOrdered(list::add);

            assertThat(list).isEqualTo(Lists.newArrayList(3,2,1,3));
          });
          it("returns an object with the values array when called to #toArray()",()->{
            Object[] result = context().nary().toArray();

            assertThat(result).isEqualTo(new Object[]{3, 2, 1, 3});
          });
          it("calls the with stream count as size the intFunction argument of #toArray(IntFunction)",()->{
            Integer[] result = context().nary().toArray(Integer[]::new);

            assertThat(result).isEqualTo(new Integer[]{3,2,1,3});
          });
          it("honors the contract when #reduce(identity,accumulator) is called",()->{
            Integer result = context().nary().reduce(1, (a, b) -> a + b);
            assertThat(result).isEqualTo(1 + 3 + 2 + 1 +3);
          });
          it("honors the contract when #reduce(identity,accumulator, combiner) is called",()->{
            Integer result = context().nary().reduce(2, (a, b) -> a + b, (a, b)-> a+b);
            assertThat(result).isEqualTo(2 + 3 + 2 + 1 +3);
          });
          it("honors the contract when #reduce(accumulator) is called",()->{
            java.util.Optional<Integer> result = context().nary().reduce((a, b) -> a + b);
            assertThat(result.get()).isEqualTo(3 + 2 + 1 +3);
          });
          it("honors the contract when #collect(supplier, accumulator, combiner) is called ",()->{
            List<Integer> result = context().nary().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            assertThat(result).isEqualTo(Lists.newArrayList(3,2,1,3));
          });
          it("honors the contract when #collect(collector) is called",()->{
            List<Integer> result = context().nary().collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3, 2, 1, 3));
          });
          it("honors the contract when #min() is called",()->{
            java.util.Optional<Integer> result = context().nary().min(Integer::compareTo);
            assertThat(result).isEqualTo(java.util.Optional.of(1));
          });
          it("honors the contract when #max() is called",()->{
            java.util.Optional<Integer> result = context().nary().max(Integer::compareTo);
            assertThat(result).isEqualTo(java.util.Optional.of(3));
          });
          it("returns the amount of values when #count() is called",()->{
            long result = context().nary().count();
            assertThat(result).isEqualTo(4);
          });
          it("honors the contract when #anyMatch() is called",()->{
            boolean result = context().nary().anyMatch((value)-> value.equals(1));
            assertThat(result).isTrue();
          });
          it("honors the contract when #allMatch() is called",()->{
            boolean result = context().nary().allMatch((value)-> value > 1);
            assertThat(result).isFalse();
          });
          it("honors the contract when #noneMatch() is called",()->{
            boolean result = context().nary().noneMatch((value)-> value.equals(4));
            assertThat(result).isTrue();
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


        describe("as nary with elements", () -> {

          it("returns the last element when #findLast() is called",()->{
            List<Integer> result = context().nary().findLast()
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });
          it("honors the contract when #reduceNary(accumulator) is called",()->{
            Nary<Integer> result = context().nary().reduceNary((a, b) -> a + b);
            assertThat(result.get()).isEqualTo(3 + 2 + 1 +3);
          });
          it("honors the contract when #findFirst() is called",()->{
            Nary<Integer> result = context().nary().findFirstNary();
            assertThat(result).isEqualTo(Nary.of(3));
          });
          it("honors the contract when #findAny() is called",()->{
            Nary<Integer> result = context().nary().findAnyNary();
            assertThat(result).isEqualTo(Nary.of(3));
          });
          it("honors the contract when #minNary() is called",()->{
            Nary<Integer> result = context().nary().minNary(Integer::compareTo);
            assertThat(result).isEqualTo(Nary.of(1));
          });
          it("honors the contract when #maxNary() is called",()->{
            Nary<Integer> result = context().nary().maxNary(Integer::compareTo);
            assertThat(result).isEqualTo(Nary.of(3));
          });

        });
      });
    });
  }
}