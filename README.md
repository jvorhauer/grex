# GReat EXpectations (grex)

A lightweight and simpler derivate of [assertj](https://assertj.github.io/doc/).

## Why?

AssertJ is great, one of the first dependencies I add to any new JVM project.

But assertj is not always allowed, please do not ask *me* why, but some companies
have the wrong kind of paranoia when it comes to importing external dependencies.

So I developed a very lightweight subset of assertj, but with the `expect` idiom, 
like [jest](https://jestjs.io/). I feel that `expect`ing something is more applicable to comparing expected results with the actual outcome of actions.

## Getting started

Check the *Tests. It should get you started and will include more elaborate examples of what can be done with `grex`.

Eventually real documentation will be added to this project, but at this time the status of the project makes documentation seem a bit premature.

### Maven

```xml
<dependency>
  <groupId>org.vorticoso</groupId>
  <atifactId>grex</atifactId>
  <version>${grex.version.latest}</version>
</dependency>
```

The latest version can be found in the `releases` part of the GitHub repository.
