package com.github.promeg.xlog_android.lib;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class StringsTest {
    @Test
    public void testCollectionToString(){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);

        String result = Strings.toString(list);

        String expected = "[1, 2, 3]";

        assertThat(result, is(expected));
    }

    @Test
    public void testLargeCollectionToString(){
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < 1000; i++) {
            list.add(123456);
        }

        String result = Strings.toString(list);

        String expectedTail = "] (126:1000)";

        assertThat(result.endsWith(expectedTail), is(true));
        assertThat(result.length(), lessThan(Strings.LOG_CONTENT_MAX_LENGTH + 20));
    }

    @Test
    public void testLargeObjectArrayToString(){
        Integer[] list = new Integer[1000];
        for(int i = 0; i < 1000; i++) {
            list[i] = 123456;
        }

        String result = Strings.toString(list);

        String expectedTail = "] (126:1000)";

        assertThat(result.endsWith(expectedTail), is(true));
        assertThat(result.length(), lessThan(Strings.LOG_CONTENT_MAX_LENGTH + 20));
    }

    @Test
    public void testLargePrimitiveArrayToString(){
        int[] list = new int[1000];
        for(int i = 0; i < 1000; i++) {
            list[i] = 123456;
        }

        String result = Strings.toString(list);

        String expectedTail = "] (126:1000)";

        assertThat(result.endsWith(expectedTail), is(true));
        assertThat(result.length(), lessThan(Strings.LOG_CONTENT_MAX_LENGTH + 20));
    }
}
