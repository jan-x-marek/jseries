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
import com.jmt.jseries.array.Array;

import java.util.function.Function;

public class Binary {

    public static <T extends Comparable<T>> Series<T, Double> add(Series<T, Double> op1, double op2) {
        return op1.mapValues(x -> x + op2)
                .withName("Add(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> add(Series<T, Double> op1, Function<T, Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x + y)
                .withName("Add(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> add(Series<T, Double> op1, Array<Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x + y)
                .withName("Add(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> sub(Series<T, Double> op1, double op2) {
        return op1.mapValues(x -> x - op2)
                .withName("Sub(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> sub(Series<T, Double> op1, Function<T, Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x - y)
                .withName("Sub(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> sub(Series<T, Double> op1, Array<Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x - y)
                .withName("Sub(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> mul(Series<T, Double> op1, double op2) {
        return op1.mapValues(x -> x * op2)
                .withName("Mul(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> mul(Series<T, Double> op1, Function<T, Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x * y)
                .withName("Mul(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> mul(Series<T, Double> op1, Array<Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x * y)
                .withName("Mul(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> div(Series<T, Double> op1, double op2) {
        return op1.mapValues(x -> x / op2)
                .withName("Div(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> div(Series<T, Double> op1, Function<T, Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x / y)
                .withName("Div(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> div(Series<T, Double> op1, Array<Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> x / y)
                .withName("Div(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> div0(Series<T, Double> op1, double op2) {
        return op1.mapValues(x -> op2 == 0.0 ? 0.0 : x / op2)
                .withName("Div0(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> div0(Series<T, Double> op1, Function<T, Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> y == 0.0 ? 0.0 : x / y)
                .withName("Div0(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> div0(Series<T, Double> op1, Array<Double> op2) {
        return op1.zipWithValues(op2, (x, y) -> y == 0.0 ? 0.0 : x / y)
                .withName("Div0(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> max(Series<T, Double> op1, double op2) {
        return op1.mapValues(x -> Math.max(x, op2))
                .withName("Max(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> max(Series<T, Double> op1, Function<T, Double> op2) {
        return op1.zipWithValues(op2, Math::max)
                .withName("Max(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> max(Series<T, Double> op1, Array<Double> op2) {
        return op1.zipWithValues(op2, Math::max)
                .withName("Max(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> min(Series<T, Double> op1, double op2) {
        return op1.mapValues(x -> Math.min(x, op2))
                .withName("Min(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> min(Series<T, Double> op1, Function<T, Double> op2) {
        return op1.zipWithValues(op2, Math::min)
                .withName("Min(" + op1.name() + "," + op2 + ")");
    }

    public static <T extends Comparable<T>> Series<T, Double> min(Series<T, Double> op1, Array<Double> op2) {
        return op1.zipWithValues(op2, Math::min)
                .withName("Min(" + op1.name() + "," + op2 + ")");
    }
}
