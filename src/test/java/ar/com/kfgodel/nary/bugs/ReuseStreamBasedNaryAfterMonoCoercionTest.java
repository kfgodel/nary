package ar.com.kfgodel.nary.bugs;

import ar.com.kfgodel.nary.NaryTestContext;
import ar.com.kfgodel.nary.api.Nary;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test verifies that a nary that is created from a stream or collection as input, after being coerced
 * doesn't fail to behave as an optional. Even if the stream was consumed on the coercion
 * Date: 11/12/19 - 01:03
 */
@RunWith(JavaSpecRunner.class)
public class ReuseStreamBasedNaryAfterMonoCoercionTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("a stream based nary", () -> {
      test().nary(() -> Nary.from(Stream.of(1)));

      describe("after being coerced to a mono nary", () -> {
        beforeEach(() -> {
          test().nary().unique(); // We force it to be evaluated as an optional and coerced
        });

        it("can still be used as a stream", () -> {
          final List<Integer> elements = test().nary().collect(Collectors.toList());
          assertThat(elements).containsExactly(1);
        });
      });

      describe("after being mapped", () -> {
        beforeEach(() -> {
          final List<Integer> mapped = test().nary()
            .map(num -> num + 1)
            .collectToList();
          assertThat(mapped).containsExactly(2);
        });

        itThrows(IllegalStateException.class, "because it was not coerced first",()->{
          test().nary()
            .map(num -> num + 2)
            .collectToList();
        }, e ->{
          assertThat(e).hasMessage("stream has already been operated upon or closed");
        });
      });


    });

  }
}