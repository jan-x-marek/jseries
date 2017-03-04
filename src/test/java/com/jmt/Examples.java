package com.jmt;

import java.time.Instant;
import java.time.ZoneId;

import com.jmt.jseries.InstantDoubleSeries;
import com.jmt.jseries.Series;
import com.jmt.jseries.algebra.Binary;
import com.jmt.jseries.algebra.Moving;
import com.jmt.jseries.array.Array;
import com.jmt.jseries.array.DoubleArray;
import com.jmt.jseries.array.InstantSortedArray;
import com.jmt.jseries.array.SortedArray;

public class Examples {
	
	public static void main(String[] args) {
		//quickExample();
		array();
	}
	
	private static void quickExample() {
		
		//Get the historical prices from somewhere, e.g. http://finance.yahoo.com/ 
		Instant[] ibmDates = new Instant[]{Instant.ofEpochMilli(1000),Instant.ofEpochMilli(2000),Instant.ofEpochMilli(3000)};
		double[] ibmPrices = new double[]{100,110,120};
		Instant[] appleDates = new Instant[]{Instant.ofEpochMilli(1000),Instant.ofEpochMilli(2000),Instant.ofEpochMilli(3000)};
		double[] applePrices = new double[]{210,209,208};
		
		Series<Instant,Double> ibm = InstantDoubleSeries.create(ibmDates, ibmPrices);
		Series<Instant,Double> apple = InstantDoubleSeries.create(appleDates, applePrices);
		
		Series<Instant,Double> portfolioValue = 
				Binary.add(
						Binary.mul(ibm, 100),
						Binary.mul(apple, 200)
				);
		
		Series<Instant,Double> smoothedPortfolioValue = Moving.avg(portfolioValue, 30);
		
		System.out.println(smoothedPortfolioValue.values().asList().toString());
	}
	
	private static void array() {
		
		Array<Double> numbers1 = DoubleArray.of(1.0, 2.0, 3.0, 2.0, 1.0);
		Array<Double> numbers2 = numbers1.map(x -> x * 2 + 1);
		System.out.println(numbers2.asList().toString());
		
		SortedArray<Instant> times1 = InstantSortedArray.ofMillis(1000, 2000, 3000, 4000, 5000);
		SortedArray<Instant> times2 = times1.mapSorted(t -> t.atZone(ZoneId.of("UTC")).withYear(2000).toInstant());
		System.out.println(times2.asList().toString());
	}

}
