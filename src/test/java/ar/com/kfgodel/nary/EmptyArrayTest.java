package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.impl.others.EmptyArray;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of the empty array used on empty streams
 * Created by kfgodel on 07/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class EmptyArrayTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("the empty array", () -> {
      context().array(() -> EmptyArray.INSTANCE);

      it("has 0 size", () -> {
        assertThat(context().array()).hasSize(0);
      });

      it("has an Object element type", () -> {
        Class<?> elementType = context().array().getClass().getComponentType();
        assertThat(elementType).isEqualTo(Object.class);
      });
    });

  }
}