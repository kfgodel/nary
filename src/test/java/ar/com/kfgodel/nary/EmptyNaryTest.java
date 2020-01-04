package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.Unary;
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
      context().unary(Nary::empty);

      describe("as optional", () -> {
        it("throws an exception if get() is called", () -> {
          try {
            context().unary().asUni().get();
            failBecauseExceptionWasNotThrown(NoSuchElementException.class);
          } catch (NoSuchElementException e) {
            assertThat(e).hasMessage("Can't call get() on an empty nary: No value present");
          }
        });
        it("answers false to #isPresent()", () -> {
          assertThat(context().unary().isPresent()).isFalse();
        });
        it("answers true to #isAbsent()", () -> {
          assertThat(context().unary().isAbsent()).isTrue();
        });
        it("nevers executes the #ifPresent() argument", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().unary().asUni().ifPresent((value) -> executed.set(true));

          assertThat(executed.get()).isFalse();
        });
        it("always executes the #ifAbsent() argument", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().unary().ifAbsent(() -> executed.set(true));

          assertThat(executed.get()).isTrue();
        });
        it("always returns an empty optional when called to #mapFilteringNullResult()", () -> {
          Nary<Integer> result = context().unary().mapFilteringNullResult((value) -> value);

          assertThat(result.asUni().isAbsent()).isTrue();
        });
        it("returns an empty stream when #map() is called", () -> {
          List<Integer> result = context().unary().map((value) -> value)
            .collectToList();

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("returns an empty stream when #flatMap() is called", () -> {
          List<Integer> result = context().unary().flatMap((value) -> Nary.ofNonNullable(value))
            .collectToList();

          assertThat(result).isEqualTo(Lists.newArrayList());
        });
        it("always returns an empty optional when called to #flatmapOptional()", () -> {
          Nary<Integer> result = context().unary().flatMapOptional(Optional::of);

          assertThat(result.asUni().isAbsent()).isTrue();
        });
        it("always returns the alternative value when #orElse() is called", () -> {
          Integer result = context().unary().orElse(4);
          assertThat(result).isEqualTo(4);
        });
        it("always executes the supplier argument when #orElseGet() is called", () -> {
          Integer result = context().unary().orElseGet(() -> 4);
          assertThat(result).isEqualTo(4);
        });
        it("always uses the supplier argument when #orElseUse() is called", () -> {
          Nary<Integer> result = context().unary().orElseUse(() -> 4);
          assertThat(result.asUni().get()).isEqualTo(4);
        });
        it("always throws the exception when #orElseThrow() is called", () -> {
          try {
            context().unary().orElseThrow(() -> new RuntimeException("Kaboom"));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
          } catch (RuntimeException e) {
            assertThat(e).hasMessage("Kaboom");
          }
        });
        it("always throws the exception when #orElseThrowRuntime() is called", () -> {
          try {
            context().unary().orElseThrowRuntime(() -> new RuntimeException("Kaboom"));
            failBecauseExceptionWasNotThrown(RuntimeException.class);
          } catch (RuntimeException e) {
            assertThat(e).hasMessage("Kaboom");
          }
        });
        describe("#equals", () -> {
          it("is true if other empty optional is passed", () -> {
            boolean result = context().unary().equals(Nary.empty());
            assertThat(result).isTrue();
          });
          it("is false if a non empty optional is passed", () -> {
            boolean result = context().unary().equals(Nary.ofNonNullable(1));
            assertThat(result).isFalse();
          });
        });
        it("always returns 1 when #hashcode() is called, as an empty list", () -> {
          assertThat(context().unary().hashCode()).isEqualTo(new ArrayList<>().hashCode());
        });
        it("returns an empty nary representation when #toString() is called", () -> {
          assertThat(context().unary().toString()).isEqualTo("EmptyNary");
        });
        it("always returns the empty optional when asOptional() is called", () -> {
          assertThat(context().unary().asOptional()).isEqualTo(Optional.empty());
        });
        it("always returns the empty unary when asUni() is called", () -> {
          assertThat((Stream)context().unary().asUni()).isEmpty();
        });
        it("always returns an empty container when #collect(supplier, accumulator) is called", () -> {
          List<Object> result = context().unary().collect(ArrayList::new, ArrayList::add);
          assertThat(result).isEmpty();
        });
        it("returns an empty stream when #asStream() is called", () -> {
          List<Integer> result = context().unary().asStream().collect(Collectors.toList());
          assertThat(result).isEmpty();
        });
        describe("#concat(Stream)", () -> {
          it("returns an empty nary if the stream is empty", () -> {
            List<Integer> result = context().unary().concat(Nary.empty())
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList());
          });
          it("returns a nary with the stream elements if the stream is not empty", () -> {
            List<Integer> result = context().unary().concat(Nary.ofNonNullable(1, 2, 3))
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3));
          });
        });
        describe("#concat(Optional)", () -> {
          it("returns an empty nary if the Optional is empty", () -> {
            List<Integer> result = context().unary().concat(Optional.empty())
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList());
          });
          it("returns a nary with the element from the Optional if it is not empty", () -> {
            List<Integer> result = context().unary().concat(Optional.of(1))
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(1));
          });
        });
        describe("#add", () -> {
          it("returns an empty nary if no arguments are passed", () -> {
            List<Integer> result = context().unary().add()
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList());
          });
          it("returns a nary with the elements from passed as arguments", () -> {
            List<Integer> result = context().unary().add(1, 2, 3)
              .collect(Collectors.toList());
            assertThat(result).isEqualTo(Lists.newArrayList(1, 2, 3));
          });
        });

        it("returns an empty nary when #peek() is called", () -> {
          Variable<Integer> variable = Variable.create();
          List<Integer> result = context().unary().peek(newValue -> variable.set(1))
            .collect(Collectors.toList());

          assertThat(result).isEqualTo(Lists.newArrayList());
          assertThat(variable.get()).isNull();
        });
        it("returns an empty nary when #filter() is called", () -> {
          List<Integer> result = context().unary().filter((value) -> true)
            .collectToList();

          assertThat(result).isEqualTo(Lists.newArrayList());
        });

        it("returns an empty list when toList() is called", () -> {
          assertThat(context().unary().collectToList()).isEqualTo(Collections.emptyList());
        });
        it("returns an empty set when toSet() is called", () -> {
          assertThat(context().unary().collectToSet()).isEqualTo(Collections.emptySet());
        });

      });

      describe("as nary", () -> {

        it("always returns an empty nary when #findLast() is called", () -> {
          List<Integer> result = context().unary().findLast()
            .collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList());
        });

        it("returns an empty nary when #reduceNary(accumulator) is called", () -> {
          Unary<Integer> result = context().unary().reduceNary((a, b) -> a + b);
          assertThat(result.isPresent()).isFalse();
        });

        it("returns an empty nary when #findFirstNary() is called", () -> {
          Unary<Integer> result = context().unary().findFirstNary();
          assertThat(result.isAbsent()).isTrue();
        });
        it("returns an empty nary when #findAnyNary() is called", () -> {
          Unary<Integer> result = context().unary().findAnyNary();
          assertThat(result.isAbsent()).isTrue();
        });

        it("returns an empty nary when #minNary() is called", () -> {
          Nary<Integer> result = context().unary().minNary(Integer::compareTo);
          assertThat((Stream) result).isEqualTo(Nary.empty());
        });
        it("returns an empty nary when #maxNary() is called", () -> {
          Nary<Integer> result = context().unary().maxNary(Integer::compareTo);
          assertThat((Stream) result).isEqualTo(Nary.empty());
        });

      });
    });

  }
}