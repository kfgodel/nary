package ar.com.kfgodel.nary.bugs;

import ar.com.kfgodel.nary.NaryTestContext;
import ar.com.kfgodel.nary.api.Narys;
import com.google.common.collect.Lists;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 20/12/17 - 17:37
 */
@RunWith(JavaSpecRunner.class)
public class NullMappingTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("using null as map result", () -> {

      describe("when using native streams", () -> {
        it("takes null as valid result", () -> {
          final List<Object> result = Stream.of("algo")
            .map(algo -> null)
            .collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList((Object) null));
        });
      });

      describe("when using native optional", () -> {
        it("implicitly filters null out as valid result", () -> {
          final boolean result = Optional.of("algo")
            .map(algo -> null)
            .isPresent();
          assertThat(result).isFalse();
        });
      });

      describe("when using nary", () -> {
        it("takes null as valid result when #map() is used", () -> {
          final List<Object> result = Narys.ofNonNullable("algo")
            .map(algo -> null)
            .collect(Collectors.toList());
          assertThat(result).isEqualTo(Lists.newArrayList((Object) null));
        });
        it("explicitly filters null as valid result when #mapFilteringNullResult() is used", () -> {
          final List<Object> result = Narys.ofNonNullable("algo")
            .mapFilteringNullResult(algo -> null)
            .map((nullValue) -> {
              throw new RuntimeException("This code is never executed");
            })
            .collect(Collectors.toList());
          assertThat(result).isEmpty();
        });
      });

    });

  }
}