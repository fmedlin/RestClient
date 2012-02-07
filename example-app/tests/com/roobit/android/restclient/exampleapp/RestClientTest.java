package com.roobit.android.restclient.exampleapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RestClientTest {

	@Test
	public void shouldHaveHappySmiles() throws Exception {
        String hello = new TestActivity().getResources().getString(R.string.hello);
        assertEquals(hello, "Hello, world");
    }
}
