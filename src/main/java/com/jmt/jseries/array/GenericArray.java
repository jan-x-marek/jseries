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

public final class GenericArray<T> implements Array<T> {

    private static final long serialVersionUID = 1L;

    public static <T> GenericArray<T> empty() {
        return ofNoClone();
    }

    @SafeVarargs
    public static <T> GenericArray<T> ofNoClone(T... values) {
        return new GenericArray<>(false, values);
    }

    @SafeVarargs
    public static <T> GenericArray<T> of(T... values) {
        return new GenericArray<>(true, values);
    }

    @SuppressWarnings("unchecked")
    public static <T> GenericArray<T> of(Collection<T> values) {
        return new GenericArray<>(false, (T[]) values.toArray());
    }

    public static <T> ArrayBuilder<T, Array<T>> builder(int sizeHint) {
        return new GenericArrayBuilder<>(sizeHint);
    }

    private final T[] values;

    private GenericArray(boolean clone, T[] values) {
        this.values = clone ? values.clone() : values;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public T get(int index) {
        return values[index];
    }

    private static class GenericArrayBuilder<T> implements ArrayBuilder<T, Array<T>> {

        private final ArrayList<T> list;

        private GenericArrayBuilder(int sizeHint) {
            list = new ArrayList<>(sizeHint);
        }

        @Override
        public ArrayBuilder<T, Array<T>> add(T value) {
            list.add(value);
            return this;
        }

        @Override
        public Array<T> build() {
            return GenericArray.of(list);
        }
    }
}