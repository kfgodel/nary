package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the contract a {@link Nary} has when used as {@link java.util.function.Supplier}
 * Date: 12/12/19 - 18:49
 */
@RunWith(JavaSpecRunner.class)
public class NaryAsSupplierTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("a nary", () -> {
      test().nary(() -> Nary.of(10));

      describe("as supplier", () -> {
        test().supplier(() -> test().nary().asUni());

        it("is coerced as optional and returns its only value", () -> {
          assertThat(test().supplier().get()).isEqualTo(10);
        });

        itThrows(NoSuchElementException.class, "if it is empty", () -> {
          test().nary(Nary::empty);
          test().supplier().get();
        }, e -> {
          assertThat(e).hasMessage("Can't call get() on an empty nary: No value present");
        });

        itThrows(MoreThanOneElementException.class, "if it has more than 1 element", () -> {
          test().nary(() -> Nary.ofNonNullable(1 ,2 ,3));
          test().supplier().get();
        }, e -> {
          assertThat(e).hasMessage("Expecting only 1 element in the stream to treat it as an " +
            "optional but found at least 2: [1, 2]");
        });

      });


    });

  }
}