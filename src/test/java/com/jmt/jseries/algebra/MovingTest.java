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

import com.jmt.jseries.InstantDoubleSeries;
import com.jmt.jseries.Series;
import com.jmt.jseries.array.DoubleArray;
import com.jmt.jseries.array.InstantSortedArray;
import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class MovingTest {

    @Test
    public void movingAvg() {

        Series<Instant, Double> src = InstantDoubleSeries.create("foo",
                InstantSortedArray.ofMillis(1, 2, 3), DoubleArray.of(1, 2, 2), -1.0);

        Series<Instant, Double> result = Moving.avg(src, 2);

        assertThat(result.domain()).isSameAs(src.domain());
        assertThat(result.defaultValue()).isEqualTo(src.defaultValue());
        assertThat(result.values().asList()).containsExactly(1.0, 1.5, 2.0);
        assertThat(result).hasToString("MA(foo,2)");
    }

    @Test
    public void movingAvgExp() {

        Series<Instant, Double> src = InstantDoubleSeries.create("foo",
                InstantSortedArray.ofMillis(1, 2), DoubleArray.of(1, 2), -1.0);

        Series<Instant, Double> result = Moving.avgExp(src, 2);

        assertThat(result.domain()).isSameAs(src.domain());
        assertThat(result.defaultValue()).isEqualTo(src.defaultValue());
        assertThat(result.values().asList()).containsExactly(1.0, 1.6666666666666665);
        assertThat(result).hasToString("EMA(foo,2)");
    }

    @Test
    public void movingSum() {

        Series<Instant, Double> src = InstantDoubleSeries.create("foo",
                InstantSortedArray.ofMillis(1, 2, 3), DoubleArray.of(1, 2, 2), -1.0);

        Series<Instant, Double> result = Moving.sum(src, 2);

        assertThat(result.domain()).isSameAs(src.domain());
        assertThat(result.defaultValue()).isEqualTo(src.defaultValue());
        assertThat(result.values().asList()).containsExactly(1.0, 3.0, 4.0);
        assertThat(result).hasToString("Sum(foo,2)");
    }

    @Test
    public void movingMax() {

        Series<Instant, Double> src = InstantDoubleSeries.create("foo",
                InstantSortedArray.ofMillis(1, 2, 3), DoubleArray.of(1, 2, 1), -1.0);

        Series<Instant, Double> result = Moving.max(src, 2);

        assertThat(result.domain()).isSameAs(src.domain());
        assertThat(result.defaultValue()).isEqualTo(src.defaultValue());
        assertThat(result.values().asList()).containsExactly(1.0, 2.0, 2.0);
        assertThat(result).hasToString("Max(foo,2)");
    }

    @Test
    public void movingMin() {

        Series<Instant, Double> src = InstantDoubleSeries.create("foo",
                InstantSortedArray.ofMillis(1, 2, 3), DoubleArray.of(2, 1, 2), -1.0);

        Series<Instant, Double> result = Moving.min(src, 2);

        assertThat(result.domain()).isSameAs(src.domain());
        assertThat(result.defaultValue()).isEqualTo(src.defaultValue());
        assertThat(result.values().asList()).containsExactly(2.0, 1.0, 1.0);
        assertThat(result).hasToString("Min(foo,2)");
    }

    @Test
    public void shift() {

        Series<Instant, Double> src = InstantDoubleSeries.create("foo",
                InstantSortedArray.ofMillis(1, 2, 3, 4), DoubleArray.of(1, 2, 3, 4), -1.0);

        Series<Instant, Double> result = Moving.shift(src, 2);

        assertThat(result.domain()).isSameAs(src.domain());
        assertThat(result.defaultValue()).isEqualTo(src.defaultValue());
        assertThat(result.values().asList()).containsExactly(1.0, 1.0, 1.0, 2.0);
        assertThat(result).hasToString("Shift(foo,2)");
    }
    
    @Test
    public void quantile() {

        Series<Instant, Double> src = InstantDoubleSeries.create("foo",
                InstantSortedArray.ofMillis(1, 2, 3, 4), DoubleArray.of(1, 2, 3, 4), -1.0);

        Series<Instant, Double> result = Moving.quantile(src, 3, 0.5);

        assertThat(result.domain()).isSameAs(src.domain());
        assertThat(result.defaultValue()).isEqualTo(src.defaultValue());
        assertThat(result.values().asList()).containsExactly(1.0, 1.0, 2.0, 3.0);
        assertThat(result).hasToString("Qtl(foo,3,0.5)");
    }
}
