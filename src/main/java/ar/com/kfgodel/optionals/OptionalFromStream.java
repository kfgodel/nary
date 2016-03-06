package ar.com.kfgodel.optionals;


import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This type represents the converter to get an optional value from a stream.<br>
 * Created by kfgodel on 23/10/14.
 */
public class OptionalFromStream {

    /**
     * Gets the optional value found in the given stream.<br>
     *     If the stream contains more than one element, an exception is risen
     * @param stream The stream to take elements from
     * @param <T> The expected type of element
     * @return The optional element, or empty if none was in the stream
     * @throws MoreThanOneElementException if more than one found in the stream
     */
    public static<T> Optional<T> using(Stream<T> stream) throws MoreThanOneElementException {
        Iterator<T> iterator = stream.iterator();
        if(!iterator.hasNext()){
            return Optional.empty();
        }
        T onlyElement = iterator.next();
        if(iterator.hasNext()){
            throw new MoreThanOneElementException("There's more than one element in the stream to create an optional: " + Arrays.asList(onlyElement, iterator.next()));
        }
        return Optional.ofNullable(onlyElement);
    }
}
