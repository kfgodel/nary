package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.variable.Variable;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

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
        it("throws an exception if get() is called", () -> {
          try {
            context().nary().get();
            failBecauseExceptionWasNotThrown(NoSuchElementException.class);
          } catch (NoSuchElementException e) {
            assertThat(e).hasMessage("Can't call get() on an empty nary: No value present");
          }
        });
        it("answers false to #isPresent()", () -> {
          assertThat(context().nary().isPresent()).isFalse();
        });
        it("answers true to #isAbsent()", () -> {
          assertThat(context().nary().isAbsent()).isTrue();
        });
        it("nevers executes the #ifPresent() argument", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().nary().ifPresent((value) -> executed.set(true));

          assertThat(executed.get()).isFalse();
        });
        it("always executes the #ifAbsent() argument", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().nary().ifAbsent(() -> executed.set(true));

          assertThat(executed.get()).isTrue();
        });
        it("always returns an empty optional when called to #mapFilteringNullResult()", () -> {
          Nary<Integer> result = context().nary().mapFilteringNullResult((value) -> value);

          assertThat(result.isAbsent()).isTrue();
        });
        it("returns an empty stream when #map() is called", () -> {
          List<Integer> result = context().nary().map((value) -> value)
            .collectToList();

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("returns an empty stream when #flatMap() is called", () -> {
          List<Integer> result = context().nary().flatMap((value) -> Nary.ofNonNullable(value))
            .collectToList();

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("always returns an empty optional when called to #flatmapOptional()", () -> {
          Nary<Integer> result = context().nary().flatMapOptional((value) -> Nary.ofNonNullable(value).asOptional());

          assertThat(result.isAbsent()).isTrue();
        });
        it("always returns the alternative value when #orElse() is called", () -> {
          Integer result = context().nary().orElse(4);
          assertThat(result).isEqualTo(4);
        });
        it("always executes the supplier argument when #orElseGet() is called", () -> {
          Integer result = context().nary().orElseGet(() -> 4);
          assertThat(result).isEqualTo(4);
        });
        it("always uses the supplier argument when #orElseUse() is called", () -> {
          Nary<Integer> result = context().nary().orElseUse(() -> 4);
          assertThat(result.get()).isEqualTo(4);
        });
        it("always throws the exception when #orElseThrow() is called", () -> {
          try {
            context().nary().orElseThrow(() -> new RuntimeException("Kaboom"));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
          } catch (RuntimeException e) {
            assertThat(e).hasMessage("Kaboom");
          }
        });
        it("always throws the exception when #orElseThrowRuntime() is called", () -> {
          try {
            context().nary().orElseThrowRuntime(() -> new RuntimeException("Kaboom"));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
          } catch (RuntimeException e) {
            assertThat(e).hasMessage("Kaboom");
          }
        });
        describe("#equals", () -> {
          it("is true if other empty optional is passed", () -> {
            boolean result = context().nary().equals(Nary.empty());
            assertThat(result).isTrue();
          });
          it("is false if a non empty optional is passed", () -> {
            boolean result = context().nary().equals(Nary.ofNonNullable(1));
            assertThat(result).isFalse();
          });
        });
        it("always returns 1 when #hashcode() is called, as an empty list", () -> {
          assertThat(context().nary().hashCode()).isEqualTo(new ArrayList<>().hashCode());
        });
        it("returns an empty nary representation when #toString() is called", () -> {
          assertThat(context().nary().toString()).isEqualTo("EmptyNary");
        });
        it("always returns the empty optional when asOptional() is called", () -> {
          assertThat(context().nary().asOptional()).isEqualTo(Optional.empty());
        });
        it("always returns an empty container when #collect(supplier, accumulator) is called", () -> {
          List<Object> result = context().nary().collect(ArrayList::new, ArrayList::add);
          assertThat(result).isEmpty();
        });
        it("returns an empty stream when #asStream() is called", () -> {
          List<Integer> result = context().nary().asStream().collect(Collectors.toList());
          assertThat(result).isEmpty();
        });
        describe("#concat(Stream)", () -> {
          it("returns an empty nary if the stream is empty", () -> {
            List<Integer> result = context().nary().concat(Nary.empty())
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList());
          });
          it("returns a nary with the stream elements if the stream is not empty", () -> {
            List<Integer> result = context().nary().concat(Nary.ofNonNullable(1, 2, 3))
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3));
          });
        });
        describe("#concat(Optional)", () -> {
          it("returns an empty nary if the Optional is empty", () -> {
            List<Integer> result = context().nary().concat(Optional.empty())
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList());
          });
          it("returns a nary with the element from the Optional if it is not empty", () -> {
            List<Integer> result = context().nary().concat(Optional.of(1))
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(1));
          });
        });
        describe("#add", () -> {
          it("returns an empty nary if no arguments are passed", () -> {
            List<Integer> result = context().nary().add()
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList());
          });
          it("returns a nary with the elements from passed as arguments", () -> {
            List<Integer> result = context().nary().add(1, 2, 3)
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3));
          });
        });

        it("returns an empty nary when #peek() is called", () -> {
          Variable<Integer> variable = Variable.create();
          List<Integer> result = context().nary().peek(newValue -> variable.set(1))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
          assertThat(variable.get()).isNull();
        });
        it("returns an empty nary when #filter() is called", () -> {
          List<Integer> result = context().nary().filter((value) -> true)
            .collectToList();

          assertThat(result).isEqualTo(Lists.newArrayList());
        });

        it("returns an empty list when toList() is called", () -> {
          assertThat(context().nary().collectToList()).isEqualTo(Collections.emptyList());
        });
        it("returns an empty set when toSet() is called", () -> {
          assertThat(context().nary().collectToSet()).isEqualTo(Collections.emptySet());
        });

      });

      describe("as nary", () -> {

        it("always returns an empty nary when #findLast() is called", () -> {
          List<Integer> result = context().nary().findLast()
            .collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList());
        });

        it("returns an empty nary when #reduceNary(accumulator) is called", () -> {
          Nary<Integer> result = context().nary().reduceNary((a, b) -> a + b);
          assertThat(result.isPresent()).isFalse();
        });

        it("returns an empty nary when #findFirstNary() is called", () -> {
          Nary<Integer> result = context().nary().findFirstNary();
          assertThat(result.isAbsent()).isTrue();
        });
        it("returns an empty nary when #findAnyNary() is called", () -> {
          Nary<Integer> result = context().nary().findAnyNary();
          assertThat(result.isAbsent()).isTrue();
        });

        it("returns an empty nary when #minNary() is called", () -> {
          Nary<Integer> result = context().nary().minNary(Integer::compareTo);
          assertThat((Stream) result).isEqualTo(Nary.empty());
        });
        it("returns an empty nary when #maxNary() is called", () -> {
          Nary<Integer> result = context().nary().maxNary(Integer::compareTo);
          assertThat((Stream) result).isEqualTo(Nary.empty());
        });

      });
    });

  }
}