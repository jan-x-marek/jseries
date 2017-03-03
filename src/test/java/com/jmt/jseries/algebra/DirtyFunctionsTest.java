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

import com.jmt.testutil.FunctionAssert;
import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class DirtyFunctionsTest {
	
    private static final Offset<Double> offset9 = Offset.offset(0.000_000_001);
    
    @Test
    public void movingAvg() {
        FunctionAssert.assertThat(DirtyFunctions.movingAvg(3))
                .returnsCloseTo(1d, 1d, offset9)
                .returnsCloseTo(2d, (1d + 2d) / 2d, offset9)
                .returnsCloseTo(4d, (1d + 2d + 4d) / 3d, offset9)
                .returnsCloseTo(8d, (2d + 4d + 8d) / 3d, offset9)
                .returnsCloseTo(16d, (4d + 8d + 16d) / 3d, offset9)
                .returnsCloseTo(32d, (8d + 16d + 32d) / 3d, offset9);
    }

    @Test
    public void movingAvgExp() {
        double r0 = 1.0;
        double r1 = r0 / 3.0 + 2.0 * 2.0 / 3.0;
        double r2 = r1 / 3.0 + 2.0 * 4.0 / 3.0;
        double r3 = r2 / 3.0 + 2.0 * 8.0 / 3.0;
        FunctionAssert.assertThat(DirtyFunctions.movingAvgExp(2))
                .returnsCloseTo(1.0, r0, offset9)
                .returnsCloseTo(2.0, r1, offset9)
                .returnsCloseTo(4.0, r2, offset9)
                .returnsCloseTo(8.0, r3, offset9);
    }

    @Test
    public void movingSum() {
        FunctionAssert.assertThat(DirtyFunctions.movingSum(3))
                .returns(1.0, 1.0)
                .returns(2.0, 3.0)
                .returns(4.0, 7.0)
                .returns(8.0, 14.0)
                .returns(16.0, 28.0);
    }

    @Test
    public void movingMax() {
        FunctionAssert.assertThat(DirtyFunctions.movingMax(3))
                .returns(1.0, 1.0)
                .returns(1.0, 1.0)
                .returns(3.0, 3.0)
                .returns(2.0, 3.0)
                .returns(1.0, 3.0)
                .returns(1.0, 2.0);
    }

    @Test
    public void movingMin() {
        FunctionAssert.assertThat(DirtyFunctions.movingMin(3))
                .returns(1.0, 1.0)
                .returns(3.0, 1.0)
                .returns(2.0, 1.0)
                .returns(3.0, 2.0)
                .returns(3.0, 2.0)
                .returns(3.0, 3.0);
    }

    @Test
    public void shift_0() {
        FunctionAssert.assertThat(DirtyFunctions.shift(0))
                .returns(1.0, 1.0)
                .returns(2.0, 2.0);
    }

    @Test
    public void shift_1() {
        FunctionAssert.assertThat(DirtyFunctions.shift(1))
                .returns(1.0, 1.0)
                .returns(2.0, 1.0)
                .returns(3.0, 2.0);
    }

    @Test
    public void shift_n() {
        FunctionAssert.assertThat(DirtyFunctions.shift(3))
                .returns(1.0, 1.0)
                .returns(2.0, 1.0)
                .returns(3.0, 1.0)
                .returns(4.0, 1.0)
                .returns(5.0, 2.0)
                .returns(6.0, 3.0)
                .returns(7.0, 4.0);
    }

    @Test
    public void quantile_large_window() {

        //large window - precision is OK

        Function<Double, Double> quantile = DirtyFunctions.movingQuantile(100, 0.4);

        for(int i=1; i<50; i++) {
            quantile.apply((double)i);
        }
        assertThat(quantile.apply(50d)).isEqualTo(20);

        for(int i=51; i<100; i++) {
            quantile.apply((double)i);
        }
        assertThat(quantile.apply(100d)).isEqualTo(40);

        for(int i=101; i<150; i++) {
            quantile.apply((double)i);
        }
        assertThat(quantile.apply(150d)).isEqualTo(90);

        for(int i=150; i>100; i--) {
            quantile.apply((double)i);
        }
        assertThat(quantile.apply(100d)).isEqualTo(120);
    }

    @Test
    public void quantile_small_window() {
        //small window - quite imprecise
        Function<Double, Double> quantile = DirtyFunctions.movingQuantile(3, 0.5);
        assertThat(quantile.apply(1d)).isEqualTo(1);
        assertThat(quantile.apply(2d)).isEqualTo(1);
        assertThat(quantile.apply(3d)).isEqualTo(2);
        assertThat(quantile.apply(4d)).isEqualTo(3);
        assertThat(quantile.apply(5d)).isEqualTo(4);
        assertThat(quantile.apply(4d)).isEqualTo(4);
        assertThat(quantile.apply(3d)).isEqualTo(4);
    }

    @Test
    public void quantile_zeroQuantile() {
        Function<Double, Double> quantile = DirtyFunctions.movingQuantile(3, 0.0);
        assertThat(quantile.apply(1d)).isEqualTo(1);
        assertThat(quantile.apply(2d)).isEqualTo(1);
        assertThat(quantile.apply(3d)).isEqualTo(1);
        assertThat(quantile.apply(4d)).isEqualTo(2);
        assertThat(quantile.apply(5d)).isEqualTo(3);
        assertThat(quantile.apply(4d)).isEqualTo(4);
        assertThat(quantile.apply(3d)).isEqualTo(3);
    }

    @Test
    public void quantile_oneQuantile() {
        Function<Double, Double> quantile = DirtyFunctions.movingQuantile(3, 1.0);
        assertThat(quantile.apply(1d)).isEqualTo(1);
        assertThat(quantile.apply(2d)).isEqualTo(2);
        assertThat(quantile.apply(3d)).isEqualTo(3);
        assertThat(quantile.apply(4d)).isEqualTo(4);
        assertThat(quantile.apply(5d)).isEqualTo(5);
        assertThat(quantile.apply(4d)).isEqualTo(5);
        assertThat(quantile.apply(3d)).isEqualTo(5);
    }
}
