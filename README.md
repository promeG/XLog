# XLog
Annotation-triggered method call logging based on Xposed.

## What's XLog?

Does the same thing like [Hugo](https://github.com/JakeWharton/hugo). Print method calls, arguments, return values, and the excute time by simply add `@XLog` to a method.

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

## How to use XLog

### Step 1

Install [Xposed Framework](http://repo.xposed.info/module/de.robv.android.xposed.installer) and [XLog module](http://repo.xposed.info/module/com.promegu.xlog.xposedmodule).

### Step 2

Add XLog to your project.

```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    provided 'com.github.promeg:xlog-compiler:1.0'
  }
}
```

### Step 3

There is no step 3 :-)

## Why not [Hugo](https://github.com/JakeWharton/hugo)?

Hugo perform bytecode weaving to do the logging, so it cannot work with other bytecode-weaving-based libraries such as [retrolambda](https://github.com/orfjackal/retrolambda), [loglifecycle](https://github.com/stephanenicolas/loglifecycle).

Moreover, Hugo cannot work on Android Library Module.

XLog solves above problems by using Xposed Framework. 

Reference: 

[Hugo + Retrolambda - is it possible to use both?](https://github.com/JakeWharton/hugo/issues/78)

[Lambdas and Method Reference to work with @DebugLog](https://github.com/JakeWharton/hugo/issues/77)

[DebugLog does not work in library module](https://github.com/JakeWharton/hugo/issues/80)

[Libraries' @DebugLog doesn't work](https://github.com/JakeWharton/hugo/issues/31)
