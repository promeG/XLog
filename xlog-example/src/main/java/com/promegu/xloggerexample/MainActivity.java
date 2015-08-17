package com.promegu.xloggerexample;

import android.app.Activity;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.promegu.xlog.base.XLog;

import java.util.Collection;
import java.util.HashSet;

public class MainActivity extends Activity {

    @XLog
    public MainActivity() {
    }

    @Override
    @XLog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Check logcat!");
        setContentView(tv);

        SampleCalculator sampleCalculator = new SampleCalculator();
        sampleCalculator.calculate(1, 2);

        printArgs("The", "Quick", "Brown", "Fox");

        Log.i("Fibonacci", "fibonacci's 4th number is " + fibonacci(4));

        Greeter greeter = new Greeter("Jake");
        Log.d("Greeting", greeter.sayHello());

        Charmer charmer = new Charmer("Jake");
        Log.d("Charming", charmer.askHowAreYou());

        startSleepyThread();

        Collection<Integer> largeCollection = new HashSet<>();
        for(int i = 1000; i < 2000; i++){
            largeCollection.add(i);
        }

        xlogLargeCollection(largeCollection);

        int[] ints = new int[1000];
        for(int i = 0; i < ints.length; i++){
            ints[i] = i;
        }

        xlogLargeArray(ints);
    }


    @Override
    @XLog
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    @XLog
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @XLog
    private void printArgs(String... args) {
        for (String arg : args) {
            Log.i("Args", arg);
        }
    }

    @XLog
    private void xlogLargeCollection(Collection collection){
        Log.i("Collection size", String.valueOf(collection.size()));
    }

    @XLog
    private void xlogLargeArray(int[] ints){
        Log.i("Large array size", String.valueOf(ints.length));
    }

    @XLog
    private int fibonacci(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Number must be greater than zero.");
        }
        if (number == 1 || number == 2) {
            return 1;
        }
        // NOTE: Don't ever do this. Use the iterative approach!
        return fibonacci(number - 1) + fibonacci(number - 2);
    }

    private void startSleepyThread() {
        new Thread(new Runnable() {
            private static final long SOME_POINTLESS_AMOUNT_OF_TIME = 50;

            @Override public void run() {
                sleepyMethod(SOME_POINTLESS_AMOUNT_OF_TIME);
            }

            // also support anonymous class
            @XLog
            private void sleepyMethod(long milliseconds) {
                SystemClock.sleep(milliseconds);
            }
        }, "I'm a lazy thr.. bah! whatever!").start();
    }

    static class Greeter {
        private final String name;

        @XLog
        Greeter(String name) {
            this.name = name;
        }

        @XLog
        public String sayHello() {
            return "Hello, " + name;
        }
    }

    static class Charmer {
        private final String name;

        @XLog
        Charmer(String name) {
            this.name = name;
        }

        public String askHowAreYou() {
            return "How are you " + name + "?";
        }
    }
}
