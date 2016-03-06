package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.nary.api.Nary;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type tests the different nary creation methods
 * Created by kfgodel on 06/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class NaryCreationTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("a nary creation", () -> {

      it("can be done without elements",()->{
        Nary<Integer> nary = Nary.empty();

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList());
      });   
      
      it("can be done from a single element",()->{
        Nary<Integer> nary = Nary.of(1);

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1));
      });

      it("can be donde from variable elements",()->{
        Nary<Integer> nary = Nary.of(1, 2, 3);

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be done from a stream",()->{
        Nary<Integer> nary = Nary.create(Stream.of(1, 2, 3));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be done from an optional",()->{
        Nary<Integer> nary = Nary.create(Optional.of(1));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1));
      });

      it("can be made from a nullable element as optional",()->{
        Nary<Integer> nary = Nary.ofNullable(null);

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList());
      });

      it("can be done from a spliterator",()->{
        Nary<Integer> nary = Nary.create(Spliterators.spliterator(new int[]{1, 2, 3}, 0));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be done from an iterator",()->{
        ArrayList<Integer> originalList = Lists.newArrayList(1, 2, 3);
        Nary<Integer> nary = Nary.create(originalList.iterator());

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(originalList);
      });

      it("can be done from an iterable",()->{
        Nary<Integer> nary = Nary.create(Lists.newArrayList(1, 2, 3));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be made from an array",()->{
        Nary<Integer> nary = Nary.create(new Integer[]{1, 2, 3});

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

    });

  }
}