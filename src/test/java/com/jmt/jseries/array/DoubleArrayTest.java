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

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleArrayTest {

    @Test
    public void testAsList() {
        DoubleArray a = DoubleArray.ofNoClone(-1.0, 1.0, 2.0, 0.0);
        assertThat(a.asList()).containsExactly(-1.0, 1.0, 2.0, 0.0);
    }

    @Test
    public void testGet() {

        DoubleArray a = DoubleArray.ofNoClone(-1.0, 1.0, 2.0, 0.0);

        assertEquals(4, a.size());

        assertEquals(-1.0, a.get(0));
        assertEquals(1.0, a.get(1));

        assertEquals(-1.0, a.getFirst());
        assertEquals(0.0, a.getLast());
    }

    @Test
    public void testGetWrongIndex() {

        DoubleArray a = DoubleArray.ofNoClone(-1.0, 1.0, 2.0, 0.0);

        try {
            a.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            a.get(5);
            fail();
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void testMap() {
        DoubleArray array = DoubleArray.ofNoClone(1.0, 2.0);
        assertThat(array.map(d -> d + 1).asList()).containsExactly(2.0, 3.0);
    }

    @Test
    public void testMap_toDifferentType() {
        DoubleArray array = DoubleArray.ofNoClone(1.0, 2.0);
        Array<String> mapped = array.map(GenericArray.builder(0), d -> "" + d);
        assertThat(mapped.asList()).containsExactly("1.0", "2.0");
    }

    //A hack to quickly deal with JUnit API change
    private void assertEquals(Object v1, Object v2) {
        Assert.assertEquals(v1, v2);
    }

    private void fail() {
        Assert.fail();
    }
}
