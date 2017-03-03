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

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

import static com.jmt.testutil.Parse.dateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InstantSortedArrayTest {

    @Test
    public void initFromLongs() {

        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);

        assertEquals(3, array.size());

        assertEquals(0L, array.get(0).toEpochMilli());
        assertEquals(2000000000L, array.get(2).toEpochMilli());

        assertEquals(array.get(0), dateTime("01/01/1970 00:00:00 UTC"));
        assertEquals(array.get(2), dateTime("24/01/1970 03:33:20 UTC"));
    }

    @Test
    public void initFromDates() {

        InstantSortedArray array = InstantSortedArray.of(
                dateTime("01/01/1970 00:00:00 UTC"),
                dateTime("12/01/1970 13:46:40 UTC"),
                dateTime("24/01/1970 03:33:20 UTC")
        );

        assertEquals(3, array.size());

        assertEquals(0L, array.get(0).toEpochMilli());
        assertEquals(2000000000L, array.get(2).toEpochMilli());

        assertEquals(array.get(0), dateTime("01/01/1970 00:00:00 UTC"));
        assertEquals(array.get(2), dateTime("24/01/1970 03:33:20 UTC"));
    }

    @Test
    public void arrayNotSorted() {
        assertThatThrownBy(
                () -> InstantSortedArray.ofMillisNoClone(0, 2000000000, 1000000000, 4000000000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not in ascending order");
    }

    @Test
    public void find() {
        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);
        assertEquals(1, array.findLE(dateTime("12/01/1970 13:46:40 UTC")));
        assertEquals(0, array.findLT(dateTime("12/01/1970 13:46:40 UTC")));
        assertEquals(1, array.findGE(dateTime("12/01/1970 13:46:40 UTC")));
        assertEquals(2, array.findGT(dateTime("12/01/1970 13:46:40 UTC")));
        assertEquals(1, array.find(dateTime("12/01/1970 13:46:40 UTC")));
    }

    @Test
    public void map() {
        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);
        Array<Instant> mapped = array.map(instant -> instant.plusSeconds(3));
        assertThat(mapped.asList()).containsExactly(
                dateTime("01/01/1970 00:00:03 UTC"),
                dateTime("12/01/1970 13:46:43 UTC"),
                dateTime("24/01/1970 03:33:23 UTC")
        );
    }

    @Test
    public void map_toDifferentType() {
        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);
        Array<Long> mapped = array.map(GenericArray.builder(0), instant -> instant.getEpochSecond());
        assertThat(mapped.asList()).containsExactly(0L, 1000000L, 2000000L);
    }

    @Test
    public void map_sorted() {
        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);
        SortedArray<Instant> mapped = array.mapSorted(instant -> instant.plusSeconds(3));
        assertThat(mapped.asList()).containsExactly(
                dateTime("01/01/1970 00:00:03 UTC"),
                dateTime("12/01/1970 13:46:43 UTC"),
                dateTime("24/01/1970 03:33:23 UTC")
        );
    }

    @Test
    public void map_sorted_toDifferentType() {
        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);
        SortedArray<Long> mapped = array.mapSorted(GenericSortedArray.<Long>builder(0), instant -> instant.getEpochSecond());
        assertThat(mapped.asList()).containsExactly(0L, 1000000L, 2000000L);
    }

    @Test
    public void asList() {

        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);

        assertThat(array.asList()).containsExactly(
                dateTime("01/01/1970 00:00:00 UTC"),
                dateTime("12/01/1970 13:46:40 UTC"),
                dateTime("24/01/1970 03:33:20 UTC")
        );
    }

    //A hack to quickly deal with JUnit API change
    private void assertEquals(Object v1, Object v2) {
        Assert.assertEquals(v1, v2);
    }
}
