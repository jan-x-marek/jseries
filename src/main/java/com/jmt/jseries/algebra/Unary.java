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

public class Unary {

    public static <T extends Comparable<T>> Series<T, Double> minus(Series<T, Double> op) {
        return op.mapValues(x -> -x).withName("Minus(" + op.name() + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> abs(Series<T, Double> op) {
        return op.mapValues(Math::abs).withName("Abs(" + op.name() + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> sqr(Series<T, Double> op) {
        return op.mapValues(x -> x * x).withName("Sqr(" + op.name() + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> sqrt(Series<T, Double> op) {
        return op.mapValues(Math::sqrt).withName("Sqrt(" + op.name() + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> sgn(Series<T, Double> op) {
        return op.mapValues(Math::signum).withName("Sgn(" + op.name() + ")");
    }
}
