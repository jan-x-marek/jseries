# JSeries

Ultra-lightweight time series library for Java 8

* Fast, memory-efficient
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
  * Or add the artifact ``com.jmt:jseries:1.0`` to your gradle or maven dependencies.  

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

## Random Design Notes

Data are stored only in memory, in Java arrays, specifically in primitive arrays wherever possible.
This helps to achieve the best speed and memory efficiency.

Everything is immutable. Arrays are encapsulated in immutable wrappers. 
It keeps the semantics simple and eliminates any concurrency troubles.

There is only a single set of interfaces, used both for primitive and non-primitive types. 
For example, there is an Array<T> interface, which has a generic implementation for
an array of Objects, and a specific implementation DoubleArray (of type Array<Double>),
which stores the values as primitives internally.
It greatly simplifies the API, compared to the standard Java APIs (functions, streams)
where every interface is copied many times for every primitive type.
Naturally, it might affects performance due to some extra boxing/unboxing.
However, I did quite some performance benchmarking to see how much the generic
interface really slows things down, and the difference was small, certainly worth the tradeoff.
Apparently, Java 8 runtime is doing excellent job with runtime optimization.

**TODO** 

## Quick Guide
 
**TODO**

