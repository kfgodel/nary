package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.dgarcia.javaspec.api.Variable;
import ar.com.kfgodel.nary.impl.others.EmptySpliterator;
import org.junit.runner.RunWith;

import java.util.Spliterator;

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
        Variable<Boolean> executed = Variable.of(false);

        boolean result = context().spliterator().tryAdvance((value) -> executed.set(true));

        assertThat(result).isFalse();
        assertThat(executed.get()).isFalse();
      });
      
      it("returns itself when splitted",()->{
        Spliterator<Integer> splitted = context().spliterator().trySplit();
        assertThat(splitted).isSameAs(context().spliterator());
      });

      it("has 0 size",()->{
          assertThat(context().spliterator().estimateSize()).isEqualTo(0);
      });
      
      it("has no characteristics",()->{
          assertThat(context().spliterator().characteristics()).isEqualTo(0);
      });   
    });

  }
}