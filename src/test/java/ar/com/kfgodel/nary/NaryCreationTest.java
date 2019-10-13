package ar.com.kfgodel.nary;

import ar.com.kfgodel.nary.api.Nary;
import info.kfgodel.jspek.api.JavaSpec;
import info.kfgodel.jspek.api.JavaSpecRunner;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashMap;
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

      it("can be done without elements", () -> {
        Nary<Integer> nary = Nary.empty();

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList());
      });

      it("can be done from a single element", () -> {
        Nary<Integer> nary = Nary.ofNonNullable(1);

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1));
      });

      it("can be done from variable elements", () -> {
        Nary<Integer> nary = Nary.ofNonNullable(1, 2, 3);

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be created from a nullable element", () -> {
        assertThat((Stream) Nary.of(null)).isEmpty();
        assertThat((Stream) Nary.of(1)).isNotEmpty();
      });

      it("can be done from a stream", () -> {
        Nary<Integer> nary = Nary.from(Stream.of(1, 2, 3));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be done from an optional", () -> {
        Nary<Integer> nary = Nary.from(Optional.of(1));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1));
      });

      it("can be done from a spliterator", () -> {
        Nary<Integer> nary = Nary.from(Spliterators.spliterator(new int[]{1, 2, 3}, 0));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be done from an iterator", () -> {
        ArrayList<Integer> originalList = Lists.newArrayList(1, 2, 3);
        Nary<Integer> nary = Nary.from(originalList.iterator());

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(originalList);
      });

      it("can be done from an iterable", () -> {
        Nary<Integer> nary = Nary.from((Iterable)Lists.newArrayList(1, 2, 3));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });
      it("can be done from a collection", () -> {
        Nary<Integer> nary = Nary.from((Collection)Lists.newArrayList(1, 2, 3));

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be made from an array", () -> {
        Nary<Integer> nary = Nary.from(new Integer[]{1, 2, 3});

        List<Integer> list = nary.collect(Collectors.toList());
        assertThat(list).isEqualTo(Lists.newArrayList(1, 2, 3));
      });

      it("can be created from enumeration", () -> {
        final Dictionary<String, String> dictionary = new Hashtable<>();
        dictionary.put("text", "123");

        final List<String> elements = Nary.from(dictionary.keys())
          .collectToList();

        assertThat(elements).containsExactly("text");
      });

      it("can be created from a map", () -> {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("text", "123");

        final List<String> elements = Nary.from(map)
          .map(entry -> entry.getKey() + entry.getValue())
          .collectToList();

        assertThat(elements).containsExactly("text123");
      });

    });

  }
}