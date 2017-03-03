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

import java.util.Arrays;

public class ArraySearchUtil {

    public static int findGE(long[] a, long x) {
        int index = find(a, x);
        if (index < 0) {
            index = -index - 1;
        }
        return index;
    }

    public static int findGT(long[] a, long x) {
        int index = find(a, x);
        int length = a.length;
        if (index < 0) {
            index = -index - 1;
        } else {
            while (index < length && a[index] == x) index++;
        }
        return index;
    }

    public static int findLT(long[] a, long x) {
        int index = find(a, x);
        if (index < 0) {
            index = -index - 2;
        } else {
            while (index >= 0 && a[index] == x) index--;
        }
        return index;
    }

    public static int findLE(long[] a, long x) {
        int index = find(a, x);
        if (index < 0) {
            index = -index - 2;
        }
        return index;
    }

    public static int find(long[] a, long x) {
        return Arrays.binarySearch(a, x);
    }

    public static <T> int findGE(T[] a, T x) {
        int index = find(a, x);
        if (index < 0) {
            index = -index - 1;
        }
        return index;
    }

    public static <T> int findGT(T[] a, T x) {
        int index = find(a, x);
        int length = a.length;
        if (index < 0) {
            index = -index - 1;
        } else {
            while (index < length && a[index].equals(x)) index++;
        }
        return index;
    }

    public static <T> int findLT(T[] a, T x) {
        int index = find(a, x);
        if (index < 0) {
            index = -index - 2;
        } else {
            while (index >= 0 && a[index].equals(x)) index--;
        }
        return index;
    }

    public static <T> int findLE(T[] a, T x) {
        int index = find(a, x);
        if (index < 0) {
            index = -index - 2;
        }
        return index;
    }

    public static <T> int find(T[] a, T x) {
        return Arrays.binarySearch(a, x);
    }
}
