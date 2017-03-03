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

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Function;

public class DirtyFunctions {

    public static Function<Double, Double> movingAvg(int period) {

        if (period < 1) {
            throw new IllegalArgumentException("" + period);
        }

        if (period == 1) {
            return Function.identity();
        }

        return new Function<Double, Double>() {

            private final DoubleRingBuffer history = new DoubleRingBuffer(period + 1);
            private double prevResult;

            @Override
            public Double apply(Double d) {
                double result;
                history.add(d);
                if (history.size() <= period) {
                    double sum = 0.0;
                    for (int i = 0; i < history.size(); i++) {
                        sum += history.get(i);
                    }
                    result = sum / history.size();
                } else {
                    double oldItem = history.first() / period;
                    double newItem = history.last() / period;
                    result = prevResult - oldItem + newItem;
                }
                prevResult = result;
                return result;
            }
        };
    }

    public static Function<Double, Double> movingAvgExp(int period) {

        if (period < 1) {
            throw new IllegalArgumentException("" + period);
        }

        if (period == 1) {
            return Function.identity();
        }

        return new Function<Double, Double>() {

            private final double smoothingFactor = 2.0 / (period + 1.0);
            private double prevResult;
            private boolean firstCall = true;

            @Override
            public Double apply(Double d) {
                double result;
                if (firstCall) {
                    firstCall = false;
                    result = d;
                } else {
                    result = d * smoothingFactor + prevResult * (1.0 - smoothingFactor);
                }
                prevResult = result;
                return result;
            }
        };
    }

    public static Function<Double, Double> movingSum(int period) {

        if (period < 1) {
            throw new IllegalArgumentException("" + period);
        }

        if (period == 1) {
            return Function.identity();
        }

        return new Function<Double, Double>() {

            private final DoubleRingBuffer history = new DoubleRingBuffer(period + 1);
            private double prevResult = 0.0;

            @Override
            public Double apply(Double d) {
                double result;
                history.add(d);
                if (history.size() <= period) {
                    result = prevResult + history.last();
                } else {
                    result = prevResult + history.last() - history.first();
                }
                prevResult = result;
                return result;
            }
        };
    }

    public static Function<Double, Double> movingMin(int period) {
        return movingMinOrMax(period, false);
    }

    public static Function<Double, Double> movingMax(int period) {
        return movingMinOrMax(period, true);
    }

    private static Function<Double, Double> movingMinOrMax(int period, boolean max) {

        if (period < 1) {
            throw new IllegalArgumentException("" + period);
        }

        if (period == 1) {
            return Function.identity();
        }

        return new Function<Double, Double>() {

            private final DoubleRingBuffer history = new DoubleRingBuffer(period);

            private final Comparator<Double> comparator = max ?
                    Comparator.<Double>naturalOrder().reversed() :
                    Comparator.<Double>naturalOrder();

            private final PriorityQueue<Double> ordered = new PriorityQueue<>(period, comparator);

            @Override
            public Double apply(Double d) {
                if (history.size() == period) {
                    ordered.remove(history.first());
                }
                history.add(d);
                ordered.add(d);
                return ordered.peek();
            }
        };
    }

    public static Function<Double, Double> shift(int period) {
        if (period < 0) {
            throw new IllegalArgumentException("" + period);
        }
        if (period == 0) {
            return Function.identity();
        }
        if (period == 1) {
            return new Function<Double, Double>() {

                private boolean firstCall = true;
                private double prev;

                @Override
                public Double apply(Double d) {
                    if (firstCall) {
                        firstCall = false;
                        prev = d;
                        return d;
                    } else {
                        double result = prev;
                        prev = d;
                        return result;
                    }
                }
            };
        } else {
            return new Function<Double, Double>() {

                private final DoubleRingBuffer history = new DoubleRingBuffer(period);

                @Override
                public Double apply(Double d) {
                    if (history.isEmpty()) {
                        history.add(d);
                        return d;
                    } else {
                        double result = history.first();
                        history.add(d);
                        return result;
                    }
                }
            };
        }
    }

    /**
     * Scalable quantile implementation using two heaps.
     * No interpolation is performed, the nearest lower value from the sample is returned.
     * Hence the result can be quite imprecise for small a window size or a very uneven data distribution.
     * @param period window size
     * @param quantile 0..1
     * @return The moving quantile function
     */
    public static Function<Double, Double> movingQuantile(int period, double quantile) {

        if (period < 1) {
            throw new IllegalArgumentException("" + period);
        }

        if (quantile < 0 || quantile > 1) {
            throw new IllegalArgumentException("" + quantile);
        }

        if (period == 1) {
            return Function.identity();
        }

        return new Function<Double, Double>() {

            private final DoubleRingBuffer history = new DoubleRingBuffer(period + 1);

            //The heap stores max value in the root (reverse comparator)
            private final PriorityQueue<Double> low = new PriorityQueue<>(1, Comparator.reverseOrder());

            //The heap stores min value in the root (default comparator).
            private final PriorityQueue<Double> high = new PriorityQueue<>(1, Comparator.naturalOrder());

            @Override
            public Double apply(Double d) {

                if (isEmpty() || d <= peek()) {
                    low.add(d);
                } else {
                    high.add(d);
                }

                history.add(d);
                if (history.isFull()) {
                    double old = history.first();
                    if (!low.remove(old)) {
                        high.remove(old);
                    }
                }

                int expectedLowSize = (int) Math.round(quantile * size());

                while (!low.isEmpty() && low.size() > expectedLowSize) {
                    high.add(low.poll());
                }
                while (!high.isEmpty() && low.size() < expectedLowSize) {
                    low.add(high.poll());
                }

                return peek();
            }

            private boolean isEmpty() {
                return size() == 0;
            }

            private int size() {
                return low.size() + high.size();
            }

            private double peek() {
                return low.isEmpty() ? high.peek() : low.peek();
            }
        };
    }
}
