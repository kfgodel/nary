package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.nary.impl.others.EmptyIterator;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * Created by kfgodel on 07/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class EmptyIteratorTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an empty iterator", () -> {
      context().iterator(EmptyIterator::instance);

      it("has no next elements",()->{
          assertThat(context().iterator().hasNext()).isFalse();
      });

      it("throws an exception when next() element is accessed",()->{
          try{
            context().iterator().next();
            failBecauseExceptionWasNotThrown(NoSuchElementException.class);
          }catch(NoSuchElementException e){
            assertThat(e).hasMessage("Next element can't be accessed on empty iterator");
          }
      });
    });
  }
}