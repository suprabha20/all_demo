package aexp.junit;

import junit.framework.TestSuite;
import junit.framework.Assert;

public class SomeTest extends TestSuite {
	public void testSomething() throws Throwable
	{
	       Assert.assertTrue(1 + 1 == 2);
	}

	public void testSomethingElse() throws Throwable
	{
	       Assert.assertTrue(1 + 1 == 3);
	}
}
