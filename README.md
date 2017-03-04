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
so that the code from the data modeling tools can be easily translated to it,
It's pure Java with no dependencies, and aims to run as fast and memory-efficiently as Java can get.

Please, do not expect a full Numpy port to Java. It gets nowhere near and has no such ambition. 
I support just a tiny subset of the functionality, which was enough to satisfy my practical needs: 
only 1D arrays and time series, and a small set of predefined operations. 
Nevertheless, I believe there are many developers whose needs in this respect are equally limited and 
who can benefit from the library just like I do.

And it's easy to extend and add the missing functionality your own.

## Quick Example

**TODO**

## Build & Install

* Clone the repo
  * ``git clone https://github.com/jan-x-marek/jseries``
* Build the project
  * ``./gradlew install``
* Use it
  * Add the resulting jar (``build/libs/jseries-1.0.jar``) to your classpath
  * Or add the artifact ``com.jmt:jseries:1.0`` to your gradle or maven dependencies.  

## Design Principles

**TODO**

## Stability

I have been using the code in production in several projects for more than a year, so I daresay it's safe and stable.

## Completeness

**TODO**

## Quick Guide 
 
**TODO**

