package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.impl.others.OneElementIterator;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type verifies the behavior of an iterator with only one element
 * Created by kfgodel on 07/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class OneElementIteratorTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("a one element iterator", () -> {
      context().iterator(()-> OneElementIterator.create(1));

      it("has more elements",()->{
          assertThat(context().iterator().hasNext()).isTrue();
      });
      
      it("returns the element when next() is called",()->{
          assertThat(context().iterator().next()).isEqualTo(1);
      });

      describe("once iterated", () -> {
        beforeEach(()->{
          context().iterator().next();
        });

        it("has no next elements",()->{
          assertThat(context().iterator().hasNext()).isFalse();
        });

        it("throws an exception when next() element is accessed",()->{
          try{
            context().iterator().next();
            failBecauseExceptionWasNotThrown(NoSuchElementException.class);
          }catch(NoSuchElementException e){
            assertThat(e).hasMessage("This one element iterator was already iterated");
          }
        });

      });
    });
  }
}