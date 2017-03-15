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

import static org.assertj.core.api.Assertions.*;

public class DoubleArrayTest {

    @Test
    public void testAsList() {
        DoubleArray a = DoubleArray.ofNoClone(-1.0, 1.0, 2.0, 0.0);
        assertThat(a.asList()).containsExactly(-1.0, 1.0, 2.0, 0.0);
    }

    @Test
    public void testGet() {
        DoubleArray a = DoubleArray.ofNoClone(-1.0, 1.0, 2.0, 0.0);
        assertThat(a.size()).isEqualTo(4);
        assertThat(a.get(0)).isEqualTo(-1.0);
        assertThat(a.get(1)).isEqualTo(1.0);
        assertThat(a.getFirst()).isEqualTo(-1.0);
        assertThat(a.getLast()).isEqualTo(0.0);
    }

    @Test
    public void testGetWrongIndex() {
        DoubleArray a = DoubleArray.ofNoClone(-1.0, 1.0, 2.0, 0.0);
        assertThatThrownBy(() -> a.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> a.get(5)).isInstanceOf(IndexOutOfBoundsException.class);
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
    
    @Test
    public void linspace() {
    	assertThat(DoubleArray.linspace(0, 5, 1).asList()).containsExactly(0d,1d,2d,3d,4d);
    	assertThat(DoubleArray.linspace(0, 4.1, 1).asList()).containsExactly(0d,1d,2d,3d,4d);
    	assertThat(DoubleArray.linspace(1, 2, 0.5).asList()).containsExactly(1d,1.5);
    	assertThat(DoubleArray.linspace(1, 1, 1).asList()).isEmpty();
    	assertThat(DoubleArray.linspace(0, -3, -1).asList()).containsExactly(0d,-1d,-2d);
    	assertThat(DoubleArray.linspace(0, -3.1, -1).asList()).containsExactly(0d,-1d,-2d);
    }
    
    @Test
    public void linspace_large() {
    	DoubleArray a = DoubleArray.linspace(0, 1000000, 1);
		assertThat(a.size()).isEqualTo(1000000);
		assertThat(a.get(654321)).isEqualTo(654321d);
    }
    
    @Test
    public void linspace_too_large() {
    	assertThatThrownBy(() -> DoubleArray.linspace(0, 1000000000, 0.01))
    		.isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void linspace_negative() {
    	assertThatThrownBy(() -> DoubleArray.linspace(0, 5, -1))
    		.isInstanceOf(IllegalArgumentException.class);
    	assertThatThrownBy(() -> DoubleArray.linspace(0, -5, 1))
			.isInstanceOf(IllegalArgumentException.class);
    }
}
