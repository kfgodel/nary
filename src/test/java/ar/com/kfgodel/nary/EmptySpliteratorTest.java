package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.impl.others.EmptySpliterator;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.variable.Variable;
import org.junit.runner.RunWith;

import static java.util.Spliterator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of an empty spliterator
 * Created by kfgodel on 07/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class EmptySpliteratorTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an empty spliterator", () -> {
      context().spliterator(EmptySpliterator::instance);

      it("cannot advance",()->{
        boolean result = context().spliterator().tryAdvance((value) -> {throw new RuntimeException("never happens");});

        assertThat(result).isFalse();
      });

      it("doesn't execute the consumer argument",()->{
        Variable<Boolean> executed = Variable.of(false);

        context().spliterator().tryAdvance((value) -> executed.set(true));

        assertThat(executed.get()).isFalse();
      });

      it("returns null when split",()->{
        assertThat(context().spliterator().trySplit()).isNull();
      });

      it("has 0 estimated size",()->{
          assertThat(context().spliterator().estimateSize()).isEqualTo(0);
      });

      it("has 0 exact size",()->{
        assertThat(context().spliterator().getExactSizeIfKnown()).isEqualTo(0);
      });

      it("has several characteristics to help combination with other spliterators",()->{
          assertThat(context().spliterator().characteristics())
            .isEqualTo(ORDERED | DISTINCT | SORTED | SIZED | NONNULL | IMMUTABLE | CONCURRENT );
      });

      it("ignores the consumer argument when #forEachRemaining() called",()->{
        Variable<Boolean> executed =Variable.of(false);

        context().spliterator().forEachRemaining((value)-> executed.set(true));

        assertThat(executed.get()).isFalse();
      });
    });

  }
}