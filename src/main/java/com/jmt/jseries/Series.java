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
package com.jmt.jseries;

import com.jmt.jseries.array.Array;
import com.jmt.jseries.array.ArrayBuilder;
import com.jmt.jseries.array.SortedArray;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;


public interface Series<T extends Comparable<T>, R> extends Function<T, R>, Serializable {

    String name();

    Series<T, R> withName(String name);

    SortedArray<T> domain();

    default T domain(int i) {
        return domain().get(i);
    }

    default int find(T x) {
        return domain().find(x);
    }

    default int findGE(T x) {
        return domain().findGE(x);
    }

    default int findGT(T x) {
        return domain().findGT(x);
    }

    default int findLE(T x) {
        return domain().findLE(x);
    }

    default int findLT(T x) {
        return domain().findLT(x);
    }

    Array<R> values();

    default R value(int i) {
        return values().get(i);
    }

    default R value(T x) {
        return apply(x);
    }

    default R apply(T x) {
        int index = domain().findLE(x);
        return index < 0 ? defaultValue() : value(index);
    }

    R defaultValue();

    default int size() {
        return domain().size();
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default Series<T, R> mapValues(Function<? super R, ? extends R> mapper) {
        return mapValues(values().newBuilder(size()), defaultValue(), mapper);
    }

    <S> Series<T, S> mapValues(ArrayBuilder<S, ? extends Array<S>> resultBuilder,
                               S defaultValue, Function<? super R, ? extends S> mapper);

    Series<T, R> zipWithValues(Function<? super T, ? extends R> operand2,
                               BiFunction<? super R, ? super R, ? extends R> operator);

    Series<T, R> zipWithValues(Array<? extends R> operand2,
                               BiFunction<? super R, ? super R, ? extends R> operator);
}
