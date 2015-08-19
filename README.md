# XLog
Method call logging based on [dexposed](https://github.com/alibaba/dexposed).

## What's XLog?

### 1. XLog a method
Print method calls, arguments, return values, and the execute time by simply add `@XLog` to a method.

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

### 3. XLog more
#####XLog a method that you cannot access to the source code

```java
List<XLogMethod> xLogMethods;
xLogMethods.add(new XLogMethod(TextView.class, "setText"));

XLogConfig.config(XLogConfig.newConfigBuilder(this)
                .logMethods(xLogMethods)
                .build());


textView.setText("Hello, promeG!");
```

```
D/TextView﹕ ⇢ setText(CharSequence="Hello, promeG!")
D/TextView﹕ ⇢ setText(CharSequence="Hello, promeG!", BufferType=NORMAL)
D/TextView﹕ ⇢ setText(CharSequence="Hello, promeG!", BufferType=NORMAL, boolean=true, int=0)
D/TextView﹕ ⇠ setText [0ms]
D/TextView﹕ ⇠ setText [0ms]
D/TextView﹕ ⇠ setText [0ms]
```

#####Ignore a method's log if its running time less than a time threshold

```java
XLogConfig.config(XLogConfig.newConfigBuilder(this)
                .timeThreshold(10)
                .build());
```




## How to use XLog

### 1. Add XLog to your project.

XLog will do the logging only in debug builds. In release builds, XLog will do noting and the annotation itself will not present.

```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    debugCompile 'com.github.promeg:xlog-compiler:2.1.1' // ~6kB
    debugCompile 'com.github.promeg:xlog-android:2.1.1' // ~150kB

    releaseCompile 'com.github.promeg:xlog-android-idle:2.1.1' // ~5kB
  }
}
```

### 2. Configure XLog in your `Application` class

```java
public class MyApplication extends Application {
  public void onCreate() {
    super.onCreate();
    XLogConfig.config(XLogConfig.newConfigBuilder(this)
                    .logMethods(List<XLogMethod> xLogMethod) //optional
                    .timeThreshold(long timeInMillis) // optional
                    .build());
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
