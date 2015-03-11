package com.promegu.xloggerexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.promegu.xlog.base.XLog;


public class MainActivity extends ActionBarActivity {

    @XLog
    public MainActivity() {
    }

    @Override
    @XLog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 10; i < 20; i++){
            getNumber(i);
            Number number = new Number(i);
            getNumber(number);
            getNumber(number, i, true, false);
        }
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
    private String getNumber(int number){
        Log.d("MainActivity", "getNumber:  " + number);
        return String.valueOf(number);
    }

    @XLog
    private Number getNumber(Number number){
        Log.d("MainActivity", "getNumber2:  " + number);
        return number;
    }

    @XLog
    private Number getNumber(Number number,int i, boolean b, Boolean B){
        Log.d("MainActivity", "getNumber2:  " + number);
        return number;
    }
}
