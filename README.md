NAry
==============

A small Java project to represent the compile time n-arity uncertainty of elements.  
In plain english, a compile time type that can represent 0, 1, or n elements in run time.    

Nary is the child of Optional and a Stream. With Nary you get the enhanced interface 
of Stream available to optionals, and allows treating a single object Stream as 
an Optional.  

You will, usually, use it to represent search results. Where you can't decide 
on compile time how many results you will get on runtime but, at the same time, 
allow the client code to be flexible about the n-arity of the result.

```java
Nary<Records> foundRecords = service.makeApiCall(filterArguments)
```
Depending on the filter arguments, that same api call could return multiple results, 
or just maybe 1 (if ever). To allow clients of the api better express that ambiguity
Nary allows you to treat them as both Optional and Stream.   


## Examples
- **As a Stream:**
```java
List<Integer> even = integersUpTo(4)
  .filter(number -> number % 2 == 0)
  .collectToList()
```
In this example the creator of `integersUpTo()` doesn't know how many results
it will produce back, so Nary is used as a Type to represent that uncertainty.  
However when we call it we know there's only 4 possible results (1,2,3,4).  
Because a Nary is a Stream we can operate over it normally: filter even numbers
and collect them to a list.  


- **As an Optional:**
```java
Integer result = integersUpTo(1) 
  .get() // Assume there's only one element
```
Again in this example we call previous method `integersUpTo()` but
because we used `1` as argument, we know we know at compile time
that only one result will be produced. So we can use Nary as a non empty Optional.  
   
By using Nary the creator of `integersUpTo()` doesn't require different return types
to support these two use cases, and offers better expressibility to client code.   

#### Disclaimer
Due to Java design limitations, `java.util.Optional` is a final class and 
cannot be used for inheritance. So, Nary is a Stream, but it's not an Optional.
It offers similar methods as Optional to be used as such, but it's not a drop-in
replacement.  
 
## Using Nary
### Maven dependency ###

* Declare the dependency
```
<dependency>
  <groupId>info.kfgodel</groupId>
  <artifactId>nary</artifactId>
  <version>2.0.7</version>
</dependency>
```

## Caveats

#### Calling Optional methods on a Stream
Because Nary is at the same time a Stream and an Optional type if you mix them
the wrong way you will get a runtime exception.
You can think of an Optional as a special case of Stream (it's a Stream with only
1 element, or none), so treating an Optional like a Stream works,
treating a Stream like an optional only works if it doesn't contain more than 1
element.
  
If a Nary instance has more than 1 contained element and you call any Optional
based methods then it will fail being unable to pick 1 element.
If you call any method on a 1 element Nary, everything works. 
**So, unless explicitly indicated** ensure there's no more than 1 element
before calling Optional methods on a random Nary 

#### Reusing instances
Nary instances should be considered temporary object as you do with streams.
You use it for transformation and then move it to another storage structure like
a collection (or whatever you need). They are disposable objects.

The only exception to this rule is a 1-element nary that can be re-used (as with
Optional).  