package aexp.junit;

import junit.framework.TestSuite;

public class ExampleSuite extends TestSuite {
	/**
	 * 需要测试的类
	 */
	public ExampleSuite() {
		addTestSuite(MathTest.class);
		addTestSuite(ContactTest.class);
		addTestSuite(SomeTest.class);
	}
}
