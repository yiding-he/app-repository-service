package com.hyd.remoteapplauncher;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class UtilTest {

    @Test
    public void subArray() {
        String[] arr = {"1", "2", "3"};
        assertArrayEquals(new String[]{"2", "3"}, Util.subArray(arr, 1));
    }
}
