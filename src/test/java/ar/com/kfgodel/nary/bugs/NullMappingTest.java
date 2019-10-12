package ar.com.kfgodel.nary.bugs;

import ar.com.kfgodel.nary.NaryTestContext;
import ar.com.kfgodel.nary.api.Nary;
import com.google.common.collect.Lists;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Date: 20/12/17 - 17:37
 */
@RunWith(JavaSpecRunner.class)
public class NullMappingTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("using null as map result", () -> {


      it("it implicitly filters null as empty optional after map",()->{
        Nary.of("algo")
          .mapOptional((algo)-> null)
          .mapOptional((algo)-> {
            fail("It should never execute");
            return "Never happens";
          })
          .ifPresent((nullied)-> fail("Should never run"));
      });

      it("treats null from first map as empty nary for the second map",()->{
        List<Object> mapped = Nary.of("algo")
          .mapNary((algo) -> null)
          .mapNary((algo) -> algo)
          .collect(Collectors.toList());

        assertThat(mapped).isEqualTo(Lists.newArrayList((Object)null));
      });   
    });

  }
}