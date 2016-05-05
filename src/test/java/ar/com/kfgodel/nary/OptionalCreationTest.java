package ar.com.kfgodel.nary;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.nary.api.optionals.Optional;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Spliterators;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class tests the creation for optional types
 * Created by kfgodel on 04/05/16.
 */
@RunWith(JavaSpecRunner.class)
public class OptionalCreationTest extends JavaSpec<NaryTestContext> {
  @Override
  public void define() {
    describe("an optional creation", () -> {
      it("can be done without element",()->{
        Optional<Integer> optional = Optional.empty();

        assertThat(optional.isAbsent()).isTrue();
      });

      it("can be done from a non null element",()->{
        Optional<Integer> optional = Optional.of(1);

        assertThat(optional.get()).isEqualTo(1);
      });


      it("can be done from a nullable element",()->{
        Optional<Integer> optional = Optional.ofNullable(null);

        assertThat(optional.isAbsent()).isTrue();
      });

      it("can be done from a native optional",()->{
        Optional<Integer> optional = Optional.create(java.util.Optional.of(1));

        assertThat(optional.get()).isEqualTo(1);
      });

      it("can be done from a stream",()->{
        Optional<Integer> optional = Optional.create(Stream.of(1));

        assertThat(optional.get()).isEqualTo(1);
      });

      it("can be done from a spliterator",()->{
        Optional<Integer> optional = Optional.create(Spliterators.spliterator(new int[]{1}, 0));

        assertThat(optional.get()).isEqualTo(1);
      });

      it("can be done from an iterator",()->{
        ArrayList<Integer> originalList = Lists.newArrayList(1);
        Optional<Integer> optional = Optional.create(originalList.iterator());

        assertThat(optional.get()).isEqualTo(1);
      });

      it("can be done from an iterable",()->{
        Optional<Integer> optional = Optional.create(Lists.newArrayList(1));

        assertThat(optional.get()).isEqualTo(1);
      });

      it("can be made from an array",()->{
        Optional<Integer> optional = Optional.create(new Integer[]{1});

        assertThat(optional.get()).isEqualTo(1);
      });

    });

  }
}