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
import com.jmt.jseries.array.DoubleArray;
import com.jmt.jseries.array.InstantSortedArray;
import com.jmt.jseries.array.SortedArray;

import java.time.Instant;
import java.util.Collection;

public class InstantDoubleSeries extends SeriesImpl<Instant, Double> {

    private static final long serialVersionUID = 1L;

    public static InstantDoubleSeries create(Collection<Instant> domain, Collection<Double> values) {
        return create(null, domain, values);
    }

    public static InstantDoubleSeries create(
            String name, Collection<Instant> domain,
            Collection<Double> values) {
        return create(name, domain, values, 0.0);
    }

    public static InstantDoubleSeries create(
            String name, Collection<Instant> domain,
            Collection<Double> values, double defaultValue) {
        return create(name, InstantSortedArray.of(domain), DoubleArray.of(values), defaultValue);
    }

    public static InstantDoubleSeries create(
            Instant[] domain, double[] values) {
        return create(null, domain, values);
    }

    public static InstantDoubleSeries create(
            String name, Instant[] domain,
            double[] values) {
        return create(name, domain, values, 0.0);
    }

    public static InstantDoubleSeries create(
            String name, Instant[] domain,
            double[] values, double defaultValue) {
        return create(name, InstantSortedArray.of(domain), DoubleArray.ofNoClone(values), defaultValue);
    }

    public static InstantDoubleSeries create(
            SortedArray<Instant> domain, Array<Double> values) {
        return create(null, domain, values);
    }

    public static InstantDoubleSeries create(
            String name, SortedArray<Instant> domain,
            Array<Double> values) {
        return create(name, domain, values, 0.0);
    }

    public static InstantDoubleSeries create(
            String name, SortedArray<Instant> domain,
            Array<Double> values, double defaultValue) {
        return new InstantDoubleSeries(name, domain, values, defaultValue);
    }

    protected InstantDoubleSeries(
            String name, SortedArray<Instant> domain,
            Array<Double> values, double defaultValue) {
        super(name, domain, values, defaultValue);
    }
}
