package com.khinf.mobile.console.cert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WebAppTest extends TestCase {
	public WebAppTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(WebAppTest.class);
	}

	public void testWebApp() {
		assertTrue(true);
	}
}
