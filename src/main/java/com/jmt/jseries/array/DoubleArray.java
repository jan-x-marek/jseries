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

public final class DoubleArray implements Array<Double> {

    private static final long serialVersionUID = 1L;

    public static DoubleArray empty() {
        return ofNoClone();
    }

    public static DoubleArray ofNoClone(double... values) {
        return new DoubleArray(false, values);
    }

    public static DoubleArray of(double... values) {
        return new DoubleArray(true, values);
    }

    public static DoubleArray of(Collection<Double> values) {
        return new DoubleArray(false, toPrimitiveDouble(values));
    }
    
    /**
     * Create an array of evenly spaced numbers over a specified interval.
     */
    public static DoubleArray linspace(double start, double endExcl, double step) {
    	
    	double range = endExcl - start;
    	
		double dSize  = range < 0 ? 
				Math.floor(range / step) : 
				Math.ceil(range / step);
				
    	if (dSize >= Integer.MAX_VALUE || dSize<0) {
    		throw new IllegalArgumentException(start+" "+endExcl+" "+step+" "+dSize);
    	}
    	
    	int size = (int) dSize;
    	double[] result = new double[size];
    	double x = start;
    	for(int i=0; i<size; i++) {
    		result[i] = x;
    		x += step;
    	}
    	
        return ofNoClone(result);
    }
    
    private static double[] toPrimitiveDouble(Collection<Double> list) {
        int i = 0;
        int length = list.size();
        double[] result = new double[length];
        for (double element : list) {
            result[i++] = element;
        }
        return result;
    }

    public static ArrayBuilder<Double, Array<Double>> builder(int sizeHint) {
        return new DoubleArrayBuilder(sizeHint);
    }

    private final double[] values;

    private DoubleArray(boolean clone, double... values) {
        this.values = clone ? values.clone() : values;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Double get(int index) {
        return values[index];
    }

    @Override
    public ArrayBuilder<Double, ? extends Array<Double>> newBuilder(int sizeHint) {
        return new DoubleArrayBuilder(sizeHint);
    }

    private static class DoubleArrayBuilder implements ArrayBuilder<Double, Array<Double>> {

        //TODO Use an efficient primitive collection?
        private final ArrayList<Double> list;

        private DoubleArrayBuilder(int sizeHint) {
            list = new ArrayList<>(sizeHint);
        }

        @Override
        public ArrayBuilder<Double, Array<Double>> add(Double value) {
            list.add(value);
            return this;
        }

        @Override
        public Array<Double> build() {
            return DoubleArray.of(list);
        }
    }
}
