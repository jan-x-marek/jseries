package com.jmt;

import java.time.Instant;

import com.jmt.jseries.InstantDoubleSeries;
import com.jmt.jseries.Series;
import com.jmt.jseries.algebra.Binary;
import com.jmt.jseries.algebra.Moving;
import com.jmt.jseries.array.Array;
import com.jmt.jseries.array.DoubleArray;
import com.jmt.jseries.array.GenericSortedArray;
import com.jmt.jseries.array.SortedArray;

public class Examples {
	
	public static void main(String[] args) {
		//quickExample();
		//array();
		sortedArray();
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
		Array<Double> a = DoubleArray.of(1.0, 2.0, 3.0);
		a.get(2);	//Retrieve an element
		a.asList();	//Convert to list
		a.map(x -> x * 2);	//Transform the values with a function
		System.out.println(a.asList().toString());
	}
	
	private static void sortedArray() {
		SortedArray<String> a = GenericSortedArray.of("a", "b", "e", "f");
		a.get(2); 		//Retrieve an element
		a.findLE("c"); 	//Return the index of the first element lower or equal "c"
		a.map(x -> x + "FOO");	//Transform the values with a function, the result is normal Array
		a.mapSorted(x -> x + "BAR");	//Transform the values with a function, the result is SortedArray
		System.out.println(a.asList().toString());
	}
}
