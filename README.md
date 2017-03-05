# JSeries

Ultra-lightweight time series library for Java 8

* Super-fast, memory-efficient
* Zero runtime dependencies, tiny jar
* Neat API, leveraging Java 8 features

## The Usecase

I need to process time series in several my applications. 
Sometimes it's financial data, such as historical stock prices,
sometimes it's data from mobile phone sensors, like accelerometer or microphone.
I need to perform operations like smoothing the series, various transformations,
zipping several series by date, and so on. I usually design the algorithm in 
a proper data analysis tool like Python data tools or R, where vectors and 
series are fist-class citizens, and then I need to transfer the resulting algorithm to Java, 
where it runs in production. 

This is where JSeries fits.

It operates on a similar level of abstraction
(high-level vector operations, no ugly for loops over arrays), 
so that the code from the data modeling tools can be easily translated to it.
It's pure Java with no dependencies, and aims to run as fast and memory-efficiently as Java can get.

Please, do not expect a full Java clone of Numpy. It gets nowhere near and has no such ambition. 
I support just a tiny subset of the functionality, which was enough to satisfy my practical needs:
only 1D arrays and time series, and a small set of predefined operations. 
Nevertheless, I believe there are many developers whose needs in this respect are equally limited and 
who can benefit from the library just like I did.

And the lib is easy to extend, you can quickly add the missing pieces of functionality on your own.

I use the library to process series sometimes containing tens of millions of elements,
and it takes just a fraction of second. Indeed, it has only a small performance 
overhead compared to manipulating bare primitive arrays directly,
but the API is much nicer and safer, the code much shorter and easier to understand, etc, etc.

## Quick Example

Let's say you own 100 shares of IBM and 200 shares of Apple,
and you want to calculate the value of your portfolio from historical prices of the two stocks.

```java
//Get the historical prices from somewhere, e.g. http://finance.yahoo.com/ 
Instant[] ibmDates = ...;
double[] ibmPrices = ...;
Instant[] appleDates = ...;
double[] applePrices = ...;

//Create time series containing IBM prices
Series<Instant,Double> ibm = InstantDoubleSeries.create(ibmDates, ibmPrices);
//Create time series containing Apple prices
Series<Instant,Double> apple = InstantDoubleSeries.create(appleDates, applePrices);

//Calculate the portfolio value. 
//Multiply IBM prices by 100 and Apple by 200, and add the resulting series.
//The "add" operation can deal with missing data (e.g. if Apple prices are missing for some days).
//The result is time series containing the historical value of the portfolio.
Series<Instant,Double> portfolioValue = 
		Binary.add(
				Binary.mul(ibm, 100),
				Binary.mul(apple, 200)
		);

//Just to be fancy, smooth the portfolio value with 30-day moving average.
Series<Instant,Double> smoothedPortfolioValue = Moving.avg(portfolioValue, 30);

//And print the result.
System.out.println(smoothedPortfolioValue.values().asList().toString());
```

## Build & Install

* Requirements: git, Java 8
* Clone the repo
  * ``git clone https://github.com/jan-x-marek/jseries``
* Build the project
  * ``./gradlew install``
* Use it
  * Add the resulting jar ``build/libs/jseries-1.0.jar`` to your classpath
  * Or add the artifact ``com.jmt:jseries:1.0`` to your gradle or maven dependencies (it's in the local repository only).  

## Stability

I have been using the code in production in several projects for more than a year, so I daresay it's safe and stable.

## Completeness

As noted above, the extent of the functionality (supported data types, various mappings and transformations) 
is quite limited. At the moment, it supports only the bits that I needed in my projects, 
and it certainly misses some obvious stuff that other users might need.
I intend to keep adding more stuff in the future, but only to a limited extent.
I am not planning to duplicate the entire Numpy, Pandas, or whatever, I want to keep it small and simple.

The library is easily extensible. If you like the overall concept, and miss a particular function,
please, drop me a message and I may decide to add it. Or, you are very welcome to add it on your own 
and send me a pull request.

Also, the javadoc is nearly non-existent at the moment, and it will hopefully improve.

## Random Design Notes

Data are stored only in memory, in Java arrays, specifically in primitive arrays wherever possible.
This helps to achieve the best speed and memory efficiency.

Everything is immutable. Arrays are encapsulated in immutable wrappers. 
It keeps the semantics simple and eliminates any concurrency troubles.
Well - there is one exception to this rule - DirtyFunctions - see the user guide.

There is only a single set of interfaces, used both for primitive and non-primitive types. 
For example, there is an Array<T> interface, which has a generic implementation for
an array of Objects, and a specific implementation DoubleArray (of type Array<Double>),
which stores the values as primitives internally, but has big Double in its type signature.
It greatly simplifies the API, compared to the standard Java APIs (functions, streams)
where every interface is copied many times for every primitive type.
Naturally, it might affect performance, due to some extra boxing/unboxing.
However, I did quite some performance benchmarking to see how much the generic
interface really slows things down, and the difference was small, certainly worth the tradeoff.
Apparently, Java 8 runtime is doing excellent job with runtime optimization.

The overall design tends to functional programming flavor (as far as Java permits), 
leveraging the cool new Java 8 stuff, such as lambdas and default method implementations.

## Quick User Guide

### Array

Array<T> is an interface providing simple immutable wrapper around Java array. 
There are two implementations at the moment. GenericArray that can contain any type,
and DoubleArray that internally contains primitive double[] in order to minimize memory usage.

```java
Array<Double> a = DoubleArray.of(1.0, 2.0, 3.0);
a.get(2);           //Retrieve an element
a.asList();	        //Convert to list
a.map(x -> x * 2);  //Transform the values with a function
System.out.println(a.asList().toString());
```

### SortedArray

Extension of Array, the values are sorted. Provides methods for efficient (binary) search.
There are two implementations: GenericSortedArray that can store any type, and InstantArray
that stores instants in primitive long[] in order to minimize memory usage.

```java
SortedArray<String> a = GenericSortedArray.of("a", "b", "e", "f");
a.get(2); 		               //Retrieve an element
a.findLE("c"); 	               //Return the index of the first element lower or equal "c" - which is 1
a.map(x -> x + "FOO");	       //Transform the values with a function, the result is normal Array
a.mapSorted(x -> x + "BAR");   //Transform the values with a function, the result is SortedArray
System.out.println(a.asList().toString());
```

### Series

Now the cool stuff comes. Series<T,R> is an interface that puts together two things: 
a SortedArray, called *domain*, and an Array, called *values*.
The domain contains some kind of points in time in ascending order,
and the values contain some kind of value, or measurement, for each of the given time points.
For example, the domain can contain days, and the values can be closing prices of a stock for each day.
Naturally, the domain and the values can contain objects of any type,
and can be any specifically optimized implementations of SortedArray and Array.
In the further examples, I will work with InstantDoubleSeries, 
which uses the optimized SortedInstantArray and DoubleArray internally.

```java
//InstantDoubleSeries provides a bunch of different factory methods.
Series<Instant,Double> s = InstantDoubleSeries.create(
		new Instant[]{i1, i2, i3}, 
		new double[]{v1, v2, v3});
```

The concept of Series is of a dualistic nature. 

On the one hand, we can see it as a plain pair of arrays, 
and access the arrays or their elements individually as discrete points.

```java
s.domain()     //the entire domain array
s.values()     //the entire values array
s.domain(7);   //the 7th time point
s.value(7);    //the 7th value
s.size();      //size of the domain (same as size of the values)

```

On the other hand, Series can be seen as a total function 
(i.e. a function defined for any point in time).
And indeed, Series extends the java.util.Function interface. 
The call ``series.apply(t)`` searches 
for the closest lower or equal time point  in the domain, 
and returns the corresponding value. If there is no such time point in the domain, 
it returns some default value (optionally specified in the constructor).
We can imagine the data points in the series as measurements from a sensor, 
and a request for the value at any given time t (``series.apply(t)``) 
returns the last known value at that time.

Turning the Series into a total function opens us great possibilities, 
as we can integrate it with all the functional infrastructure of Java
(streams, function composition, whatever).

There is a bunch of tools to transform the series.
```java
//Create new series with the same domain, 
//and the values transformed with the given function.
series.mapValues(x -> x+7);
series.mapValues(Math::sqrt);
//The same thing. Unary is a helper class that contains some standard unary operations.
Unary.sqrt(series);
  
//Create new Series that have the same domain as series1,
//and the values are additions of the corresponding values 
//from series1 and series2. More details on that later.
//There is a bunch of standard binary operators in the Binary class.   
Binary.add(series1, series2)

//Create new series with the same domain, and the values 
//contain rolling average with window size 30.
//There are several more rolling-window operations in the class Moving.
//See also the DirtyFunctions section below.
Moving.avg(series, 30);
```

The binary operations, such as ``Binary.add(series1, series2)``, 
might be a bit mysterious because the two series may have different domains.
Here is where the functional nature of series helps naturally.

Series provide a (maybe a bit esoteric) operation with a horrible signature:
```java
Series<T, R> zipWithValues(Function<? super T, ? extends R> operand2,
                           BiFunction<? super R, ? super R, ? extends R> operator);
```
It expects a 1-arg function and a 2-argument function as parameters.
The result is a Series with the same domain, where the values are calculated like this:
For each time point, apply the function *operand2* on the time point, and feed the result, 
together with the value point to *operator*.
 Now, imagine what *operand2* is another Series, and the *operator* is simply ``(x,y) -> x+y``.
This is exactly how ``Binary.add(s1,s2)`` works.
The resulting domain is same as ``s1.domain``. 
It takes the points from s1, one by one,
it searches for a point with lower or equal time in s2, 
and adds the two values. Clear semantics, and works for any combination of domains.

### DirtyFunctions

Dirty function is an implementation of the Function interface, which internally remembers 
some state, so the result of a function call depends on previous calls. 
It's generally not a recommended practice, but it can be a great tool if used with caution. 
I use dirty functions in the Moving transformations mentioned in the previous section,
and they are often useful on their own, outside the domain of Series.

```java
//This creates a function that calculates moving average.
//When you invoke it with an argument, it returns the average 
//of the arguments of the last 3 calls (or less for the first calls). 
Function<Double, Double> movingAvg = DirtyFunctions.movingAvg(3);

System.out.println(movingAvg.apply(1.0));	//prints 1    - 1/1
System.out.println(movingAvg.apply(2.0));   //prints 1.5  - (1+2)/2
System.out.println(movingAvg.apply(4.0));   //prints 2.33 - (1+2+4)/3
System.out.println(movingAvg.apply(8.0));   //prints 4.66 - (2+4+8)/3
System.out.println(movingAvg.apply(16.0));  //prints 9.33 - (4+8+16)/3 
```

There are similar dirty functions available for moving max, min, sum, and quantile.
They are all implemented using a ring buffer and reusing previous results
as much as possible, so they scale very well for large window sizes.
Because they implement the Function, interface they can be easily composed and compounded,
and can be used for fast calculations in realtime processing.    

Let's say we have a sensor providing some realtime input, and we want to know the square root 
of the difference between the minimum and the maximum value for the last 100 measurements.
We compose moving minimum and maximum with Math.sqrt, and we get a new dirty function.
We feed the input into is as it comes and the function returns the desired statistics based on the recent history.
It's a very efficient and error-prone way to implement sophisticated real-time data processors.

```java
Function<Double, Double> recentMin = DirtyFunctions.movingMin(100);
Function<Double, Double> recentMax = DirtyFunctions.movingMax(100);
Function<Double, Double> recentRange = x -> recentMax.apply(x) - recentMin.apply(x);
Function<Double, Double> sqrt = Math::sqrt;

Function<Double, Double> dirtyComposite = sqrt.compose(recentRange);

System.out.println(dirtyComposite.apply(0.0));		//prints 0    - sqrt(0 - 0)
System.out.println(dirtyComposite.apply(10.0));		//prints 3.16 - sqrt(10 - 0) 
System.out.println(dirtyComposite.apply(-10.0));    //prints 4.47 - sqrt(10 - (-10))
System.out.println(dirtyComposite.apply(20.0));     //prints 5.47 - sqrt(20 - (-10))
System.out.println(dirtyComposite.apply(30.0));     //prints 6.32 - sqrt(30 - (-10))
```
