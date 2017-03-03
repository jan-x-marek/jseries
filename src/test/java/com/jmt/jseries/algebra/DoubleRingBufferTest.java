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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DoubleRingBufferTest {

    @Test
    public void invalidSize() {
        assertThatThrownBy(() -> new DoubleRingBuffer(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new DoubleRingBuffer(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void empty() {
        DoubleRingBuffer buffer = new DoubleRingBuffer(3);
        assertThat(buffer.size()).isEqualTo(0);
        assertThat(buffer).hasToString("[]");
        assertThatThrownBy(() -> buffer.last()).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> buffer.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void addAndGet() {

        DoubleRingBuffer buffer = new DoubleRingBuffer(3);

        buffer.add(1.0);
        assertThat(buffer.size()).isEqualTo(1);
        assertThat(buffer).hasToString("[1.0]");

        buffer.add(2.0);
        assertThat(buffer.size()).isEqualTo(2);
        assertThat(buffer).hasToString("[1.0, 2.0]");

        buffer.add(3.0);
        assertThat(buffer.size()).isEqualTo(3);
        assertThat(buffer).hasToString("[1.0, 2.0, 3.0]");

        buffer.add(4.0);
        assertThat(buffer.size()).isEqualTo(3);
        assertThat(buffer).hasToString("[2.0, 3.0, 4.0]");

        buffer.add(5.0);
        buffer.add(6.0);
        buffer.add(7.0);
        assertThat(buffer.size()).isEqualTo(3);
        assertThat(buffer).hasToString("[5.0, 6.0, 7.0]");
    }

    @Test
    public void accessors() {

        DoubleRingBuffer buffer = new DoubleRingBuffer(3);
        buffer.add(1.0);
        buffer.add(2.0);

        assertThat(buffer.first()).isEqualTo(1.0);
        assertThat(buffer.last()).isEqualTo(2.0);
        assertThat(buffer.get(0)).isEqualTo(1.0);
        assertThat(buffer.get(1)).isEqualTo(2.0);
        assertThatThrownBy(() -> buffer.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> buffer.get(2)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void size1() {
        DoubleRingBuffer buffer = new DoubleRingBuffer(1);
        buffer.add(1.0);
        assertThat(buffer).hasToString("[1.0]");
        buffer.add(2.0);
        assertThat(buffer).hasToString("[2.0]");
    }

    @Test
    public void toArray() {
        DoubleRingBuffer buffer = new DoubleRingBuffer(2);
        assertThat(buffer.toArray()).isEmpty();
        buffer.add(1.0);
        assertThat(buffer.toArray()).containsExactly(1.0);
        buffer.add(2.0);
        assertThat(buffer.toArray()).containsExactly(1.0, 2.0);
        buffer.add(3.0);
        assertThat(buffer.toArray()).containsExactly(2.0, 3.0);
    }
}
