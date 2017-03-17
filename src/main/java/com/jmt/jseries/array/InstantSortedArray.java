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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

public final class InstantSortedArray implements SortedArray<Instant> {

    private static final long serialVersionUID = 1L;

    private final long[] values;

    public static InstantSortedArray empty() {
        return of();
    }

    public static InstantSortedArray of(Collection<Instant> values) {
        return of(values.toArray(new Instant[values.size()]));
    }

    public static InstantSortedArray of(Instant... values) {
        return new InstantSortedArray(false, toLong(values));
    }

    private static long[] toLong(Instant[] dates) {
        int length = dates.length;
        long[] result = new long[length];
        for (int i = 0; i < length; i++) {
            result[i] = dates[i].toEpochMilli();
        }
        return result;
    }

    public static InstantSortedArray ofMillis(long... values) {
        return new InstantSortedArray(true, values);
    }

    public static InstantSortedArray ofMillisNoClone(long... values) {
        return new InstantSortedArray(false, values);
    }
    
    /**
     * Create an array of evenly spaced numbers over a specified interval.
     */
    public static InstantSortedArray linspace(long start, long endExcl, long step) {
    	
    	long range = endExcl - start;
    	
		long lSize = (long)Math.ceil((double)range / step);
				
    	if (lSize >= Integer.MAX_VALUE || lSize<0) {
    		throw new IllegalArgumentException(start+" "+endExcl+" "+step+" "+lSize);
    	}
    	
    	int size = (int) lSize;
    	long[] result = new long[size];
    	long x = start;
    	for(int i=0; i<size; i++) {
    		result[i] = x;
    		x += step;
    	}
    	
        return ofMillisNoClone(result);
    }
    
    public static ArrayBuilder<Instant, SortedArray<Instant>> builder(int sizeHint) {
        return new SortedInstantArrayBuilder(sizeHint);
    }

    private InstantSortedArray(boolean clone, long... values) {
        checkOrder(values);
        this.values = clone ? values.clone() : values;
    }

    private void checkOrder(long[] values) {
        for (int i = 1; i < values.length; i++) {
            if (values[i - 1] > values[i]) {
                throw new IllegalArgumentException("The array is not in ascending order. position: " + i + " values: " + values[i - 1] + " " + values[i]);
            }
        }
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Instant get(int index) {
        return Instant.ofEpochMilli(values[index]);
    }

    @Override
    public int find(Instant x) {
        return ArraySearchUtil.find(values, x.toEpochMilli());
    }

    @Override
    public int findGE(Instant x) {
        return ArraySearchUtil.findGE(values, x.toEpochMilli());
    }

    @Override
    public int findGT(Instant x) {
        return ArraySearchUtil.findGT(values, x.toEpochMilli());
    }

    @Override
    public int findLE(Instant x) {
        return ArraySearchUtil.findLE(values, x.toEpochMilli());
    }

    @Override
    public int findLT(Instant x) {
        return ArraySearchUtil.findLT(values, x.toEpochMilli());
    }

    @Override
    public ArrayBuilder<Instant, ? extends SortedArray<Instant>> newSortedBuilder(int sizeHint) {
        return new SortedInstantArrayBuilder(sizeHint);
    }

    private static class SortedInstantArrayBuilder implements ArrayBuilder<Instant, SortedArray<Instant>> {

        //TODO Use an efficient primitive collection?
        private final ArrayList<Instant> list;

        private SortedInstantArrayBuilder(int sizeHint) {
            list = new ArrayList<>(sizeHint);
        }

        @Override
        public ArrayBuilder<Instant, SortedArray<Instant>> add(Instant value) {
            list.add(value);
            return this;
        }

        @Override
        public SortedArray<Instant> build() {
            return InstantSortedArray.of(list);
        }
    }
}
