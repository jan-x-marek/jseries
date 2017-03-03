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
package com.jmt.testutil;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;

import java.util.function.Function;

public class FunctionAssert<T, R> extends AbstractAssert<FunctionAssert<T, R>, Function<T, R>> {

    public static <T, R> FunctionAssert<T, R> assertThat(Function<T, R> f) {
        return new FunctionAssert<>(f);
    }

    private FunctionAssert(Function<T, R> f) {
        super(f, FunctionAssert.class);
    }

    public FunctionAssert<T, R> returns(T t, R r) {
        Assertions.assertThat(actual.apply(t)).isEqualTo(r);
        return this;
    }

    public FunctionAssert<T, R> returnsCloseTo(T t, Double r, Offset<Double> offset) {
        Assertions.assertThat((Double) actual.apply(t)).isCloseTo((Double) r, offset);
        return this;
    }
}
