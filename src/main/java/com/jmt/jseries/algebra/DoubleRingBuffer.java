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
package com.jmt.jseries.algebra;

import java.util.ArrayList;

public class DoubleRingBuffer {

    private final int maxSize;
    private final double[] values;

    private int size;
    private int lastIndex;

    public DoubleRingBuffer(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("" + maxSize);
        }
        this.maxSize = maxSize;
        values = new double[maxSize];
        size = 0;
        lastIndex = -1;
    }

    public void add(double d) {

        lastIndex++;
        if (lastIndex >= maxSize) {
            lastIndex = 0;
        }

        values[lastIndex] = d;

        if (size < maxSize) {
            size++;
        }
    }

    public double get(int relIndex) {
        if (relIndex < 0 || relIndex >= size) {
            throw new IndexOutOfBoundsException(relIndex + " >= " + size);
        }
        int trueIndex = (lastIndex + relIndex + 1) % size;
        return values[trueIndex];
    }

    public double last() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return values[lastIndex];
    }

    public double first() {
        return get(0);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isFull() {
        return size() == maxSize;
    }

    @Override
    public String toString() {
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(get(i));
        }
        return list.toString();
    }

    public double[] toArray() {
        //TODO make it faster with arraycopy
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = get(i);
        }
        return result;
    }
}
