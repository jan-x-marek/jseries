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

public class ArraySearchUtilTest extends Assert {

    @Test
    public void longFindLT() throws Exception {

        try {
            ArraySearchUtil.findLT(null, 2);
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(-1, ArraySearchUtil.findLT(new long[0], 2));

        long[] array = new long[]{2, 5, 5, 8};

        assertEquals(-1, ArraySearchUtil.findLT(array, 1));
        assertEquals(-1, ArraySearchUtil.findLT(array, 2));
        assertEquals(0, ArraySearchUtil.findLT(array, 3));
        assertEquals(0, ArraySearchUtil.findLT(array, 5));
        assertEquals(2, ArraySearchUtil.findLT(array, 6));
        assertEquals(2, ArraySearchUtil.findLT(array, 8));
        assertEquals(3, ArraySearchUtil.findLT(array, 9));
        assertEquals(3, ArraySearchUtil.findLT(array, 20));
    }

    @Test
    public void longFindLE() throws Exception {

        try {
            ArraySearchUtil.findLE(null, 2);
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(-1, ArraySearchUtil.findLE(new long[0], 2));

        long[] array = new long[]{2, 5, 5, 8};

        assertEquals(-1, ArraySearchUtil.findLE(array, 1));
        assertEquals(0, ArraySearchUtil.findLE(array, 2));
        assertEquals(0, ArraySearchUtil.findLE(array, 3));
        assertEquals(1, ArraySearchUtil.findLE(array, 5));
        assertEquals(2, ArraySearchUtil.findLE(array, 6));
        assertEquals(3, ArraySearchUtil.findLE(array, 8));
        assertEquals(3, ArraySearchUtil.findLE(array, 9));
        assertEquals(3, ArraySearchUtil.findLE(array, 20));
    }

    @Test
    public void longFindGE() throws Exception {

        try {
            ArraySearchUtil.findGE(null, 2);
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(0, ArraySearchUtil.findGE(new long[0], 2));

        long[] array = new long[]{2, 5, 5, 8};

        assertEquals(0, ArraySearchUtil.findGE(array, 1));
        assertEquals(0, ArraySearchUtil.findGE(array, 2));
        assertEquals(1, ArraySearchUtil.findGE(array, 3));
        assertEquals(1, ArraySearchUtil.findGE(array, 5));
        assertEquals(3, ArraySearchUtil.findGE(array, 6));
        assertEquals(3, ArraySearchUtil.findGE(array, 8));
        assertEquals(4, ArraySearchUtil.findGE(array, 9));
        assertEquals(4, ArraySearchUtil.findGE(array, 20));
    }

    @Test
    public void longFindGT() throws Exception {

        try {
            ArraySearchUtil.findGT(null, 2);
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(0, ArraySearchUtil.findGT(new long[0], 2));

        long[] array = new long[]{2, 5, 5, 8};

        assertEquals(0, ArraySearchUtil.findGT(array, 1));
        assertEquals(1, ArraySearchUtil.findGT(array, 2));
        assertEquals(1, ArraySearchUtil.findGT(array, 3));
        assertEquals(3, ArraySearchUtil.findGT(array, 5));
        assertEquals(3, ArraySearchUtil.findGT(array, 6));
        assertEquals(4, ArraySearchUtil.findGT(array, 8));
        assertEquals(4, ArraySearchUtil.findGT(array, 9));
        assertEquals(4, ArraySearchUtil.findGT(array, 20));
    }

    @Test
    public void longFind() throws Exception {

        try {
            ArraySearchUtil.find(null, 2);
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(-1, ArraySearchUtil.find(new long[0], 2));

        long[] array = new long[]{2, 5, 5, 8};

        assertEquals(-1, ArraySearchUtil.find(array, 1));
        assertEquals(0, ArraySearchUtil.find(array, 2));
        assertEquals(-2, ArraySearchUtil.find(array, 3));
        assertEquals(1, ArraySearchUtil.find(array, 5));
        assertEquals(-4, ArraySearchUtil.find(array, 6));
        assertEquals(3, ArraySearchUtil.find(array, 8));
        assertEquals(-5, ArraySearchUtil.find(array, 9));
        assertEquals(-5, ArraySearchUtil.find(array, 20));
    }

    @Test
    public void genericFindLT() throws Exception {

        try {
            ArraySearchUtil.findLT(null, "2");
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(-1, ArraySearchUtil.findLT(new String[0], "2"));

        String[] array = new String[]{"2", "5", "5", "8"};

        assertEquals(-1, ArraySearchUtil.findLT(array, "1"));
        assertEquals(-1, ArraySearchUtil.findLT(array, "2"));
        assertEquals(0, ArraySearchUtil.findLT(array, "3"));
        assertEquals(0, ArraySearchUtil.findLT(array, "5"));
        assertEquals(2, ArraySearchUtil.findLT(array, "6"));
        assertEquals(2, ArraySearchUtil.findLT(array, "8"));
        assertEquals(3, ArraySearchUtil.findLT(array, "9"));
        assertEquals(3, ArraySearchUtil.findLT(array, "Z"));
    }

    @Test
    public void genericFindLE() throws Exception {

        try {
            ArraySearchUtil.findLE(null, "2");
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(-1, ArraySearchUtil.findLE(new String[0], "2"));

        String[] array = new String[]{"2", "5", "5", "8"};

        assertEquals(-1, ArraySearchUtil.findLE(array, "1"));
        assertEquals(0, ArraySearchUtil.findLE(array, "2"));
        assertEquals(0, ArraySearchUtil.findLE(array, "3"));
        assertEquals(1, ArraySearchUtil.findLE(array, "5"));
        assertEquals(2, ArraySearchUtil.findLE(array, "6"));
        assertEquals(3, ArraySearchUtil.findLE(array, "8"));
        assertEquals(3, ArraySearchUtil.findLE(array, "9"));
        assertEquals(3, ArraySearchUtil.findLE(array, "Z"));
    }

    @Test
    public void genericFindGE() throws Exception {

        try {
            ArraySearchUtil.findGE(null, "2");
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(0, ArraySearchUtil.findGE(new String[0], "2"));

        String[] array = new String[]{"2", "5", "5", "8"};

        assertEquals(0, ArraySearchUtil.findGE(array, "1"));
        assertEquals(0, ArraySearchUtil.findGE(array, "2"));
        assertEquals(1, ArraySearchUtil.findGE(array, "3"));
        assertEquals(1, ArraySearchUtil.findGE(array, "5"));
        assertEquals(3, ArraySearchUtil.findGE(array, "6"));
        assertEquals(3, ArraySearchUtil.findGE(array, "8"));
        assertEquals(4, ArraySearchUtil.findGE(array, "9"));
        assertEquals(4, ArraySearchUtil.findGE(array, "Z"));
    }

    @Test
    public void genericFindGT() throws Exception {

        try {
            ArraySearchUtil.findGT(null, "2");
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(0, ArraySearchUtil.findGT(new String[0], "2"));

        String[] array = new String[]{"2", "5", "5", "8"};

        assertEquals(0, ArraySearchUtil.findGT(array, "1"));
        assertEquals(1, ArraySearchUtil.findGT(array, "2"));
        assertEquals(1, ArraySearchUtil.findGT(array, "3"));
        assertEquals(3, ArraySearchUtil.findGT(array, "5"));
        assertEquals(3, ArraySearchUtil.findGT(array, "6"));
        assertEquals(4, ArraySearchUtil.findGT(array, "8"));
        assertEquals(4, ArraySearchUtil.findGT(array, "9"));
        assertEquals(4, ArraySearchUtil.findGT(array, "Z"));
    }

    @Test
    public void genericFind() throws Exception {

        try {
            ArraySearchUtil.find(null, "2");
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals(-1, ArraySearchUtil.find(new String[0], "2"));

        String[] array = new String[]{"2", "5", "5", "8"};

        assertEquals(-1, ArraySearchUtil.find(array, "1"));
        assertEquals(0, ArraySearchUtil.find(array, "2"));
        assertEquals(-2, ArraySearchUtil.find(array, "3"));
        assertEquals(1, ArraySearchUtil.find(array, "5"));
        assertEquals(-4, ArraySearchUtil.find(array, "6"));
        assertEquals(3, ArraySearchUtil.find(array, "8"));
        assertEquals(-5, ArraySearchUtil.find(array, "9"));
        assertEquals(-5, ArraySearchUtil.find(array, "Z"));
    }
}
