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

import com.jmt.jseries.array.DoubleArray;
import com.jmt.jseries.array.GenericArray;
import com.jmt.jseries.array.InstantSortedArray;
import com.jmt.testutil.Parse;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SeriesImplTest {

    @Test
    public void testValuesAccess() {

        InstantDoubleSeries dts = InstantDoubleSeries.create(
                "foobar",
                new Instant[]{Parse.date("01/01/2011"), Parse.date("03/01/2011"), Parse.date("05/01/2011")},
                new double[]{0.0, 1.0, 2.0},
                -1.0);

        assertEquals(3, dts.size());

        assertEquals(-1.0, dts.apply(Parse.date("01/01/2010")));
        assertEquals(0.0, dts.apply(Parse.date("01/01/2011")));
        assertEquals(0.0, dts.apply(Parse.date("02/01/2011")));
        assertEquals(1.0, dts.apply(Parse.date("03/01/2011")));
        assertEquals(2.0, dts.apply(Parse.date("05/01/2011")));
        assertEquals(2.0, dts.apply(Parse.date("05/01/3011")));

        assertEquals(-1.0, dts.value(Parse.date("01/01/2010")));
        assertEquals(0.0, dts.value(Parse.date("02/01/2011")));
        assertEquals(1.0, dts.value(Parse.date("03/01/2011")));

        assertEquals("foobar", dts.toString());

        assertEquals(3, dts.domain().size());
        assertEquals(3, dts.values().size());
    }

    @Test
    public void testDefaultName() {
        InstantDoubleSeries ds = InstantDoubleSeries.create(new Instant[]{}, new double[]{});
        assertEquals(ds.getClass().getSimpleName(), ds.toString());
    }

    @Test
    public void testDefaultValue() {

        InstantDoubleSeries dts = InstantDoubleSeries.create(
                null,
                new Instant[]{Parse.date("01/01/2011"), Parse.date("03/01/2011"), Parse.date("05/01/2011")},
                new double[]{0.0, 1.0, 2.0},
                Double.NaN);

        assertEquals(Double.NaN, dts.defaultValue());
        assertEquals(Double.NaN, dts.apply(Parse.date("01/01/2010")));
        assertEquals(0.0, dts.apply(Parse.date("01/01/2011")));
    }

    @Test
    public void testNonMatchingLengths() {
        try {
            InstantDoubleSeries.create(
                    new Instant[]{Parse.date("01/01/2011"), Parse.date("03/01/2011")},
                    new double[]{0.0, 1.0, 2.0});
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testTimlessConstant() {
        InstantDoubleSeries c1 = InstantDoubleSeries.create("xxx", new Instant[0], new double[0], 4.2);
        assertEquals(0, c1.size());
        assertEquals(4.2, c1.apply(Parse.date("01/01/2010")));
        assertEquals("xxx", c1.toString());
    }

    @Test
    public void withName() {

        Series<Instant, Double> s1 = InstantDoubleSeries.create(
                "foobar",
                new Instant[]{Parse.date("01/01/2011")},
                new double[]{1.0},
                -1.0);

        Series<Instant, Double> s2 = s1.withName("baz");

        assertSame(s1.domain(), s2.domain());
        assertSame(s1.values(), s2.values());
        assertEquals(s1.defaultValue(), s2.defaultValue());
        assertEquals("baz", s2.name());
    }

    @Test
    public void map() {

        Series<Instant, Double> dts = InstantDoubleSeries.create(
                "foobar",
                new Instant[]{Parse.date("01/01/2011"), Parse.date("03/01/2011")},
                new double[]{1.0, 2.0},
                -1.0);

        Series<Instant, Double> mapped = dts.mapValues(x -> x / 2);

        assertEquals(2, mapped.size());

        assertEquals(-1.0, mapped.apply(Parse.date("01/01/2010")));
        assertEquals(0.5, mapped.apply(Parse.date("01/01/2011")));
        assertEquals(1.0, mapped.apply(Parse.date("03/01/2011")));

        assertEquals("foobar", mapped.toString());
    }

    @Test
    public void map_toDifferentType() {

        Series<Instant, Double> dts = InstantDoubleSeries.create(
                "foobar",
                new Instant[]{Parse.date("01/01/2011"), Parse.date("03/01/2011")},
                new double[]{1.0, 2.0},
                -1.0);

        Series<Instant, String> mapped = dts.mapValues(GenericArray.builder(0), "*", x -> "" + x);

        assertEquals(2, mapped.size());

        assertEquals("*", mapped.apply(Parse.date("01/01/2010")));
        assertEquals("1.0", mapped.apply(Parse.date("01/01/2011")));
        assertEquals("2.0", mapped.apply(Parse.date("03/01/2011")));

        assertEquals("foobar", mapped.toString());
    }

    @Test
    public void zipWithValues_series() {

        Series<Instant, Double> s1 = InstantDoubleSeries.create(
                "foo",
                InstantSortedArray.ofMillis(1, 5, 10),
                DoubleArray.of(1.0, 2.0, 3.0),
                -1.0);

        Series<Instant, Double> s2 = InstantDoubleSeries.create(
                "bar",
                InstantSortedArray.ofMillis(3, 10),
                DoubleArray.of(1.0, 2.0),
                -10.0);

        Series<Instant, Double> result = s1.zipWithValues(s2, (x, y) -> x + y);

        assertThat(result.values().asList()).containsExactly(-9.0, 3.0, 5.0);
        assertThat(result.domain()).isSameAs(s1.domain());
        assertThat(result.name()).isEqualTo("foo");
        assertThat(result.defaultValue()).isEqualTo(-1.0);
    }

    @Test
    public void zipWithValues_plainFunction() {

        Series<Instant, Double> s1 = InstantDoubleSeries.create(
                "foo",
                InstantSortedArray.ofMillis(1, 5, 10),
                DoubleArray.of(1.0, 2.0, 3.0),
                -1.0);

        Series<Instant, Double> result = s1.zipWithValues(t -> (double) t.toEpochMilli(), (x, y) -> x + y);

        assertThat(result.values().asList()).containsExactly(2.0, 7.0, 13.0);
        assertThat(result.domain()).isSameAs(s1.domain());
        assertThat(result.name()).isEqualTo("foo");
        assertThat(result.defaultValue()).isEqualTo(-1.0);
    }

    @Test
    public void zipWithValues_array() {

        Series<Instant, Double> s = InstantDoubleSeries.create(
                "foo",
                InstantSortedArray.ofMillis(1, 5, 10),
                DoubleArray.of(1.0, 2.0, 3.0),
                -1.0);

        DoubleArray a = DoubleArray.of(10.0, 11.0, 12.0);

        Series<Instant, Double> result = s.zipWithValues(a, (x, y) -> x + y);

        assertThat(result.values().asList()).containsExactly(11.0, 13.0, 15.0);
        assertThat(result.domain()).isSameAs(s.domain());
        assertThat(result.name()).isEqualTo("foo");
        assertThat(result.defaultValue()).isEqualTo(-1.0);
    }

    @Test
    public void zipWithValues_array_wrongSize() {

        Series<Instant, Double> s = InstantDoubleSeries.create(
                "foo",
                InstantSortedArray.ofMillis(1, 5, 10),
                DoubleArray.of(1.0, 2.0, 3.0),
                -1.0);

        DoubleArray a = DoubleArray.of(10.0, 11.0);

        assertThatThrownBy(() -> s.zipWithValues(a, (x, y) -> x + y)).isInstanceOf(IllegalArgumentException.class);
    }

    //A hack to quickly deal with JUnit API change
    private void assertEquals(Object v1, Object v2) {
        Assert.assertEquals(v1, v2);
    }

    private void assertSame(Object v1, Object v2) {
        Assert.assertSame(v1, v2);
    }

    private void fail() {
        Assert.fail();
    }
}
