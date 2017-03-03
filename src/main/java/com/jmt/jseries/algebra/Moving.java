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

import com.jmt.jseries.Series;

public class Moving {

    public static <T extends Comparable<T>> Series<T, Double> avg(Series<T, Double> src, int period) {
        return src.mapValues(DirtyFunctions.movingAvg(period)).withName("MA(" + src.name() + "," + period + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> avgExp(Series<T, Double> src, int period) {
        return src.mapValues(DirtyFunctions.movingAvgExp(period)).withName("EMA(" + src.name() + "," + period + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> sum(Series<T, Double> src, int period) {
        return src.mapValues(DirtyFunctions.movingSum(period)).withName("Sum(" + src.name() + "," + period + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> min(Series<T, Double> src, int period) {
        return src.mapValues(DirtyFunctions.movingMin(period)).withName("Min(" + src.name() + "," + period + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> max(Series<T, Double> src, int period) {
        return src.mapValues(DirtyFunctions.movingMax(period)).withName("Max(" + src.name() + "," + period + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> shift(Series<T, Double> src, int shift) {
        return src.mapValues(DirtyFunctions.shift(shift)).withName("Shift(" + src.name() + "," + shift + ")");
    }
    
    public static <T extends Comparable<T>> Series<T, Double> quantile(Series<T, Double> src, int period, double quantile) {
        return src.mapValues(DirtyFunctions.movingQuantile(period, quantile)).withName("Qtl(" + src.name() + "," + period + "," + quantile + ")");
    }
}
