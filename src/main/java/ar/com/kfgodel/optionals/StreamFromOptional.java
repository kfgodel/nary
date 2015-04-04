package ar.com.kfgodel.optionals;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This type represents the converter from Optional to stream
 * Created by kfgodel on 06/11/14.
 */
public class StreamFromOptional {

    public static<T> Stream<T> create(Optional<T> optional) {
        if(optional.isPresent()){
            return Stream.of(optional.get());
        }
        return Stream.empty();
    }

}
