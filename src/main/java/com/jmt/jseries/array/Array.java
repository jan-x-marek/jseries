/* 
   Copyright 2017 Jan Marek
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.jmt.jseries.array;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface Array<T> extends Serializable {

    T get(int index);

    default T getFirst() {
        return get(0);
    }

    default T getLast() {
        return get(size() - 1);
    }

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default List<T> asList() {
        int size = size();
        ArrayList<T> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(get(i));
        }
        return result;
    }

    default ArrayBuilder<T, ? extends Array<T>> newBuilder(int sizeHint) {
        return GenericArray.builder(sizeHint);
    }

    default Array<T> map(Function<? super T, ? extends T> mapper) {
        return map(newBuilder(size()), mapper);
    }

    default <R> Array<R> map(ArrayBuilder<R, ? extends Array<R>> resultBuilder, Function<? super T, ? extends R> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            resultBuilder.add(mapper.apply(get(i)));
        }
        return resultBuilder.build();
    }
}
