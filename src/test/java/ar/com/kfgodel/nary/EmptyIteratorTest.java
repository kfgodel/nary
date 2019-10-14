package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.impl.others.EmptyIterator;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import info.kfgodel.jspek.api.variable.Variable;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type verifies the behavior of an empty iterator
 * Created by kfgodel on 07/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class EmptyIteratorTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an empty iterator", () -> {
      context().iterator(EmptyIterator::instance);

      it("has no next elements", () -> {
        assertThat(context().iterator().hasNext()).isFalse();
      });

      it("throws an exception when next() element is accessed", () -> {
        try {
          context().iterator().next();
          failBecauseExceptionWasNotThrown(NoSuchElementException.class);
        } catch (NoSuchElementException e) {
          assertThat(e).hasMessage("Next element can't be accessed on empty iterator");
        }
      });

      it("throws an exception if tried to remove", () -> {
        try {
          context().iterator().remove();
          failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
        } catch (UnsupportedOperationException e) {
          assertThat(e).hasMessage("Can't remove elements on an empty iterator");
        }
      });

      it("ignores the consumer argument when #forEachRemaining() called", () -> {
        Variable<Boolean> executed = Variable.of(false);

        context().iterator().forEachRemaining((value) -> executed.set(true));

        assertThat(executed.get()).isFalse();
      });
    });
  }
}