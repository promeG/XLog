# XLog
Method call logging based on [dexposed](https://github.com/alibaba/dexposed).

## IMPORTANT
From 2.0 XLog uses [dexposed](https://github.com/alibaba/dexposed) instead of [xposed](http://repo.xposed.info/) to do the work, so there is no need to install xposed framework or root your phone.

## What's XLog?

### 1. XLog a method
Does the same thing like [Hugo](https://github.com/JakeWharton/hugo). Print method calls, arguments, return values, and the execute time by simply add `@XLog` to a method.

```java
@XLog
public String sayHello(String name) {
    return "Hello, " + name;
}
```
```
D/MainActivity: ⇢ sayHello(name="promeg")
D/MainActivity: ⇠ sayHello[0ms] = "Hello, promeg"
```

### 2. XLog a class
Log all declared methods and constructors of a class.

This includes public, protected, default (package) access, and private methods or constructors, but excludes inherited ones.


```java
@XLog
public class BaseCalculator {

    public int calculate(int i, int j){
        return i+j;
    }
}

@XLog
public class SampleCalculator extends BaseCalculator {

}


new SampleCalculator().calculate(1, 2);

```
```
D/SampleCalculator: ⇢ com.promegu.xloggerexample.SampleCalculator()
D/BaseCalculator: ⇢ com.promegu.xloggerexample.BaseCalculator()
D/BaseCalculator: ⇠ com.promegu.xloggerexample.BaseCalculator [0ms]
D/SampleCalculator: ⇠ com.promegu.xloggerexample.SampleCalculator [0ms]
D/BaseCalculator: ⇢ calculate(int=1, int=2)
D/BaseCalculator: ⇠ calculate [0ms] = 3
```

## How to use XLog

Add XLog to your project.

XLog will do the logging only in debug builds. In release builds, XLog will do noting and the annotation itself will not present.

```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    debugCompile 'com.github.promeg:xlog-compiler:2.0' // ~6kB
    debugCompile 'com.github.promeg:xlog-android:2.0' // ~150kB

    releaseCompile 'com.github.promeg:xlog-android-idle:2.0' // ~5kB
  }
}
```


## Why not [Hugo](https://github.com/JakeWharton/hugo)?

Hugo perform bytecode weaving to do the logging, so it cannot work with other bytecode-weaving-based libraries such as [retrolambda](https://github.com/orfjackal/retrolambda), [loglifecycle](https://github.com/stephanenicolas/loglifecycle).

Moreover, Hugo cannot work on Android Library Module.

XLog solves above problems by using dexposed.

Reference: 

[Hugo + Retrolambda - is it possible to use both?](https://github.com/JakeWharton/hugo/issues/78)

[Lambdas and Method Reference to work with @DebugLog](https://github.com/JakeWharton/hugo/issues/77)

[DebugLog does not work in library module](https://github.com/JakeWharton/hugo/issues/80)

[Libraries' @DebugLog doesn't work](https://github.com/JakeWharton/hugo/issues/31)
