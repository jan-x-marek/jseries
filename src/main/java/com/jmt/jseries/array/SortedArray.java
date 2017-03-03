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

import java.util.function.Function;

public interface SortedArray<T extends Comparable<T>> extends Array<T> {

    int find(T x);

    int findGE(T x);

    int findGT(T x);

    int findLE(T x);

    int findLT(T x);

    default ArrayBuilder<T, ? extends SortedArray<T>> newSortedBuilder(int sizeHint) {
        return GenericSortedArray.builder(sizeHint);
    }

    default SortedArray<T> mapSorted(Function<? super T, ? extends T> mapper) {
        return mapSorted(newSortedBuilder(size()), mapper);
    }

    default <R extends Comparable<R>> SortedArray<R> mapSorted(
            ArrayBuilder<R, ? extends SortedArray<R>> resultBuilder,
            Function<? super T, ? extends R> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            resultBuilder.add(mapper.apply(get(i)));
        }
        return resultBuilder.build();
    }
}
