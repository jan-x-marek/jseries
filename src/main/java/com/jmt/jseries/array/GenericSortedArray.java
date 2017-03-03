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

import java.util.ArrayList;
import java.util.Collection;

public final class GenericSortedArray<T extends Comparable<T>> implements SortedArray<T> {

    private static final long serialVersionUID = 1L;

    private final T[] values;

    public static <T extends Comparable<T>> GenericSortedArray<T> empty() {
        return of();
    }

    @SafeVarargs
    public static <T extends Comparable<T>> GenericSortedArray<T> of(T... values) {
        return new GenericSortedArray<>(true, values);
    }

    @SafeVarargs
    public static <T extends Comparable<T>> GenericSortedArray<T> ofNoClone(T... values) {
        return new GenericSortedArray<>(false, values);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> GenericSortedArray<T> of(Collection<T> values) {
        return ofNoClone((T[]) values.toArray(new Comparable[values.size()]));
    }

    public static <T extends Comparable<T>> ArrayBuilder<T, SortedArray<T>> builder(int sizeHint) {
        return new SortedGenericArrayBuilder<>(sizeHint);
    }

    private GenericSortedArray(boolean clone, T[] values) {
        checkOrder(values);
        this.values = clone ? values.clone() : values;
    }

    private void checkOrder(T[] values) {
        for (int i = 1; i < values.length; i++) {
            if (values[i - 1].compareTo(values[i]) > 0) {
                throw new IllegalArgumentException("The array is not in ascending order. position: " + i + " values: " + values[i - 1] + " " + values[i]);
            }
        }
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public T get(int index) {
        return values[index];
    }

    @Override
    public int find(T x) {
        return ArraySearchUtil.find(values, x);
    }

    @Override
    public int findGE(T x) {
        return ArraySearchUtil.findGE(values, x);
    }

    @Override
    public int findGT(T x) {
        return ArraySearchUtil.findGT(values, x);
    }

    @Override
    public int findLE(T x) {
        return ArraySearchUtil.findLE(values, x);
    }

    @Override
    public int findLT(T x) {
        return ArraySearchUtil.findLT(values, x);
    }

    private static class SortedGenericArrayBuilder<T extends Comparable<T>> implements ArrayBuilder<T, SortedArray<T>> {

        private final ArrayList<T> list;

        private SortedGenericArrayBuilder(int sizeHint) {
            list = new ArrayList<>(sizeHint);
        }

        @Override
        public ArrayBuilder<T, SortedArray<T>> add(T value) {
            list.add(value);
            return this;
        }

        @Override
        public SortedArray<T> build() {
            return GenericSortedArray.of(list);
        }
    }
}
