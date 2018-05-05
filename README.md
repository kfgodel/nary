NAry
==============

A small Java project to represent the compile time n-arity uncertainty of elements.  
In plain english, a type that can represent 0, 1, or n elements.    

Nary is the merged type of an Optional and a Stream. It gets the enhaced interface of stream
available to optionals, and allows treating a single object streams as optionals.  

You will, usually, use it to represent search results. Where you can't decide 
on compile time how many results you will get on runtime, but at the same time 
allow the client code to be flexible about the n-arity of the result.

**Nary  example**
```
Integer onlyEven= Nary.of(1, 2, 3)
  .filterNary(number -> number % 2 == 0)
  .get();
```
In this example a Nary is created as a stream of 3 elements, and 2 are 
filtered out looking for even numbers. Because we know on runtime there's only
one element on the Nary, we use it as an optional calling `get()`



**Optional example:**
```
Optional.ofNullable(something)
  .ifAbsent(()-> LOG.warn("something is missing"))
  .ifPresent(nonNullSomething -> LOG.info("We got something: {}", nonNullSomething));
```
This example only shows the extend `ifAbsent()` variant method that is ironically 
absent in java.util.Optional interface. 

Due to Java design limitations, `java.util.Optional` cannot be extended so we 
use a replacement type `ar.com.kfgodel.nary.api.optionals.Optional` to extend its 
interface. This type should be preferred over original implementation. 
 

### Maven dependency ###

* Declare the dependency
```
<dependency>
  <groupId>info.kfgodel</groupId>
  <artifactId>nary</artifactId>
  <version>2.0.7</version>
  <scope>test</scope>
</dependency>
```

### Caveat
Because Nary is at the same time a stream and an optional type, you can get to 
weird runtime situations. Like treating a Nary with more than one element as 
an optional, or trying to reuse a stream nary.  

Nary instances should be considered temporary object as you do with streams.
You use it for tranformation and then move it to another storage structure like
a collection.

The only exception to this rule is when you know that there's 0 or exactly 1 element.
In that case you can re-use instances like you would do with standard Optionals.  