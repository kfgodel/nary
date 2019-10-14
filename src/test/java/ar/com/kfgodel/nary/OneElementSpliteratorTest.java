package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.impl.others.OneElementSpliterator;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.variable.Variable;
import org.junit.runner.RunWith;

import static java.util.Spliterator.CONCURRENT;
import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SIZED;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of an empty spliterator
 * Created by kfgodel on 07/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class OneElementSpliteratorTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an empty spliterator", () -> {
      context().spliterator(() -> OneElementSpliterator.create(1));

      it("can advance once", () -> {
        boolean result = context().spliterator().tryAdvance(this::noOp);

        assertThat(result).isTrue();
      });

      it("executes the consumer argument", () -> {
        Variable<Boolean> executed = Variable.of(false);

        context().spliterator().tryAdvance((value) -> executed.set(true));

        assertThat(executed.get()).isTrue();
      });

      it("returns null when split", () -> {
        assertThat(context().spliterator().trySplit()).isNull();
      });

      it("has 1 estimated size", () -> {
        assertThat(context().spliterator().estimateSize()).isEqualTo(1);
      });

      it("has 1 exact size", () -> {
        assertThat(context().spliterator().getExactSizeIfKnown()).isEqualTo(1);
      });

      it("has several characteristics to help combination with other spliterators", () -> {
        assertThat(context().spliterator().characteristics())
          .isEqualTo(ORDERED | DISTINCT | SIZED | NONNULL | IMMUTABLE | CONCURRENT);
      });

      it("executes the consumer argument once when #forEachRemaining() called", () -> {
        Variable<Integer> accumulator = Variable.of(0);

        context().spliterator().forEachRemaining((value) -> accumulator.set(accumulator.get() + 1));

        assertThat(accumulator.get()).isEqualTo(1);
      });


      describe("once advanced", () -> {
        beforeEach(() -> {
          context().spliterator().tryAdvance(this::noOp);
        });

        it("cannot advance", () -> {
          boolean result = context().spliterator().tryAdvance((value) -> {
            throw new RuntimeException("never happens");
          });

          assertThat(result).isFalse();
        });

        it("doesn't execute the consumer argument", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().spliterator().tryAdvance((value) -> executed.set(true));

          assertThat(executed.get()).isFalse();
        });

        it("has 0 estimated size", () -> {
          assertThat(context().spliterator().estimateSize()).isEqualTo(0);
        });

        it("has 0 exact size", () -> {
          assertThat(context().spliterator().getExactSizeIfKnown()).isEqualTo(0);
        });

        it("ignores the consumer argument when #forEachRemaining() called", () -> {
          Variable<Boolean> executed = Variable.of(false);

          context().spliterator().forEachRemaining((value) -> executed.set(true));

          assertThat(executed.get()).isFalse();
        });

      });

    });

  }

  private void noOp(Integer integer) {
    // Nothing to do
  }
}