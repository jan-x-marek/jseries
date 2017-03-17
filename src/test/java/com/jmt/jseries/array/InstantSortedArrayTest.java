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

import org.junit.Test;

import java.time.Instant;
import java.util.stream.LongStream;

import static com.jmt.testutil.Parse.dateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InstantSortedArrayTest {

    @Test
    public void initFromLongs() {

        InstantSortedArray array = InstantSortedArray.ofMillisNoClone(0, 1000000000, 2000000000);

        assertThat(array.size()).isEqualTo(3);

        assertThat(array.get(0).toEpochMilli()).isEqualTo(0L);
        assertThat(array.get(2).toEpochMilli()).isEqualTo(2000000000L);

        assertThat(array.get(0)).isEqualTo(dateTime("01/01/1970 00:00:00 UTC"));
        assertThat(array.get(2)).isEqualTo(dateTime("24/01/1970 03:33:20 UTC"));
    }

    @Test
    public void initFromDates() {

        InstantSortedArray array = InstantSortedArray.of(
                dateTime("01/01/1970 00:00:00 UTC"),
                dateTime("12/01/1970 13:46:40 UTC"),
                dateTime("24/01/1970 03:33:20 UTC")
        );

        assertThat(array.size()).isEqualTo(3);

        assertThat(array.get(0).toEpochMilli()).isEqualTo(0L);
        assertThat(array.get(2).toEpochMilli()).isEqualTo(2000000000L);

        assertThat(array.get(0)).isEqualTo(dateTime("01/01/1970 00:00:00 UTC"));
        assertThat(array.get(2)).isEqualTo(dateTime("24/01/1970 03:33:20 UTC"));
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
        assertThat(array.findLE(dateTime("12/01/1970 13:46:40 UTC"))).isEqualTo(1);
        assertThat(array.findLT(dateTime("12/01/1970 13:46:40 UTC"))).isEqualTo(0);
        assertThat(array.findGE(dateTime("12/01/1970 13:46:40 UTC"))).isEqualTo(1);
        assertThat(array.findGT(dateTime("12/01/1970 13:46:40 UTC"))).isEqualTo(2);
        assertThat(array.find(dateTime("12/01/1970 13:46:40 UTC"))).isEqualTo(1);
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
    
    @Test
    public void linspace() {
    	assertThat(InstantSortedArray.linspace(0, 10, 2).asList()).containsExactly(toInstants(0L, 2L, 4L, 6L, 8L));
    	assertThat(InstantSortedArray.linspace(0, 9, 2).asList()).containsExactly(toInstants(0L, 2L, 4L, 6L, 8L));
    	assertThat(InstantSortedArray.linspace(1, 1, 1).asList()).isEmpty();
    }
    
    @Test
    public void linspace_large() {
    	InstantSortedArray a = InstantSortedArray.linspace(0, 1000000, 1);
		assertThat(a.size()).isEqualTo(1000000);
		assertThat(a.get(654321)).isEqualTo(Instant.ofEpochMilli(654321));
    }
    
    @Test
    public void linspace_too_large() {
    	assertThatThrownBy(() -> InstantSortedArray.linspace(0, 1000000000000L, 1))
    		.isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void linspace_negative() {
    	assertThatThrownBy(() -> InstantSortedArray.linspace(0, 5, -1))
			.isInstanceOf(IllegalArgumentException.class);
    	assertThatThrownBy(() -> InstantSortedArray.linspace(0, -5, -1))
			.isInstanceOf(IllegalArgumentException.class);
    	assertThatThrownBy(() -> InstantSortedArray.linspace(0, -5, 1))
			.isInstanceOf(IllegalArgumentException.class);
    }
    
    private Instant[] toInstants(long... millis) {
    	return LongStream.of(millis)
    			.mapToObj(t -> Instant.ofEpochMilli(t))
    			.toArray(i -> new Instant[i]);
    }
}
