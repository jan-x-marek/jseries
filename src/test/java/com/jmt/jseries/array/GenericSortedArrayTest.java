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

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GenericSortedArrayTest {

    @Test
    public void initAndGet() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        assertEquals(3, array.size());
        assertEquals("a", array.get(0));
        assertEquals("c", array.get(2));
        assertEquals("a", array.getFirst());
        assertEquals("c", array.getLast());
    }

    @Test
    public void arrayNotSorted() {
        assertThatThrownBy(
                () -> GenericSortedArray.of("b", "a"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not in ascending order");
    }

    @Test
    public void find() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        assertEquals(1, array.findLE("ba"));
        assertEquals(0, array.findLT("b"));
        assertEquals(1, array.findGE("b"));
        assertEquals(2, array.findGT("b"));
        assertEquals(1, array.find("b"));
    }

    @Test
    public void map() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        Array<String> mapped = array.map(s -> s + "x");
        assertThat(mapped.asList()).containsExactly("ax", "bx", "cx");
    }

    @Test
    public void map_toDifferentType() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        Array<Character> mapped = array.map(GenericArray.builder(0), s -> s.charAt(0));
        assertThat(mapped.asList()).containsExactly('a', 'b', 'c');
    }

    @Test
    public void map_sorted() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        SortedArray<String> mapped = array.mapSorted(s -> s + "x");
        assertThat(mapped.asList()).containsExactly("ax", "bx", "cx");
    }

    @Test
    public void map_sorted_wronTargetOrder() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        AtomicInteger i = new AtomicInteger(2);
        assertThatThrownBy(() -> array.mapSorted(s -> i.decrementAndGet() + s))
                .hasMessageContaining("The array is not in ascending order");
    }

    @Test
    public void map_sorted_toDifferentType() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        SortedArray<Character> mapped = array.mapSorted(GenericSortedArray.<Character>builder(0), s -> s.charAt(0));
        assertThat(mapped.asList()).containsExactly('a', 'b', 'c');
    }

    @Test
    public void asList() {
        GenericSortedArray<String> array = GenericSortedArray.of("a", "b", "c");
        assertThat(array.asList()).containsExactly("a", "b", "c");
    }

    //A hack to quickly deal with JUnit API change
    private void assertEquals(Object v1, Object v2) {
        Assert.assertEquals(v1, v2);
    }
}
