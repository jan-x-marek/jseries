package com.jmt;

import java.time.Instant;

import com.jmt.jseries.InstantDoubleSeries;
import com.jmt.jseries.Series;
import com.jmt.jseries.algebra.Binary;
import com.jmt.jseries.algebra.Moving;
import com.jmt.jseries.array.DoubleArray;
import com.jmt.jseries.array.InstantSortedArray;

public class PerformaceShowcase {
	
	public static void main(String[] args) {
		
		int size = 10_000_000;
		
		Series<Instant,Double> s0 = InstantDoubleSeries.create(
				InstantSortedArray.linspace(0, size, 1),
				DoubleArray.linspace(0, size, 1));
		
		long t0 = System.currentTimeMillis();
		
		Series<Instant,Double> s1 = Binary.add(s0, s0);
		
		long t1 = System.currentTimeMillis();
		System.out.println(t1-t0);
		
		Series<Instant,Double> s2 = Moving.avg(s1, 100);
		
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1);
	}
}
