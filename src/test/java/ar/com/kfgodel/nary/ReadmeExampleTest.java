package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 15/10/19 - 23:29
 */
@RunWith(JavaSpecRunner.class)
public class ReadmeExampleTest extends JavaSpec<ReadmeExampleTestContext> {
  @Override
  public void define() {
    describe("a nary ", () -> {

      it("can be used as a stream of results", () -> {
        final List<Integer> even = integersUpTo(4)
          .filter(number -> number % 2 == 0)
          .collectToList();
        assertThat(even).containsExactly(2, 4);
      });

      it("can be used as an Optional", () -> {
        final Integer result = integersUpTo(1)
          .get();  // Assume there's only one element
        assertThat(result).isEqualTo(1);
      });

    });

  }

  private Nary<Integer> integersUpTo(int number) {
    List<Integer> returned = new ArrayList<>();
    for (int i = 1; i <= number; i++) {
      returned.add(i);
    }
    return Nary.from(returned);
  }
}