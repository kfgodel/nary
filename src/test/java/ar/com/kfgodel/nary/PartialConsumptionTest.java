package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * This class verifies that instances of Nary can be consumed partially in different calls
 *
 * Date: 13/10/19 - 17:22
 */
@RunWith(JavaSpecRunner.class)
public class PartialConsumptionTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("a nary", () -> {
      test().nary(() -> Nary.of(1, 2, 3));

      it("can partially consume its first elements ",()->{
        final List<Integer> consumed = new ArrayList<>();
        Nary<Integer> notConsumed = test().nary()
          .consumeWith(
            consumed::add,
            consumed::add
          );
        assertThat(consumed).isEqualTo(Lists.newArrayList(1,2));
        assertThat((Stream)notConsumed).containsExactly(3);
      });

      it("returns an empty nary when all elements are consumed",()->{
        Nary<Integer> notConsumed = test().nary()
          .consumeWith(
            this::ignoreElement,
            this::ignoreElement,
            this::ignoreElement
          );
          assertThat((Stream)notConsumed).isEmpty();
      });

      it("ignores consumers after the elements are exhausted",()->{
          Nary.empty()
            .consumeWith((value)-> fail("This consumer should be ignored"));
      });

    });

  }

  private void ignoreElement(Integer integer) {
    // Ignoring
  }
}