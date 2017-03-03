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

import java.util.function.BiFunction;
import java.util.function.Function;

public class SeriesImpl<T extends Comparable<T>, R> implements Series<T, R> {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final SortedArray<T> domain;

    private final Array<R> values;

    private final R defaultValue;

    public SeriesImpl(String name, SortedArray<T> domain, Array<R> values, R defaultValue) {
        if (domain.size() != values.size()) {
            throw new IllegalArgumentException("Domain and values must have the same size: " + domain.size() + " " + values.size());
        }
        this.name = name == null ? getClass().getSimpleName() : name;
        this.defaultValue = defaultValue;
        this.domain = domain;
        this.values = values;
    }

    @Override
    public SortedArray<T> domain() {
        return domain;
    }

    @Override
    public Array<R> values() {
        return values;
    }

    @Override
    public R defaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Series<T, R> withName(String name) {
        return new SeriesImpl<>(name, domain, values, defaultValue);
    }

    @Override
    public R value(int i) {
        return values.get(i);
    }

    @Override
    public T domain(int i) {
        return domain.get(i);
    }

    @Override
    public <S> Series<T, S> mapValues(ArrayBuilder<S, ? extends Array<S>> resultBuilder,
                                      S defaultValue, Function<? super R, ? extends S> mapper) {
        return new SeriesImpl<>(name, domain, values.map(resultBuilder, mapper), defaultValue);
    }

    @Override
    public Series<T, R> zipWithValues(Function<? super T, ? extends R> operand2,
                                      BiFunction<? super R, ? super R, ? extends R> operator) {
        int size = size();
        ArrayBuilder<R, ? extends Array<R>> valuesBuilder = values.newBuilder(size);
        for (int i = 0; i < size; i++) {
            valuesBuilder.add(operator.apply(value(i), operand2.apply(domain(i))));
        }
        return new SeriesImpl<>(name, domain, valuesBuilder.build(), defaultValue);
    }

    @Override
    public Series<T, R> zipWithValues(Array<? extends R> operand2,
                                      BiFunction<? super R, ? super R, ? extends R> operator) {
        int size = size();
        if (size != operand2.size()) {
            throw new IllegalArgumentException(size + "!=" + operand2.size());
        }
        ArrayBuilder<R, ? extends Array<R>> valuesBuilder = values.newBuilder(size);
        for (int i = 0; i < size; i++) {
            valuesBuilder.add(operator.apply(value(i), operand2.get(i)));
        }
        return new SeriesImpl<>(name, domain, valuesBuilder.build(), defaultValue);
    }
}
